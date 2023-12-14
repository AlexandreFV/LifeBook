package com.example.appjavavidaacademica;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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



            textViewNome.setText("Nome: " + nomeAgrupamento);
        textViewCategoria.setText("Categoria: " + categoria);
        textViewCorHex.setText("Cor Hex: " + corFundoHex);

            textViewNome.setTextColor(corTextoInt);
            textViewCategoria.setTextColor(corTextoInt);
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

                if (nomeAgrupamentoIndex >= 0 && categoriaIndex >= 0 && corFundoHexIndex >= 0 && corTextoHexIndex >= 0 && corButtonHexIndex >= 0)  {
                    String nomeAgrupamento = cursor.getString(nomeAgrupamentoIndex);
                    String categoria = cursor.getString(categoriaIndex);
                    String corFundoHex = cursor.getString(corFundoHexIndex);
                    String corBotoesHex = cursor.getString(corButtonHexIndex);
                    String corTextoHex = cursor.getString(corTextoHexIndex);

                    return new Agrupamento(nomeAgrupamento, categoria, corFundoHex,agrupamentoId, corTextoHex, corBotoesHex);
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



}
