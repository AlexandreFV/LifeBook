package com.example.appjavavidaacademica;


import android.app.AlertDialog;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ListaFaltas extends AppCompatActivity {

    private RecyclerView recyclerView;

    private SQLiteDatabase bancoDados;

    private DatabaseHelper dbHelper;

    private ImageView btnClose;

    private List<String> listaDeAgrupamentos;

    private View Filtrar;

    private View RemoveFiltro;

    private TextView textRemoveFiltro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_faltas);

        recyclerView = findViewById(R.id.recyclerViewFaltas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



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
            List<String> listaDeAgrupamentos = obterListaDeAgrupamentosDoBanco(); // substitua por sua lógica
            mostrarDialogFiltrar(listaDeAgrupamentos, tableAdapter);
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

        RadioGroup radioGroupCard = dialogView.findViewById(R.id.radioGroupCard);
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
                    int quantidadeFaltas = -1;

                    if (selectedCardId != -1) {
                        RadioButton selectedCard = dialogView.findViewById(selectedCardId);
                        cardSelecionado = selectedCard.getText().toString();
                    }

                    if (selectedFaltasId != -1) {
                        RadioButton selectedFaltas = dialogView.findViewById(selectedFaltasId);
                        quantidadeFaltas = Integer.parseInt(selectedFaltas.getText().toString().substring(1));
                    }

                    // Faça algo com os valores selecionados
                    Log.d("TAG", "Card Selecionado: " + cardSelecionado);

                    // Aplique o filtro com base nos valores selecionados
                    tableAdapter.filtrarDados(cardSelecionado, quantidadeFaltas);
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



}

