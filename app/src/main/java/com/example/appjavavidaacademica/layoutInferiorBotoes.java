package com.example.appjavavidaacademica;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;

public class layoutInferiorBotoes {

    private Context context;
    private View rootView;
    private View adicioneAlgoScreen;
    private ConstraintLayout layoutAtras;

    // Adicione os botões conforme necessário
    private ImageView lembretes;
    private ImageView desempenho;
    private ImageView notificacoes;
    private ImageView configuracoes;
    public static ImageView add;


    public layoutInferiorBotoes(Context context, View rootView, View adicioneAlgoScreen) {
        this.context = context;
        this.rootView = rootView;
        this.adicioneAlgoScreen = adicioneAlgoScreen;
        initializeViews();
        setupListeners();

    }


    private void initializeViews() {
        // Inicialize os botões aqui
        lembretes = rootView.findViewById(R.id.lembretes);
        desempenho = rootView.findViewById(R.id.desempenho);
        notificacoes = rootView.findViewById(R.id.notificacoes);
        configuracoes = rootView.findViewById(R.id.configuracoes);
        add = rootView.findViewById(R.id.add);
        Log.d("TAG","Teste");

    }

    private void setupListeners() {
        // Adicione listeners aos botões aqui
        lembretes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para o botão lembretes
            }
        });

        desempenho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Verificar se já estamos na atividade DesempenhoActivity
                if (!(context instanceof DesempenhoActivity)) {
                    // Se não estiver na DesempenhoActivity, inicie a intent
                    Intent intent = new Intent(context, DesempenhoActivity.class);
                    context.startActivity(intent);
                }
            }
        });

        notificacoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para o botão notificações
            }
        });

        configuracoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para o botão configurações

            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Verificar se já estamos na atividade AdicionarTela
                if (!(context instanceof AdicionarTela)) {
                    // Se não estiver na AdicionarTela, inicie a intent
                    Intent intent = new Intent(context, AdicionarTela.class);
                    context.startActivity(intent);
                }
            }
        });
    }

    private void showAdicioneAlgoScreenWithAnimation() {
        // Torna apenas a tela de adição visível
        adicioneAlgoScreen.setVisibility(View.VISIBLE);
        adicioneAlgoScreen.setTranslationY(adicioneAlgoScreen.getHeight());

        adicioneAlgoScreen.animate()
                .translationY(0)
                .setDuration(300)
                .start();
        add.setVisibility(View.GONE);

    }

}
