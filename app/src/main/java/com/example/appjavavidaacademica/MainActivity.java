        package com.example.appjavavidaacademica;

        import static com.example.appjavavidaacademica.AdicionarTela.salvamentoOcorreu;

        import android.content.pm.ActivityInfo;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.os.Bundle;
        import android.os.Handler;
        import android.util.Log;
        import android.view.View;
        import android.view.animation.TranslateAnimation;
        import android.widget.ListView;
        import android.widget.TextView;

        import androidx.appcompat.app.AppCompatActivity;
        import androidx.constraintlayout.widget.ConstraintLayout;

        import java.util.ArrayList;
        import java.util.List;

        public class MainActivity extends AppCompatActivity {

            private View view3;



            private View adicioneAlgoScreen;


            private ConstraintLayout LayoutMain;

            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                LayoutMain = findViewById(R.id.LayoutMain);

                View QuadradoAdicioneAlgo = findViewById(R.id.Fundo_Adicione_Algo);
                TextView AdicioneAlgoText = findViewById(R.id.AdicioneAlgo);
                adicioneAlgoScreen = findViewById(R.id.AdicioneAlgoScreen);
                layoutInferiorBotoes barraInferior = new layoutInferiorBotoes(this, findViewById(R.id.includeMainInf), findViewById(R.id.AdicioneAlgoScreen));

                if (salvamentoOcorreu == true){
                    exibirMensagemComAnimacao();
                }

                if (existemRegistrosAgrupamento())
                {
                    // Se houver registros, listar e exibir o ListView
                    listarAgrupamento();
                    QuadradoAdicioneAlgo.setVisibility(View.INVISIBLE);
                    AdicioneAlgoText.setVisibility(View.INVISIBLE);
                }
            }





            private boolean existemRegistrosAgrupamento() {
                try {
                    DatabaseHelper dbHelper = new DatabaseHelper(this);
                    SQLiteDatabase bancoDados = dbHelper.getWritableDatabase();
                    Cursor cursor = bancoDados.rawQuery("SELECT COUNT(*) FROM agrupamento", null);

                    if (cursor != null && cursor.moveToFirst()) {
                        int count = cursor.getInt(0);
                        cursor.close();
                        bancoDados.close();
                        return count > 0;
                    }

                    bancoDados.close();
                    return false;
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("TAG", "Erro ao verificar registros na tabela agrupamento: " + e.getMessage());
                    return false;
                }
            }


            private void listarAgrupamento() {
                try {
                    DatabaseHelper dbHelper = new DatabaseHelper(this);
                    SQLiteDatabase bancoDados = dbHelper.getWritableDatabase();
                    Cursor cursor = bancoDados.rawQuery("SELECT * FROM agrupamento", null);

                    List<Agrupamento> agrupamentoList = new ArrayList<>();

                    if (cursor != null && cursor.moveToFirst()) {
                        do {
                            int nomeAgrupamentoIndex = cursor.getColumnIndex("nome_agrupamento");
                            int categoriaIndex = cursor.getColumnIndex("categoria");
                            int corFundoHexIndex = cursor.getColumnIndex("corFundoHex");
                            int idAgrupamentoIndex = cursor.getColumnIndex("id_agrupamento");
                            int corTextoHexIndex = cursor.getColumnIndex("corTextoHex");
                            int corButtonHexIndex = cursor.getColumnIndex("corBotoesHex");
                            int iconEscolhidoIndex = cursor.getColumnIndex("iconEscolhido");

                            if (nomeAgrupamentoIndex >= 0 && categoriaIndex >= 0 && corFundoHexIndex >= 0 && idAgrupamentoIndex >= 0 && corTextoHexIndex >= 0 && corButtonHexIndex >= 0 && iconEscolhidoIndex >=0)  {
                                String nomeAgrupamento = cursor.getString(nomeAgrupamentoIndex);
                                String categoria = cursor.getString(categoriaIndex);
                                String corFundoHex = cursor.getString(corFundoHexIndex);
                                String corBotoesHex = cursor.getString(corButtonHexIndex);
                                String corTextoHex = cursor.getString(corTextoHexIndex);
                                int iconEscolhido = cursor.getInt(iconEscolhidoIndex);

                                int id_agrupamento = cursor.getInt(idAgrupamentoIndex);

                                Agrupamento agrupamento = new Agrupamento(nomeAgrupamento, categoria, corFundoHex,id_agrupamento, corTextoHex, corBotoesHex,iconEscolhido);
                                agrupamentoList.add(agrupamento);
                                bancoDados.close();
                            } else {
                                Log.e("TAG", "Índice de coluna inválido");
                            }
                        } while (cursor.moveToNext());
                    }

                    if (cursor != null) {
                        cursor.close();
                    }


                    // Configurar o adapter
                    AgrupamentoAdapter adapter = new AgrupamentoAdapter(this, agrupamentoList, this);

                    // Obter a referência do ListView
                    ListView listView = findViewById(R.id.listViewAgrupamento);

                    // Definir o adapter no ListView
                    listView.setAdapter(adapter);

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("TAG", "Erro ao listar dados da tabela agrupamento: " + e.getMessage());
                }
            }



            //Usado por outras funções e ou classes ao deletar ou atualizar card
            public void atualizarListaAgrupamento() {
                listarAgrupamento();
                Log.d("TAG", "Foi chamado");
                if (existemRegistrosAgrupamento() == false)
                {
                    // Se houver registros, listar e exibir o ListView
                    View QuadradoAdicioneAlgo = findViewById(R.id.Fundo_Adicione_Algo);
                    TextView AdicioneAlgoText = findViewById(R.id.AdicioneAlgo);
                    QuadradoAdicioneAlgo.setVisibility(View.VISIBLE);
                    AdicioneAlgoText.setVisibility(View.VISIBLE);
                }

            }


            private void exibirMensagemComAnimacao() {
                salvamentoOcorreu = false;
                final View fundoSuccess = findViewById(R.id.FundoSuccess);
                final TextView textAdicionadoSuccess = findViewById(R.id.TextAdicionadoSuccess);
                final View successIcon = findViewById(R.id.SuccessIcon);

                // Configurar a animação de entrada
                TranslateAnimation entradaAnimation = new TranslateAnimation(
                        1000,  // Da posição x 1000dp para fora da tela (à direita)
                        0,     // Para a posição x 0dp (posição inicial)
                        0,
                        0);
                entradaAnimation.setDuration(500);

                fundoSuccess.startAnimation(entradaAnimation);
                textAdicionadoSuccess.startAnimation(entradaAnimation);
                successIcon.startAnimation(entradaAnimation);

                // Tornar as Views visíveis após a animação de entrada
                fundoSuccess.setVisibility(View.VISIBLE);
                textAdicionadoSuccess.setVisibility(View.VISIBLE);
                successIcon.setVisibility(View.VISIBLE);

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
                        fundoSuccess.startAnimation(saidaAnimation);
                        textAdicionadoSuccess.startAnimation(saidaAnimation);
                        successIcon.startAnimation(saidaAnimation);

                        // Definir a visibilidade das Views como GONE após a animação de saída
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                fundoSuccess.setVisibility(View.GONE);
                                textAdicionadoSuccess.setVisibility(View.GONE);
                                successIcon.setVisibility(View.GONE);
                            }
                        }, 500);  // Aguarde 500 milissegundos antes de tornar as Views invisíveis
                    }
                }, 2000);  // Aguarde 2000 milissegundos (2 segundos) antes de iniciar a animação de saída
            }

        }
