package com.example.appjavavidaacademica;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.ConcatAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ListaFaltas extends AppCompatActivity {

    private RecyclerView recyclerView;

    private SQLiteDatabase bancoDados;

    private DatabaseHelper dbHelper;

    private ImageView btnClose;

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
    }

    public List<Faltas> obterFaltas(Context context) {
        List<Faltas> listaDeFaltas = new ArrayList<>();

        try {
            dbHelper = new DatabaseHelper(context);
            bancoDados = dbHelper.getReadableDatabase();

            // Colunas que você deseja obter
            String[] colunas = {"data", "id_materia", "quantidade", "motivo", "id_faltas"};

            Cursor cursor = bancoDados.query("faltas", colunas, null, null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    // Obter dados da tabela faltas
                    int id_faltas = cursor.getInt(cursor.getColumnIndex("id_faltas"));
                    String data = cursor.getString(cursor.getColumnIndex("data"));
                    int idMateria = cursor.getInt(cursor.getColumnIndex("id_materia"));
                    int quantidade = cursor.getInt(cursor.getColumnIndex("quantidade"));
                    String motivo = cursor.getString(cursor.getColumnIndex("motivo"));
                    String nomeMateria = obterNomeMateria(idMateria);

                    // Criar objeto Falta e adicionar à lista
                    Faltas falta = new Faltas(data, idMateria, quantidade, motivo,nomeMateria);
                    listaDeFaltas.add(falta);

                    // Adicionar um Log para imprimir os valores
                    Log.d("TAG", "id_faltas: " + id_faltas + ", data: " + data + ", idMateria: " + idMateria + ", quantidade: " + quantidade + ", motivo: " + motivo);

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


}

