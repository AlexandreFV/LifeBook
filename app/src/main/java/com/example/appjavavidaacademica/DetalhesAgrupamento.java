package com.example.appjavavidaacademica;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DetalhesAgrupamento  extends AppCompatActivity {

    private SQLiteDatabase bancoDados;
    private DatabaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalhes_agrupamento);


        dbHelper = new DatabaseHelper(this);

        ImageView btnClose = findViewById(R.id.ImageClose);

        int agrupamentoId = getIntent().getIntExtra("AGRUPO_ID", -1);
        Agrupamento agrupamentoSelecionado = obterAgrupamentoPorId(agrupamentoId);


        RecyclerView recyclerView = findViewById(R.id.recyclerViewMateriasAdicionadas);
        MateriaAdapterAdicionado adapter = new MateriaAdapterAdicionado(obterMateriasPorAgrupamento(agrupamentoId));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setRecyclerView(recyclerView);


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




}
