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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DetalhesAgrupamento  extends AppCompatActivity {

    private SQLiteDatabase bancoDados;
    private DatabaseHelper dbHelper;

    private MateriaAdapterAdicionado adicionarMateriaAdapter;

    private List<Materia> listaDeMaterias = new ArrayList<>();

    private RecyclerView recyclerView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalhes_agrupamento);


        dbHelper = new DatabaseHelper(this);

        ImageView btnClose = findViewById(R.id.ImageClose);

        int agrupamentoId = getIntent().getIntExtra("AGRUPO_ID", -1);
        Agrupamento agrupamentoSelecionado = obterAgrupamentoPorId(agrupamentoId);


        recyclerView = findViewById(R.id.recyclerViewMateriasAdicionadas);
        MateriaAdapterAdicionado adapter = new MateriaAdapterAdicionado(obterMateriasPorAgrupamento(agrupamentoId), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setRecyclerView(recyclerView);

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
                exibirDialogoAdicaoMateriaDetalhes(dialog,agrupamentoId);

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

            List<Materia> listaDeMaterias = obterMateriasPorAgrupamento(agrupamentoId);


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


    public List<Materia> obterMateriasPorAgrupamento(long idAgrupamento) {
        List<Materia> materias = new ArrayList<>();

        try {
            DatabaseHelper dbHelper = new DatabaseHelper(this);
            SQLiteDatabase bancoDados = dbHelper.getReadableDatabase();

            // Colunas que você deseja recuperar
            String[] colunas = {"id_materia", "nome_materia", "dia_semana", "id_agrupamento","quantAulas"};

            // Condição para a cláusula WHERE
            String selecao = "id_agrupamento = ?";
            String[] argumentos = {String.valueOf(idAgrupamento)};

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


    private void exibirDialogoAdicaoMateriaDetalhes(final Dialog dialog, int idAgrupamento) {


        // Obter referências para os EditTexts e Spinner no layout do diálogo
        EditText editTextNomeMateria = dialog.findViewById(R.id.textViewNomeMateria);
        EditText editTextQuantAula = dialog.findViewById(R.id.textViewQuantAulas);
        Spinner spinnerDiaSemana = dialog.findViewById(R.id.spinnerDataMateria);

        // Configurar o adaptador para o Spinner com a lista de dias da semana
        List<String> listaDeDiasSemana = Arrays.asList("Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado", "Domingo");
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, R.layout.spinner_preto, listaDeDiasSemana);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDiaSemana.setAdapter(spinnerAdapter);


        View buttonSalvarMateria = dialog.findViewById(R.id.buttonRemover);

        buttonSalvarMateria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nomeMateria = editTextNomeMateria.getText().toString();
                String quantAula = editTextQuantAula.getText().toString();
                String diaSemana = spinnerDiaSemana.getSelectedItem().toString();

                salvarMateriaDetalhes(dialog, nomeMateria, quantAula,  diaSemana, idAgrupamento);


            }
        });
    }


    private void salvarMateriaDetalhes(Dialog dialog,String nomeMateria,String quantAula, String diaSemana, int idAgrupamento) {

        // Lógica para salvar a matéria
        EditText editTextNomeMateria = dialog.findViewById(R.id.textViewNomeMateria);
        EditText editTextQuantAula = dialog.findViewById(R.id.textViewQuantAulas);
        Spinner spinnerDiaSemana = dialog.findViewById(R.id.spinnerDataMateria);



        if (!TextUtils.isEmpty(nomeMateria) && !TextUtils.isEmpty(quantAula)) {
            // Criar uma nova instância de Materia com os valores inseridos
            try {
                DatabaseHelper dbHelper = new DatabaseHelper(this);
                SQLiteDatabase bancoDados = dbHelper.getWritableDatabase();

                ContentValues valuesMateria = new ContentValues();
                valuesMateria.put("nome_materia", nomeMateria);
                valuesMateria.put("dia_semana", diaSemana);
                valuesMateria.put("id_agrupamento", idAgrupamento);
                valuesMateria.put("quantAulas", quantAula);

                Log.d("TAG", "NomeMateriaInsert: " + nomeMateria + " DataSemanaInsert: " + diaSemana + " QuantAula: " + quantAula);

                bancoDados.insert("materias", null, valuesMateria);

                bancoDados.close();

                Materia novaMateria = new Materia(nomeMateria, diaSemana, idAgrupamento, 0, quantAula);

                recyclerView = findViewById(R.id.recyclerViewMateriasAdicionadas);
                MateriaAdapterAdicionado adapter = new MateriaAdapterAdicionado(obterMateriasPorAgrupamento(idAgrupamento), this);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                adapter.setRecyclerView(recyclerView);

                adapter.notifyDataSetChanged();



            } catch (Exception e) {
                e.printStackTrace();
                Log.e("TAG", "Erro ao inserir dados na tabela materias: " + e.getMessage());
            }
            dialog.dismiss();

        } else {
            Toast.makeText(getApplicationContext(), "Preencha todos os campos", Toast.LENGTH_SHORT).show();

        }

    }

}
