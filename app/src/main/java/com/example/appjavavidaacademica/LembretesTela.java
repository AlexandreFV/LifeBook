package com.example.appjavavidaacademica;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class LembretesTela extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lembretes_layout);

        // Obter a data e o mês atuais
        Calendar calendar = Calendar.getInstance();
        int diaAtual = calendar.get(Calendar.DAY_OF_MONTH);
        int mesAtual = calendar.get(Calendar.MONTH) + 1; // Adicionar 1 porque os meses são baseados em zero

        // Atualizar o TextView com a data e o mês atuais
        TextView dataAtualTextView = findViewById(R.id.dataAtualTextView);
        dataAtualTextView.setText("Data Atual: " + diaAtual + "/" + mesAtual);

        layoutInferiorBotoes barraInferior = new layoutInferiorBotoes(this, findViewById(R.id.includeDesempenhoInf), findViewById(R.id.AdicioneAlgoScreen));

    }

}
