package com.example.appjavavidaacademica;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ListaFaltas extends AppCompatActivity {

    private RecyclerView recyclerView;

    private SQLiteDatabase bancoDados;
    private RadioGroup radioGroupCard;  // Adicione esta variável de instância

    private static String cardSelecionado;

    private static String quantFaltasSelecionada;

    private static String materiaSelecionada;

    private DatabaseHelper dbHelper;

    private ImageView btnClose;

    private List<String> listaDeAgrupamentos;

    private View Filtrar;

    private View RemoveFiltro;

    private View ViewCardFilter;

    private View ViewQuantFilter;

    private View ViewMaterias;


    private TextView textRemoveFiltro;

    private TextView textView10;

    private TextView textView11;

    private TextView textView12;

    private TextView textView14;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_faltas);

        recyclerView = findViewById(R.id.recyclerViewFaltas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        cardSelecionado = null;
        quantFaltasSelecionada = null;
        materiaSelecionada = null;

        ViewCardFilter = findViewById(R.id.ViewCardFilter);
        ViewQuantFilter = findViewById(R.id.ViewQuantFaltaFilter);
        ViewMaterias = findViewById(R.id.ViewMateriaFilter);

        // Obter dados da tabela de faltas
        List<Faltas> listaDeFaltas = obterFaltas(this);

        FaltasAdapter tableAdapter = new FaltasAdapter(listaDeFaltas);

        recyclerView.setAdapter(tableAdapter);

        layoutInferiorBotoes barraInferior = new layoutInferiorBotoes(this, findViewById(R.id.includeDesempenhoInf), findViewById(R.id.AdicioneAlgoScreen));


        btnClose = findViewById(R.id.imageCloseListaDeFaltas);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar a atividade DesempenhoActivity quando o botão desempenho for clicado
                finish();
                Intent intent = new Intent(ListaFaltas.this, DesempenhoActivity.class);
                startActivity(intent);
            }
        });

        Filtrar = findViewById(R.id.Filtrar);
        Filtrar.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            /*
            List<String> listaDeAgrupamentos = obterListaDeAgrupamentosDoBanco(); // substitua por sua lógica
            mostrarDialogFiltrar(listaDeAgrupamentos, tableAdapter);
             */

            Log.d("TAG"," " + cardSelecionado);



            exibirDialogPersonalizado(listaDeAgrupamentos,tableAdapter);
        }
        });

        RemoveFiltro = findViewById(R.id.RemoveFiltro);

        RemoveFiltro.setVisibility(View.GONE);
        textRemoveFiltro = findViewById(R.id.RemoverFiltroText);
        textRemoveFiltro.setVisibility(View.GONE);
        RemoveFiltro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetarLista(tableAdapter);
                cardSelecionado = null;
                quantFaltasSelecionada = null;
            }
        });



    }

    public List<Faltas> obterFaltas(Context context) {
        List<Faltas> listaDeFaltas = new ArrayList<>();

        try {
            dbHelper = new DatabaseHelper(context);
            bancoDados = dbHelper.getReadableDatabase();

            // Colunas que você deseja obter
            String[] colunas = {"faltas.data", "faltas.id_materia", "faltas.quantidade", "faltas.motivo", "faltas.id_faltas", "materias.id_agrupamento"};

            Cursor cursor = bancoDados.query("faltas INNER JOIN materias ON faltas.id_materia = materias.id_materia", colunas, null, null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    // Obter dados da tabela faltas
                    int id_faltas = cursor.getInt(cursor.getColumnIndex("id_faltas"));
                    String data = cursor.getString(cursor.getColumnIndex("data"));
                    int idMateria = cursor.getInt(cursor.getColumnIndex("id_materia"));
                    int quantidade = cursor.getInt(cursor.getColumnIndex("quantidade"));
                    String motivo = cursor.getString(cursor.getColumnIndex("motivo"));
                    String nomeMateria = obterNomeMateria(idMateria);
                    int idAgrupamento = cursor.getInt(cursor.getColumnIndex("id_agrupamento"));

                    String nomeAgrupamento = obterNomeAgrupamento(idAgrupamento);


                    // Criar objeto Falta e adicionar à lista
                    Faltas falta = new Faltas(data, idMateria, quantidade, motivo, nomeMateria, nomeAgrupamento);
                    listaDeFaltas.add(falta);

                    // Adicionar um Log para imprimir os valores
                    Log.d("TAG", "id_faltas: " + id_faltas + ", data: " + data + ", idMateria: " + idMateria + ", quantidade: " + quantidade + ", motivo: " + motivo + ", Card: " + nomeAgrupamento);

                } while (cursor.moveToNext());

                cursor.close();
            }

            bancoDados.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TAG", "Erro ao obter dados da tabela faltas: " + e.getMessage());
        }

        return listaDeFaltas;

    }

    private String obterNomeMateria(int idMateria) {
        String nomeMateria = "";

        try {
            // Consulta à tabela materias para obter o nome da matéria
            String[] colunasMaterias = {"nome_materia"};
            Cursor cursorMaterias = bancoDados.query("materias", colunasMaterias, "id_materia = ?",
                    new String[]{String.valueOf(idMateria)}, null, null, null);

            if (cursorMaterias != null && cursorMaterias.moveToFirst()) {
                nomeMateria = cursorMaterias.getString(cursorMaterias.getColumnIndex("nome_materia"));

                cursorMaterias.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TAG", "Erro ao obter nome da matéria: " + e.getMessage());
        }

        return nomeMateria;
    }

    private String obterNomeAgrupamento(int idAgrupamento) {
        String nomeAgrupamento = "";

        try {
            // Consulta à tabela agrupamento para obter o nome do agrupamento
            String[] colunasAgrupamento = {"nome_agrupamento"};
            Cursor cursorAgrupamento = bancoDados.query("agrupamento", colunasAgrupamento, "id_agrupamento = ?",
                    new String[]{String.valueOf(idAgrupamento)}, null, null, null);

            if (cursorAgrupamento != null && cursorAgrupamento.moveToFirst()) {
                nomeAgrupamento = cursorAgrupamento.getString(cursorAgrupamento.getColumnIndex("nome_agrupamento"));

                cursorAgrupamento.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TAG", "Erro ao obter nome do agrupamento: " + e.getMessage());
        }
        listaDeAgrupamentos = obterListaDeAgrupamentosDoBanco(); // substitua por sua lógica

        return nomeAgrupamento;
    }


    public void mostrarDialogFiltrar(List<String> listaDeAgrupamentos, FaltasAdapter tableAdapter) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.dialog_filtrar_lista_faltas, null);
        builder.setView(dialogView);

        RadioGroup radioGroupCard = dialogView.findViewById(R.id.radioGroupDiaS);
        RadioGroup radioGroupFaltas = dialogView.findViewById(R.id.radioGroupFaltas);

        // Adicione opções do Card dinamicamente
        for (String agrupamento : listaDeAgrupamentos) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(agrupamento);
            radioGroupCard.addView(radioButton);
        }

        // Adicione opções de Quantidade de Faltas dinamicamente com base na quantidade máxima
        int quantidadeMaximaFaltas = obterQuantidadeMaximaFaltas(); // Implemente essa função
        for (int i = 1; i <= quantidadeMaximaFaltas; i++) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText("+" + i);
            radioGroupFaltas.addView(radioButton);
        }

        // Defina o limite para o número de itens no RadioGroup do Card
        int limiteCard = 4; // Altere conforme necessário
        if (radioGroupCard.getChildCount() > limiteCard) {
            ScrollView scrollViewCard = dialogView.findViewById(R.id.scrollViewCard);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) scrollViewCard.getLayoutParams();
            layoutParams.height = getResources().getDimensionPixelSize(R.dimen.radio_group_max_height);
            scrollViewCard.setLayoutParams(layoutParams);
        }

        int limiteQuantF = 4; // Altere conforme necessário
        if (radioGroupFaltas.getChildCount() > limiteQuantF) {
            ScrollView scrollViewQuantFaltas = dialogView.findViewById(R.id.scrollViewQuantFaltas);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) scrollViewQuantFaltas.getLayoutParams();
            layoutParams.height = getResources().getDimensionPixelSize(R.dimen.radio_group_max_height);
            scrollViewQuantFaltas.setLayoutParams(layoutParams);
        }

        builder.setPositiveButton("Filtrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Lógica para lidar com a seleção
                int selectedCardId = radioGroupCard.getCheckedRadioButtonId();
                int selectedFaltasId = radioGroupFaltas.getCheckedRadioButtonId();

                if (selectedCardId != -1 || selectedFaltasId != -1) {
                    // Pelo menos um filtro foi selecionado
                    String cardSelecionado = "";
                    String quantidadeFaltas = "";

                    if (selectedCardId != -1) {
                        RadioButton selectedCard = dialogView.findViewById(selectedCardId);
                        cardSelecionado = selectedCard.getText().toString();
                    }

                    if (selectedFaltasId != -1) {
                        RadioButton selectedFaltas = dialogView.findViewById(selectedFaltasId);
                        quantidadeFaltas = selectedFaltas.getText().toString();
                    }

                    // Faça algo com os valores selecionados
                    Log.d("TAG", "Card Selecionado: " + cardSelecionado);

                    // Aplique o filtro com base nos valores selecionados
                    tableAdapter.filtrarDados(cardSelecionado, quantidadeFaltas,materiaSelecionada);
                    RemoveFiltro.setVisibility(View.VISIBLE);
                    textRemoveFiltro.setVisibility(View.VISIBLE);

                    if (tableAdapter.getItemCount() == 0) {
                        TextView TextParabensCriterio = findViewById(R.id.TextParabensCriterio);
                        View fundoParabens = findViewById(R.id.fundoParabens);

                        TextParabensCriterio.setVisibility(View.VISIBLE);
                        fundoParabens.setVisibility(View.VISIBLE);

                    } else {
                        RemoveFiltro.setVisibility(View.VISIBLE);
                        textRemoveFiltro.setVisibility(View.VISIBLE);

                        TextView TextParabensCriterio = findViewById(R.id.TextParabensCriterio);
                        View fundoParabens = findViewById(R.id.fundoParabens);

                        TextParabensCriterio.setVisibility(View.GONE);
                        fundoParabens.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Selecione pelo menos um filtro", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }



    private List<String> obterListaDeAgrupamentosDoBanco() {
        List<String> listaDeAgrupamentos = new ArrayList<>();

        try {
            dbHelper = new DatabaseHelper(this);
            bancoDados = dbHelper.getReadableDatabase();

            // Consulta à tabela agrupamento para obter a lista de agrupamentos
            String[] colunasAgrupamento = {"nome_agrupamento"};
            Cursor cursorAgrupamento = bancoDados.query("agrupamento", colunasAgrupamento, null,
                    null, null, null, null);

            if (cursorAgrupamento != null && cursorAgrupamento.moveToFirst()) {
                do {
                    String nomeAgrupamento = cursorAgrupamento.getString(cursorAgrupamento.getColumnIndex("nome_agrupamento"));
                    listaDeAgrupamentos.add(nomeAgrupamento);
                } while (cursorAgrupamento.moveToNext());

                cursorAgrupamento.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TAG", "Erro ao obter lista de agrupamentos: " + e.getMessage());
        }

        return listaDeAgrupamentos;
    }


    private int obterQuantidadeMaximaFaltas() {
        int quantidadeMaxima = 0;

        try {
            dbHelper = new DatabaseHelper(this);
            bancoDados = dbHelper.getReadableDatabase();

            Cursor cursor = bancoDados.rawQuery("SELECT MAX(quantidade) FROM faltas", null);

            if (cursor != null && cursor.moveToFirst()) {
                quantidadeMaxima = cursor.getInt(0);
                cursor.close();
            }

            bancoDados.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TAG", "Erro ao obter quantidade máxima de faltas: " + e.getMessage());
        }

        return quantidadeMaxima;
    }

    private List<String> obterNomesDasMateriasDoBanco() {
        List<String> nomesDasMaterias = new ArrayList<>();

        try {
            dbHelper = new DatabaseHelper(this);
            bancoDados = dbHelper.getReadableDatabase();

            // Consulta à tabela de matérias para obter os nomes
            String[] colunasMaterias = {"nome_materia"};

            // Adicionando condição para verificar cardSelecionado
            String selecaoCard = (cardSelecionado == null || cardSelecionado.equals("Todos")) ? null : "id_agrupamento = (SELECT id_agrupamento FROM agrupamento WHERE nome_agrupamento = '" + cardSelecionado + "')";

            Cursor cursorMaterias = bancoDados.query("materias", colunasMaterias, selecaoCard,
                    null, null, null, null);

            if (cursorMaterias != null && cursorMaterias.moveToFirst()) {
                do {
                    String nomeMateria = cursorMaterias.getString(cursorMaterias.getColumnIndex("nome_materia"));
                    nomesDasMaterias.add(nomeMateria);
                } while (cursorMaterias.moveToNext());

                cursorMaterias.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TAG", "Erro ao obter nomes das matérias: " + e.getMessage());
        } finally {
            // Certifique-se de fechar o banco de dados
            if (bancoDados != null && bancoDados.isOpen()) {
                bancoDados.close();
            }
        }

        return nomesDasMaterias;
    }


    public void resetarLista(FaltasAdapter tableAdapter) {
            // Recarregue a lista original ou faça a lógica necessária para restaurar o estado original
            tableAdapter.resetarListaOriginal();

            RemoveFiltro.setVisibility(View.GONE);
            textRemoveFiltro.setVisibility(View.GONE);

            TextView TextParabensCriterio = findViewById(R.id.TextParabensCriterio);
            View fundoParabens = findViewById(R.id.fundoParabens);

            TextParabensCriterio.setVisibility(View.GONE);
            fundoParabens.setVisibility(View.GONE);

        }


    private void exibirDialogPersonalizado(List<String> listaDeAgrupamentos,FaltasAdapter tableAdapter) {
        // Inflar o layout personalizado
        View dialogView = getLayoutInflater().inflate(R.layout.layout_filtrar_faltas_v2, null);

        // Criar um Dialog
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        dialog.setContentView(dialogView);

        textView10 = dialog.findViewById(R.id.NomeCardFiltrar);
        textView11 = dialog.findViewById(R.id.textView11);
        textView12 = dialog.findViewById(R.id.textView12);

        if(cardSelecionado == null){
            textView10.setText("Todos");
        }

        if (quantFaltasSelecionada == null){
            textView11.setText("Todos");
        }

        if (materiaSelecionada == null){
            textView12.setText("Todos");
        }

        // Configurar para ocupar toda a tela
        dialog.getWindow().setLayout(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT
        );

        ImageView imageView9 = dialog.findViewById(R.id.imageView9);

        View ViewCardFilter = dialogView.findViewById(R.id.ViewCardFilter);
        View ViewQuantFilter = dialogView.findViewById(R.id.ViewQuantFaltaFilter);
        View ViewMaterias = dialogView.findViewById(R.id.ViewMateriaFilter);

        imageView9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tableAdapter.filtrarDados(cardSelecionado, quantFaltasSelecionada,materiaSelecionada);
                dialog.dismiss();

                if (cardSelecionado != "Todos" && cardSelecionado != null || quantFaltasSelecionada != "Todos" && quantFaltasSelecionada != null || materiaSelecionada != "Todos" && materiaSelecionada != null){
                    RemoveFiltro.setVisibility(View.VISIBLE);
                    textRemoveFiltro.setVisibility(View.VISIBLE);

                    if (tableAdapter.getItemCount() == 0) {
                        TextView TextParabensCriterio = findViewById(R.id.TextParabensCriterio);
                        View fundoParabens = findViewById(R.id.fundoParabens);

                        TextParabensCriterio.setVisibility(View.VISIBLE);
                        fundoParabens.setVisibility(View.VISIBLE);
                    }else {

                        TextView TextParabensCriterio = findViewById(R.id.TextParabensCriterio);
                        View fundoParabens = findViewById(R.id.fundoParabens);

                        TextParabensCriterio.setVisibility(View.GONE);
                        fundoParabens.setVisibility(View.GONE);
                    }
                }
            }
        });

        if(cardSelecionado != null){
            textView10 = dialog.findViewById(R.id.NomeCardFiltrar);
            textView10.setText(cardSelecionado);
        }

        if(quantFaltasSelecionada != null){
            textView11 = dialog.findViewById(R.id.textView11);
            textView11.setText(quantFaltasSelecionada);
        }

        if(materiaSelecionada != null){
            textView12 = dialog.findViewById(R.id.textView12);
            textView12.setText(materiaSelecionada);
        }


        if (ViewCardFilter != null) {
            ViewCardFilter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogFiltrarCard();
                }
            });
        }

        if (ViewQuantFilter != null) {
            ViewQuantFilter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogFiltrarQuantFaltas();
                }
            });
        }

        if (ViewMaterias != null) {
            ViewMaterias.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogFiltrarMaterias();
                }
            });
        }

        // Exibir o Dialog
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent); // Define o fundo transparente
        dialog.show();

    }

    private void DialogFiltrarCard(){
        // Criar o segundo Dialog
        Dialog segundoDialog = new Dialog(ListaFaltas.this, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        segundoDialog.setContentView(getLayoutInflater().inflate(R.layout.dialog_card_filter_radio, null));

        RadioGroup radioGroupCard = segundoDialog.findViewById(R.id.radioGroupDiaS);

        RadioButton radioButtonManual = new RadioButton(getApplicationContext());
        radioButtonManual.setText("Todos");
        radioGroupCard.addView(radioButtonManual);

        List<String>listaDeAgrupamentos = obterListaDeAgrupamentosDoBanco();
        // Adicione opções do Card dinamicamente
        if (!listaDeAgrupamentos.isEmpty()) {
            for (String agrupamento : listaDeAgrupamentos) {
                RadioButton radioButton = new RadioButton(getApplicationContext());
                radioButton.setText(agrupamento);
                radioGroupCard.addView(radioButton);
                textView14 = segundoDialog.findViewById(R.id.TextNPF);
                textView14.setVisibility(View.GONE);
            }
        } else{
            radioGroupCard.setVisibility(View.VISIBLE);
            textView14 = segundoDialog.findViewById(R.id.TextFiltrarDiaS);
            textView14.setVisibility(View.VISIBLE);

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
                    cardSelecionado = selectedCard.getText().toString();
                    textView10.setText(cardSelecionado);

                    materiaSelecionada = "Todos";
                    textView12.setText("Todos");
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

    private void DialogFiltrarQuantFaltas(){
        // Criar o segundo Dialog
        Dialog segundoDialog = new Dialog(ListaFaltas.this, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        segundoDialog.setContentView(getLayoutInflater().inflate(R.layout.dialog_quantfaltas_filter_radio, null));

        RadioGroup radioGroupQuantF = segundoDialog.findViewById(R.id.radioGroupDiaS);

        RadioButton radioButtonManual1 = new RadioButton(getApplicationContext());
        radioButtonManual1.setText("Todos");
        radioGroupQuantF.addView(radioButtonManual1);


        // Adicione opções de Quantidade de Faltas dinamicamente com base na quantidade máxima
        int quantidadeMaximaFaltas = obterQuantidadeMaximaFaltas(); // Implemente essa função
        if (quantidadeMaximaFaltas > 0) {
            for (int i = 1; i <= quantidadeMaximaFaltas; i++) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText("+" + i);
            radioGroupQuantF.addView(radioButton);
        }
    } else {
        // Se não houver opções, desabilitar o RadioGroup e exibir o TextView
        radioGroupQuantF.setVisibility(View.VISIBLE);
        textView14 = segundoDialog.findViewById(R.id.textView10);
        textView14.setVisibility(View.VISIBLE);
    }

        int limiteQuantF = 4; // Altere conforme necessário
        if (radioGroupQuantF.getChildCount() > limiteQuantF) {
            ScrollView scrollViewCard = segundoDialog.findViewById(R.id.scrollViewCard);
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) scrollViewCard.getLayoutParams();
            layoutParams.height = getResources().getDimensionPixelSize(R.dimen.radio_group_max_height);
            scrollViewCard.setLayoutParams(layoutParams);
        }



        ImageView btnCloseFiltCard = segundoDialog.findViewById(R.id.imageView10);
        btnCloseFiltCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selectedFaltasId = radioGroupQuantF.getCheckedRadioButtonId();
                if (selectedFaltasId != -1) {
                    RadioButton selectQuantF = segundoDialog.findViewById(selectedFaltasId);
                    quantFaltasSelecionada = selectQuantF.getText().toString();
                    textView11.setText(quantFaltasSelecionada);
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


    private void DialogFiltrarMaterias(){
        // Criar o segundo Dialog
        Dialog segundoDialog = new Dialog(ListaFaltas.this, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        segundoDialog.setContentView(getLayoutInflater().inflate(R.layout.dialog_materias_filter_radio, null));

        RadioGroup radioGroupMaterias = segundoDialog.findViewById(R.id.radioGroupDiaS);

        RadioButton radioButtonManual1 = new RadioButton(getApplicationContext());
        radioButtonManual1.setText("Todos");
        radioGroupMaterias.addView(radioButtonManual1);

        List<String> nomesDasMaterias = obterNomesDasMateriasDoBanco();
        if (!nomesDasMaterias.isEmpty()) {
            // Adicionar botões dinamicamente
            for (String nomeMateria : nomesDasMaterias) {
                RadioButton radioButton = new RadioButton(getApplicationContext());
                radioButton.setText(nomeMateria);
                radioGroupMaterias.addView(radioButton);
            }
        } else {
            // Se não houver opções, desabilitar o RadioGroup e exibir o TextView
            radioGroupMaterias.setVisibility(View.VISIBLE);
            textView14 = segundoDialog.findViewById(R.id.textView15);
            textView14.setVisibility(View.VISIBLE);
        }

        int limiteQuantMaterias = 4; // Altere conforme necessário
        if (radioGroupMaterias.getChildCount() > limiteQuantMaterias) {
            ScrollView scrollViewCard = segundoDialog.findViewById(R.id.scrollViewCard);
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) scrollViewCard.getLayoutParams();
            layoutParams.height = getResources().getDimensionPixelSize(R.dimen.radio_group_max_height);
            scrollViewCard.setLayoutParams(layoutParams);
        }


        ImageView btnCloseFiltCard = segundoDialog.findViewById(R.id.imageView10);
        btnCloseFiltCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selectedFaltasId = radioGroupMaterias.getCheckedRadioButtonId();
                if (selectedFaltasId != -1) {
                    RadioButton selectQuantF = segundoDialog.findViewById(selectedFaltasId);
                    materiaSelecionada = selectQuantF.getText().toString();
                    textView12.setText(materiaSelecionada);
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


}

