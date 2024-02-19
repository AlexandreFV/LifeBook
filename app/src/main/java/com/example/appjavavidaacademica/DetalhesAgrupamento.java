package com.example.appjavavidaacademica;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DetalhesAgrupamento  extends AppCompatActivity {

    private SQLiteDatabase bancoDados;
    private DatabaseHelper dbHelper;

    private MateriaAdapterAdicionado adicionarMateriaAdapter;

    private List<Materia> listaDeMaterias = new ArrayList<>();

    private RecyclerView recyclerView;

    private String filtroDiaSemana;

    private Spinner spinnerFiltro;

    private View filtroView;

    private int agrupamentoId;

    private static String diaSemanaSelecionado;

    private static String materiaNomeSelecionada;

    private static String quantAulasSelecionada;


    private TextView NomeCardFiltrar;

    private TextView materiaNomeFiltrar;

    private TextView materiaQuantAulaFiltrar;

    private View removeFiltro;

    private TextView removerFiltroText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalhes_agrupamento);

        diaSemanaSelecionado = null;
        materiaNomeSelecionada = null;
        quantAulasSelecionada = null;

        dbHelper = new DatabaseHelper(this);

        ImageView btnClose = findViewById(R.id.ImageClose);

        agrupamentoId = getIntent().getIntExtra("AGRUPO_ID", -1);
        Agrupamento agrupamentoSelecionado = obterAgrupamentoPorId(agrupamentoId);

        recyclerView = findViewById(R.id.recyclerViewMateriasAdicionadas);
        MateriaAdapterAdicionado adapter = new MateriaAdapterAdicionado(obterMateriasPorAgrupamento(agrupamentoId, filtroDiaSemana), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setRecyclerView(recyclerView);
        adapter.notifyDataSetChanged();

        filtroView = findViewById(R.id.filtrar);

        filtroView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            DialogFiltrarMateria(adapter);
            }
        });

         removeFiltro = findViewById(R.id.removeFiltro);
         removerFiltroText = findViewById(R.id.removerFiltroText);


        layoutInferiorBotoes barraInferior = new layoutInferiorBotoes(this, findViewById(R.id.includeDetalhesInf), findViewById(R.id.AdicioneAlgoScreen));

        ImageView btnMaisMateria = findViewById(R.id.btnMaisMateria);
        btnMaisMateria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listaDeMaterias.clear();

                final Dialog dialog = new Dialog(DetalhesAgrupamento.this, android.R.style.Theme_Translucent_NoTitleBar);
                dialog.setContentView(R.layout.dialog_add_materia);

                ImageView buttonCancelar = dialog.findViewById(R.id.buttonCancelarCard);

                buttonCancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Fecha o Dialog
                        dialog.dismiss();
                    }
                });

                dialog.show();
                exibirDialogoAdicaoMateriaDetalhes(dialog,agrupamentoId,adapter);

            }
        });


        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

                // Iniciar a MainActivity
                Intent intent = new Intent(DetalhesAgrupamento.this, MainActivity.class);
                startActivity(intent);
            }
        });

        removeFiltro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetarLista(adapter);
                diaSemanaSelecionado = null;
                materiaNomeSelecionada = null;
                quantAulasSelecionada = null;
            }
        });

        if (agrupamentoSelecionado != null) {
            // Aqui você pode usar as informações do agrupamentoSelecionado
            String nomeAgrupamento = agrupamentoSelecionado.getNomeAgrupamento();
            String categoria = agrupamentoSelecionado.getCategoria();
            String corFundoHex = agrupamentoSelecionado.getCorFundoHex();
            String corBotoesHex = agrupamentoSelecionado.getCorButtonHex();
            String corTextoHex = agrupamentoSelecionado.getCorTextoHex();
            int iconEscolhido = agrupamentoSelecionado.getIconEscolhido();

            int corFundoInt = Color.parseColor(corFundoHex);
            int corTextoInt = Color.parseColor(corTextoHex);
            int corBotoesInt = Color.parseColor(corBotoesHex);

            View fundoDetalhesFaixa = findViewById(R.id.FundoDetalhesFaixa);
            fundoDetalhesFaixa.setBackgroundColor(corFundoInt);


            View cardDetails = findViewById(R.id.CardDetalhes);
            cardDetails.setBackgroundColor(corFundoInt);
            GradientDrawable drawable = new GradientDrawable();
            drawable.setColor(corFundoInt);
            drawable.setCornerRadius(70);
            cardDetails.setBackground(drawable);

            // Exemplo de como usar as informações (substitua pelos seus próprios requisitos)
            TextView textViewNome = findViewById(R.id.TextNomeCard);
            TextView textViewCategoria = findViewById(R.id.TextCategoriaCard);
            TextView textViewCorHex = findViewById(R.id.TextCorHex);
            ImageView IconView = findViewById(R.id.IconImageAdicionada);

            textViewNome.setText("Nome: " + nomeAgrupamento);
            textViewCategoria.setText("Categoria: " + categoria);
            textViewCorHex.setText("Cor Hex: " + corFundoHex);
            IconView.setImageResource(iconEscolhido);

            textViewNome.setTextColor(corTextoInt);
            textViewCategoria.setTextColor(corTextoInt);

            List<Materia> listaDeMaterias = obterMateriasPorAgrupamento(agrupamentoId,filtroDiaSemana);


            // Agora você pode usar essas informações como necessário
        } else {
            // O agrupamento com o ID correspondente não foi encontrado
            Log.e("TAG", "Agrupamento não encontrado para o ID: " + agrupamentoId);
        }
    }

    private Agrupamento obterAgrupamentoPorId(int agrupamentoId) {
        List<Agrupamento> agrupamentos = new ArrayList<>();

        try {
            bancoDados = dbHelper.getReadableDatabase();
            Cursor cursor = bancoDados.rawQuery("SELECT * FROM agrupamento WHERE id_agrupamento = ?", new String[]{String.valueOf(agrupamentoId)});

            if (cursor != null && cursor.moveToFirst()) {
                int nomeAgrupamentoIndex = cursor.getColumnIndex("nome_agrupamento");
                int categoriaIndex = cursor.getColumnIndex("categoria");
                int corFundoHexIndex = cursor.getColumnIndex("corFundoHex");
                int corTextoHexIndex = cursor.getColumnIndex("corTextoHex");
                int corButtonHexIndex = cursor.getColumnIndex("corBotoesHex");
                int iconEscolhidoIndex = cursor.getColumnIndex("iconEscolhido");

                if (nomeAgrupamentoIndex >= 0 && categoriaIndex >= 0 && corFundoHexIndex >= 0 && corTextoHexIndex >= 0 && corButtonHexIndex >= 0 && iconEscolhidoIndex >= 0) {
                    String nomeAgrupamento = cursor.getString(nomeAgrupamentoIndex);
                    String categoria = cursor.getString(categoriaIndex);
                    String corFundoHex = cursor.getString(corFundoHexIndex);
                    String corBotoesHex = cursor.getString(corButtonHexIndex);
                    String corTextoHex = cursor.getString(corTextoHexIndex);
                    int iconEscolhido = cursor.getInt(iconEscolhidoIndex);

                    return new Agrupamento(nomeAgrupamento, categoria, corFundoHex, agrupamentoId, corTextoHex, corBotoesHex, iconEscolhido);
                } else {
                    Log.e("TAG", "Índice de coluna inválido");
                }
            }
            bancoDados.close();

            if (cursor != null) {
                cursor.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TAG", "Erro ao obter agrupamento do banco de dados: " + e.getMessage());
        }
        return null;
    }


    public List<Materia> obterMateriasPorAgrupamento(long idAgrupamento, String filtroDiaSemana) {
        List<Materia> materias = new ArrayList<>();

        try {
            DatabaseHelper dbHelper = new DatabaseHelper(this);
            SQLiteDatabase bancoDados = dbHelper.getReadableDatabase();

            // Colunas que você deseja recuperar
            String[] colunas = {"id_materia", "nome_materia", "dia_semana", "id_agrupamento", "quantAulas"};

            // Condição para a cláusula WHERE
            String selecao = "id_agrupamento = ?";
            String[] argumentos = {String.valueOf(idAgrupamento)};

            if (!TextUtils.isEmpty(filtroDiaSemana)) {
                selecao += " AND dia_semana = ?";
                argumentos = new String[]{String.valueOf(idAgrupamento), filtroDiaSemana};
            }

            Cursor cursor = bancoDados.query("materias", colunas, selecao, argumentos, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int idMateriaIndex = cursor.getColumnIndex("id_materia");
                    int nomeMateriaIndex = cursor.getColumnIndex("nome_materia");
                    int diaSemanaIndex = cursor.getColumnIndex("dia_semana");
                    int idAgrupamentoIndex = cursor.getColumnIndex("id_agrupamento");
                    int aulaPorDiaIndex = cursor.getColumnIndex("quantAulas");

                    if (idMateriaIndex >= 0 && nomeMateriaIndex >= 0 && diaSemanaIndex >= 0 && idAgrupamentoIndex >= 0 && aulaPorDiaIndex >= 0) {
                        int idMateria = cursor.getInt(idMateriaIndex);
                        String nomeMateria = cursor.getString(nomeMateriaIndex);
                        String diaSemana = cursor.getString(diaSemanaIndex);
                        int idAgrupamentoBanco = cursor.getInt(idAgrupamentoIndex);
                        String quantAulas = cursor.getString(aulaPorDiaIndex);

                        Materia materia = new Materia(nomeMateria, diaSemana, idAgrupamentoBanco, idMateria, quantAulas);
                        materias.add(materia);

                        Log.d("TAG", "nomeMateria: " + nomeMateria);
                        Log.d("TAG", "diaSemana: " + diaSemana);
                        Log.d("TAG", "idMateria: " + idMateria);
                        Log.d("TAG", "quantAula: " + quantAulas);

                    } else {
                        Log.e("TAG", "Índice de coluna inválido");
                    }
                } while (cursor.moveToNext());
            }

            if (cursor != null) {
                cursor.close();
            }

            bancoDados.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TAG", "Erro ao obter matérias por agrupamento: " + e.getMessage());
        }

        return materias;
    }


    public void consultarRegistros() {
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(this);
            SQLiteDatabase bancoDados = dbHelper.getReadableDatabase();

            // Colunas que você deseja recuperar
            String[] colunas = {"id_materia", "nome_materia", "dia_semana", "id_agrupamento","quantAulas"};

            // Condição para a cláusula WHERE (pode ser vazia para selecionar todos os registros)
            String selecao = "";
            String[] argumentos = {};

            Cursor cursor = bancoDados.query("materias", colunas, selecao, argumentos, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int idMateriaIndex = cursor.getColumnIndex("id_materia");
                    int nomeMateriaIndex = cursor.getColumnIndex("nome_materia");
                    int diaSemanaIndex = cursor.getColumnIndex("dia_semana");
                    int idAgrupamentoIndex = cursor.getColumnIndex("id_agrupamento");

                    if (idMateriaIndex >= 0 && nomeMateriaIndex >= 0 && diaSemanaIndex >= 0 && idAgrupamentoIndex >= 0) {
                        long idMateria = cursor.getLong(idMateriaIndex);
                        String nomeMateria = cursor.getString(nomeMateriaIndex);
                        String diaSemana = cursor.getString(diaSemanaIndex);
                        long idAgrupamento = cursor.getLong(idAgrupamentoIndex);

                        // Exiba os valores no Log ou faça o que desejar com eles
                        Log.d("TAG", "ID Materia: " + idMateria);
                        Log.d("TAG", "Nome Materia: " + nomeMateria);
                        Log.d("TAG", "ID Agrupamento: " + idAgrupamento);

                    } else {
                        Log.e("TAG", "Índice de coluna inválido");
                    }
                } while (cursor.moveToNext());
            }

            if (cursor != null) {
                cursor.close();
            }

            bancoDados.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TAG", "Erro ao consultar registros: " + e.getMessage());
        }
    }

    public void exibirMensagemComAnimacaoDetalhesCard() {

        final View fundoSuccessDetalhesCard = findViewById(R.id.fundoSuccessDetalhesCard);
        final TextView textAdicionadoSuccessDetalhesCard = findViewById(R.id.textAdicionadoSuccessDetalhesCard);
        final View successIconDetalhesCard = findViewById(R.id.successIconDetalhesCard);

        // Configurar a animação de entrada
        TranslateAnimation entradaAnimation = new TranslateAnimation(
                1000,  // Da posição x 1000dp para fora da tela (à direita)
                0,     // Para a posição x 0dp (posição inicial)
                0,
                0);
        entradaAnimation.setDuration(500);

        fundoSuccessDetalhesCard.startAnimation(entradaAnimation);
        textAdicionadoSuccessDetalhesCard.startAnimation(entradaAnimation);
        successIconDetalhesCard.startAnimation(entradaAnimation);

        // Tornar as Views visíveis após a animação de entrada
        fundoSuccessDetalhesCard.setVisibility(View.VISIBLE);
        textAdicionadoSuccessDetalhesCard.setVisibility(View.VISIBLE);
        successIconDetalhesCard.setVisibility(View.VISIBLE);

        // Agendar a animação de saída após alguns segundos
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Configurar a animação de saída
                TranslateAnimation saidaAnimation = new TranslateAnimation(
                        0,   // Da posição x 0dp (posição atual)
                        1000, // Para a posição x 1000dp para fora da tela (à direita)
                        0,
                        0);
                saidaAnimation.setDuration(500);

                // Iniciar a animação de saída
                fundoSuccessDetalhesCard.startAnimation(saidaAnimation);
                textAdicionadoSuccessDetalhesCard.startAnimation(saidaAnimation);
                successIconDetalhesCard.startAnimation(saidaAnimation);

                // Definir a visibilidade das Views como GONE após a animação de saída
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fundoSuccessDetalhesCard.setVisibility(View.GONE);
                        textAdicionadoSuccessDetalhesCard.setVisibility(View.GONE);
                        successIconDetalhesCard.setVisibility(View.GONE);
                    }
                }, 500);  // Aguarde 500 milissegundos antes de tornar as Views invisíveis
            }
        }, 2000);  // Aguarde 2000 milissegundos (2 segundos) antes de iniciar a animação de saída
    }


    private void exibirDialogoAdicaoMateriaDetalhes(final Dialog dialog, int idAgrupamento,MateriaAdapterAdicionado adapter) {


        // Obter referências para os EditTexts e Spinner no layout do diálogo
        EditText editTextNomeMateria = dialog.findViewById(R.id.textViewNomeMateria);
        EditText editTextQuantAula = dialog.findViewById(R.id.textViewQuantAulas);
        Spinner spinnerDiaSemana = dialog.findViewById(R.id.spinnerDataMateria);

        // Configurar o adaptador para o Spinner com a lista de dias da semana
        List<String> listaDeDiasSemana = Arrays.asList("Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado", "Domingo");
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, R.layout.spinner_preto, listaDeDiasSemana);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDiaSemana.setAdapter(spinnerAdapter);


        View buttonSalvarMateria = dialog.findViewById(R.id.buttonAdicionar);

        buttonSalvarMateria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nomeMateria = editTextNomeMateria.getText().toString();
                String quantAula = editTextQuantAula.getText().toString();
                String diaSemana = spinnerDiaSemana.getSelectedItem().toString();

                salvarMateriaDetalhes(dialog, nomeMateria, quantAula,  diaSemana, idAgrupamento, adapter);


            }
        });
    }


    private void salvarMateriaDetalhes(Dialog dialog, String nomeMateria, String quantAula, String diaSemana, int idAgrupamento, MateriaAdapterAdicionado adapter) {

        // Lógica para salvar a matéria
        EditText editTextNomeMateria = dialog.findViewById(R.id.textViewNomeMateria);
        EditText editTextQuantAula = dialog.findViewById(R.id.textViewQuantAulas);
        Spinner spinnerDiaSemana = dialog.findViewById(R.id.spinnerDataMateria);

        if (!TextUtils.isEmpty(nomeMateria) && !TextUtils.isEmpty(quantAula)) {
            try {
                DatabaseHelper dbHelper = new DatabaseHelper(this);
                SQLiteDatabase bancoDados = dbHelper.getWritableDatabase();

                ContentValues valuesMateria = new ContentValues();
                valuesMateria.put("nome_materia", nomeMateria);
                valuesMateria.put("dia_semana", diaSemana);
                valuesMateria.put("id_agrupamento", idAgrupamento);
                valuesMateria.put("quantAulas", quantAula);

                bancoDados.insert("materias", null, valuesMateria);

                bancoDados.close();

                // Criar uma nova instância de Materia com os valores inseridos
                Materia novaMateria = new Materia(nomeMateria, diaSemana, idAgrupamento, 0, quantAula);


                // Adicionar a nova matéria à lista existente
                listaDeMaterias.add(novaMateria);

                // Notificar o adapter sobre a mudança nos dados
                adapter.setMaterias(listaDeMaterias);


                // Fechar o diálogo
                dialog.dismiss();

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("TAG", "Erro ao inserir dados na tabela materias: " + e.getMessage());
            }
        } else {
            Toast.makeText(getApplicationContext(), "Preencha todos os campos", Toast.LENGTH_SHORT).show();
        }
    }


    private void updateRecyclerView(int agrupamentoId, String filtroDiaSemana) {
        recyclerView = findViewById(R.id.recyclerViewMateriasAdicionadas);
        MateriaAdapterAdicionado adapter;

        if ("Todas".equals(filtroDiaSemana)) {
            // Se "Todas" for selecionado, obtenha todas as matérias sem considerar o dia da semana
            adapter = new MateriaAdapterAdicionado(obterMateriasPorAgrupamento(agrupamentoId, null), this);
        } else {
            // Caso contrário, obtenha matérias considerando o dia da semana selecionado
            adapter = new MateriaAdapterAdicionado(obterMateriasPorAgrupamento(agrupamentoId, filtroDiaSemana), this);
        }

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setRecyclerView(recyclerView);
        adapter.notifyDataSetChanged();
    }

    private void DialogFiltrarMateria(MateriaAdapterAdicionado adapter){
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_filtrar_materias_detalhes_card, null);

        final Dialog dialog = new Dialog(this, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        dialog.setContentView(dialogView);

        // Configurar para ocupar toda a tela
        dialog.getWindow().setLayout(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT
        );

        NomeCardFiltrar = dialogView.findViewById(R.id.NomeCardFiltrar);
        materiaNomeFiltrar = dialogView.findViewById(R.id.textView12);
        materiaQuantAulaFiltrar = dialogView.findViewById(R.id.textView11);

        if(diaSemanaSelecionado == null){
            NomeCardFiltrar.setText("Todos");
        }

        if(diaSemanaSelecionado != null){
            NomeCardFiltrar = dialog.findViewById(R.id.NomeCardFiltrar);
            NomeCardFiltrar.setText(diaSemanaSelecionado);
        }

        if(materiaNomeSelecionada == null){
            materiaNomeFiltrar.setText("Todos");
        }

        if(materiaNomeSelecionada != null){
            materiaNomeFiltrar = dialog.findViewById(R.id.textView12);
            materiaNomeFiltrar.setText(materiaNomeSelecionada);
        }

        if(quantAulasSelecionada == null){
            materiaQuantAulaFiltrar.setText("Todos");
        }

        if(quantAulasSelecionada != null){
            materiaQuantAulaFiltrar = dialog.findViewById(R.id.textView11);
            materiaQuantAulaFiltrar.setText(String.valueOf(quantAulasSelecionada));
        }


        View ViewCardFilter = dialogView.findViewById(R.id.ViewCardFilter);
        View ViewMateriaFilter = dialogView.findViewById(R.id.ViewMateriaFilter);
        View ViewQuantFaltaFilter = dialogView.findViewById(R.id.ViewQuantFaltaFilter);

        if (ViewCardFilter != null) {
            ViewCardFilter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogFiltrarDiaS();
                }
            });
        }

        if (ViewMateriaFilter != null) {
            ViewMateriaFilter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogFiltrarMateriaN();
                }
            });
        }

        if (ViewQuantFaltaFilter != null) {
            ViewQuantFaltaFilter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogFiltrarQuantAulas();
                }
            });
        }


        // Exibir o Dialog
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent); // Define o fundo transparente
        dialog.show();


        ImageView imageView9 = dialog.findViewById(R.id.imageView9);

        imageView9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.filtrarDados(diaSemanaSelecionado, materiaNomeSelecionada,quantAulasSelecionada);

                dialog.dismiss();

                if (diaSemanaSelecionado != "Todos" && diaSemanaSelecionado != null || materiaNomeSelecionada != "Todos" && materiaNomeSelecionada != null || quantAulasSelecionada != "Todos" && quantAulasSelecionada != null){

                    removeFiltro.setVisibility(View.VISIBLE);
                    removerFiltroText.setVisibility(View.VISIBLE);

                    if (adapter.getItemCount() == 0) {
                        TextView TextParabensCriterio = findViewById(R.id.textView3);

                        TextParabensCriterio.setVisibility(View.VISIBLE);
                    }else {

                        TextView TextParabensCriterio = findViewById(R.id.textView3);

                        TextParabensCriterio.setVisibility(View.GONE);
                    }
                }

            }
        });

    }


    private void DialogFiltrarDiaS(){
        // Criar o segundo Dialog
        Dialog segundoDialog = new Dialog(DetalhesAgrupamento.this, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        segundoDialog.setContentView(getLayoutInflater().inflate(R.layout.dialog_filtrar_diasemana_detalhes, null));

        RadioGroup radioGroupCard = segundoDialog.findViewById(R.id.radioGroupDiaS);

        List<String> listaDeDiasDaSemana = obterListaDeDiasDaSemanaDoBanco(agrupamentoId);
        // Adicione opções do Card dinamicamente
        if (!listaDeDiasDaSemana.isEmpty()) {
            for (String dia : listaDeDiasDaSemana) {
                RadioButton radioButton = new RadioButton(getApplicationContext());
                radioButton.setText(dia);
                radioGroupCard.addView(radioButton);
                // Definir a visibilidade do texto informativo como GONE
                /*
                textView14 = segundoDialog.findViewById(R.id.TextNPF);
                textView14.setVisibility(View.GONE);

                 */
            }
        } else {
            // Se a lista estiver vazia, tornar o RadioGroup visível e exibir um texto informativo
            radioGroupCard.setVisibility(View.VISIBLE);
            /*
            textView14 = segundoDialog.findViewById(R.id.TextFiltrarDiaS);
            textView14.setVisibility(View.VISIBLE);

             */
        }

        int limiteCard = 4; // Altere conforme necessário
        if (radioGroupCard.getChildCount() > limiteCard) {
            ScrollView scrollViewCard = segundoDialog.findViewById(R.id.scrollViewCard);
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) scrollViewCard.getLayoutParams();
            layoutParams.height = getResources().getDimensionPixelSize(R.dimen.radio_group_max_height);
            scrollViewCard.setLayoutParams(layoutParams);
        }

        ImageView btnCloseFiltCard = segundoDialog.findViewById(R.id.imageView10);
        btnCloseFiltCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selectedCardId = radioGroupCard.getCheckedRadioButtonId();
                if (selectedCardId != -1) {
                    RadioButton selectedCard = segundoDialog.findViewById(selectedCardId);

                    diaSemanaSelecionado = selectedCard.getText().toString();
                    NomeCardFiltrar.setText(diaSemanaSelecionado);

                    String materiaSelecionada = "Todos";


                }


                segundoDialog.dismiss();
            }
        });

        // Configurar para ocupar toda a tela
        segundoDialog.getWindow().setLayout(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT
        );

        // Exibir o segundo Dialog
        segundoDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        segundoDialog.show();

                    /*
                    // Fechar o primeiro Dialog se necessário
                    dialog.dismiss();
                     */
    }



    private List<String> obterListaDeDiasDaSemanaDoBanco(int IdAgrupamento) {
        List<String> listaDeDiasSemana = new ArrayList<>();
        listaDeDiasSemana.add("Todos");

        try {
            // Abrir o banco de dados em modo somente leitura
            bancoDados = dbHelper.getReadableDatabase();

            // Consulta SQL com cláusula WHERE para filtrar por IdAgrupamento
            String sqlQuery = "SELECT DISTINCT dia_semana FROM materias WHERE id_agrupamento = ?";

            // Argumentos da consulta SQL (o ID do agrupamento)
            String[] args = {String.valueOf(IdAgrupamento)};

            // Executar a consulta SQL com argumentos
            Cursor cursor = bancoDados.rawQuery(sqlQuery, args);

            // Criar um conjunto para manter o controle dos dias já encontrados
            Set<String> diasEncontrados = new HashSet<>();

            // Iterar sobre os resultados do cursor
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String dia = cursor.getString(cursor.getColumnIndex("dia_semana"));
                    diasEncontrados.add(dia);
                } while (cursor.moveToNext());
            }

            // Fechar o cursor
            if (cursor != null) {
                cursor.close();
            }

            // Array com os dias da semana na ordem desejada
            String[] diasDaSemana = {"Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado", "Domingo"};

            // Adicionar os dias encontrados à lista, na ordem desejada
            for (String dia : diasDaSemana) {
                if (diasEncontrados.contains(dia)) {
                    listaDeDiasSemana.add(dia);
                }
            }

            // Adicionar "Todos" se nenhum dia for encontrado
            if (listaDeDiasSemana.isEmpty()) {
                listaDeDiasSemana.add("Todos");
            }

        } catch (Exception e) {
            // Log de erro em caso de exceção
            Log.e("Erro", "Erro ao obter lista de dias da semana: " + e.getMessage());
        } finally {
            // Fechar o banco de dados
            if (bancoDados != null) {
                bancoDados.close();
            }
        }

        return listaDeDiasSemana;
    }



    private void DialogFiltrarMateriaN(){
        // Criar o segundo Dialog
        Dialog segundoDialog = new Dialog(DetalhesAgrupamento.this, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        segundoDialog.setContentView(getLayoutInflater().inflate(R.layout.dialog_filtrar_materia_detalhes, null));

        RadioGroup radioGroupCard = segundoDialog.findViewById(R.id.radioGroupMateriaN);

        List<String> listaDeMaterias = obterListaDeMateriasDoBanco(agrupamentoId);
        // Adicione opções do Card dinamicamente
        if (!listaDeMaterias.isEmpty()) {
            for (String materia : listaDeMaterias) {
                RadioButton radioButton = new RadioButton(getApplicationContext());
                radioButton.setText(materia);
                radioGroupCard.addView(radioButton);
                // Definir a visibilidade do texto informativo como GONE
                /*
                textView14 = segundoDialog.findViewById(R.id.TextNPF);
                textView14.setVisibility(View.GONE);

                 */
            }
        } else {
            // Se a lista estiver vazia, tornar o RadioGroup visível e exibir um texto informativo
            radioGroupCard.setVisibility(View.VISIBLE);
            /*
            textView14 = segundoDialog.findViewById(R.id.TextFiltrarDiaS);
            textView14.setVisibility(View.VISIBLE);

             */
        }

        int limiteCard = 4; // Altere conforme necessário
        if (radioGroupCard.getChildCount() > limiteCard) {
            ScrollView scrollViewCard = segundoDialog.findViewById(R.id.scrollViewCard);
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) scrollViewCard.getLayoutParams();
            layoutParams.height = getResources().getDimensionPixelSize(R.dimen.radio_group_max_height);
            scrollViewCard.setLayoutParams(layoutParams);
        }

        ImageView btnCloseFiltCard = segundoDialog.findViewById(R.id.imageView10);
        btnCloseFiltCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selectedCardId = radioGroupCard.getCheckedRadioButtonId();
                if (selectedCardId != -1) {
                    RadioButton selectedCard = segundoDialog.findViewById(selectedCardId);

                    materiaNomeSelecionada = selectedCard.getText().toString();
                    materiaNomeFiltrar.setText(String.valueOf(materiaNomeSelecionada));

                    String materiaSelecionada = "Todos";


                }


                segundoDialog.dismiss();
            }
        });

        // Configurar para ocupar toda a tela
        segundoDialog.getWindow().setLayout(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT
        );

        // Exibir o segundo Dialog
        segundoDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        segundoDialog.show();

                    /*
                    // Fechar o primeiro Dialog se necessário
                    dialog.dismiss();
                     */
    }


    private List<String> obterListaDeMateriasDoBanco(int agrupamentoId) {
        List<String> listaDeMaterias = new ArrayList<>();
        listaDeMaterias.add("Todos");

        try {
            bancoDados = dbHelper.getReadableDatabase();
            String sqlQuery = "SELECT nome_materia FROM materias WHERE id_agrupamento = ?";
            String[] args = {String.valueOf(agrupamentoId)};
            Cursor cursor = bancoDados.rawQuery(sqlQuery, args);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String materia = cursor.getString(cursor.getColumnIndex("nome_materia"));
                    listaDeMaterias.add(materia);
                } while (cursor.moveToNext());
            }

            if (cursor != null) {
                cursor.close();
            }

            // Adicionar "Todos" se nenhum dia for encontrado
            if (listaDeMaterias.isEmpty()) {
                listaDeMaterias.add("Todos");
            }

        } catch (Exception e) {
            Log.e("Erro", "Erro ao obter lista de matérias: " + e.getMessage());
        } finally {
            if (bancoDados != null) {
                bancoDados.close();
            }
        }

        return listaDeMaterias;
    }


    private void DialogFiltrarQuantAulas(){
        // Criar o segundo Dialog
        Dialog segundoDialog = new Dialog(DetalhesAgrupamento.this, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        segundoDialog.setContentView(getLayoutInflater().inflate(R.layout.dialog_filtrar_quantdias_layout, null));

        RadioGroup radioGroupCard = segundoDialog.findViewById(R.id.radioGroupMateriaN);

        // Adicionar a opção "Todos" ao RadioGroup
        RadioButton radioButtonTodos = new RadioButton(getApplicationContext());
        radioButtonTodos.setText("Todos");
        radioGroupCard.addView(radioButtonTodos);

        List<Integer> valoresQuantAulas = obterValoresQuantAulasDoAgrupamento(agrupamentoId);
        Collections.sort(valoresQuantAulas);



        // Adicione opções do Card dinamicamente
        if (!valoresQuantAulas.isEmpty()) {
            // Adicionar botões de rádio para cada valor de quantAulas ao RadioGroup
            for (int valorQuantAulas : valoresQuantAulas) {
                RadioButton radioButton = new RadioButton(getApplicationContext());

                radioButton.setText(String.valueOf(valorQuantAulas));
                radioGroupCard.addView(radioButton);
            }

    } else {
            // Se a lista estiver vazia, tornar o RadioGroup visível e exibir um texto informativo
            radioGroupCard.setVisibility(View.VISIBLE);
            /*
            textView14 = segundoDialog.findViewById(R.id.TextFiltrarDiaS);
            textView14.setVisibility(View.VISIBLE);

             */
        }

        int limiteCard = 4; // Altere conforme necessário
        if (radioGroupCard.getChildCount() > limiteCard) {
            ScrollView scrollViewCard = segundoDialog.findViewById(R.id.scrollViewCard);
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) scrollViewCard.getLayoutParams();
            layoutParams.height = getResources().getDimensionPixelSize(R.dimen.radio_group_max_height);
            scrollViewCard.setLayoutParams(layoutParams);
        }

        ImageView btnCloseFiltCard = segundoDialog.findViewById(R.id.imageView10);
        btnCloseFiltCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selectedCardId = radioGroupCard.getCheckedRadioButtonId();
                if (selectedCardId != -1) {
                    RadioButton selectedCard = segundoDialog.findViewById(selectedCardId);

                    quantAulasSelecionada = selectedCard.getText().toString();
                    materiaQuantAulaFiltrar.setText(quantAulasSelecionada);

                    String materiaSelecionada = "Todos";

                }


                segundoDialog.dismiss();
            }
        });

        // Configurar para ocupar toda a tela
        segundoDialog.getWindow().setLayout(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT
        );

        // Exibir o segundo Dialog
        segundoDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        segundoDialog.show();

                    /*
                    // Fechar o primeiro Dialog se necessário
                    dialog.dismiss();
                     */
    }


    private List<Integer> obterValoresQuantAulasDoAgrupamento(int agrupamentoId) {
        List<Integer> valoresQuantAulas = new ArrayList<>();

        try {
            bancoDados = dbHelper.getReadableDatabase();

            // Consulta SQL para obter todos os valores de quantAulas para o agrupamento atual
            String sqlQuery = "SELECT quantAulas FROM materias WHERE id_agrupamento = ?";
            String[] args = {String.valueOf(agrupamentoId)};
            Cursor cursor = bancoDados.rawQuery(sqlQuery, args);

            if (cursor != null && cursor.moveToFirst()) {
                int quantAulasIndex = cursor.getColumnIndex("quantAulas");

                if (quantAulasIndex >= 0) {
                    do {
                        int valorQuantAulas = cursor.getInt(quantAulasIndex);
                        if (!valoresQuantAulas.contains(valorQuantAulas)) {
                            valoresQuantAulas.add(valorQuantAulas);
                        }
                    } while (cursor.moveToNext());
                }
            }

            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            Log.e("Erro", "Erro ao obter valores de quantAulas do agrupamento: " + e.getMessage());
        } finally {
            if (bancoDados != null) {
                bancoDados.close();
            }
        }

        return valoresQuantAulas;
    }

    public void resetarLista(MateriaAdapterAdicionado tableAdapter) {
        // Recarregue a lista original ou faça a lógica necessária para restaurar o estado original
        tableAdapter.resetarListaOriginal();

        removeFiltro.setVisibility(View.GONE);
        removerFiltroText.setVisibility(View.GONE);

        TextView TextParabensCriterio = findViewById(R.id.textView3);

        TextParabensCriterio.setVisibility(View.GONE);

    }

}
