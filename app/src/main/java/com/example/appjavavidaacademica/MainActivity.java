        package com.example.appjavavidaacademica;

        import androidx.appcompat.app.AppCompatActivity;
        import androidx.cardview.widget.CardView;
        import androidx.constraintlayout.widget.ConstraintLayout;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;

        import android.app.AlertDialog;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.content.pm.ActivityInfo;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.graphics.Color;
        import android.os.Bundle;
        import android.content.ContentValues;
        import android.util.Log;
        import android.view.Gravity;
        import android.view.View;
        import android.view.WindowManager;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.graphics.drawable.GradientDrawable;
        import android.widget.ListView;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.google.android.material.textfield.TextInputEditText;

        import java.util.ArrayList;
        import java.util.HashSet;
        import java.util.List;
        import java.util.Set;

        public class MainActivity extends AppCompatActivity {

            private RecyclerView recyclerViewMaterias;
            private AdicionarMateriaAdapter adicionarMateriaAdapter;
            private List<Materia> listaDeMaterias = new ArrayList<>();

            private List<String> valoresNomeMateria = new ArrayList<>();

            private List<String> valoresDataSemana = new ArrayList<>();

            private List<String> valoresQuantAulas = new ArrayList<>();


            private View adicioneAlgoScreen;
            private View colorPreview;
            private int selectedFontColorIndex = 0;

            private int selectedButtonColorIndex = 0;

            private int selectedFundoColorIndex = 0;

            private String[] defaultHexColors = {
                    "#3FC264",   // Verde
                    "#3FD2CD",   // Azul
                    "#FFFFFF",   // Branco
                    "#FF7FFF"    // Magenta
            };


            private String[] defaultFontHexColors = {
                    "#000000",
                    "#3FC264",   // Verde
                    "#3FD2CD",   // Azul
                    "#FFFFFF",   // Branco
                    "#FF7FFF",    // Magenta
            };

            private String[] defaultButtonHexColors = {
                    "#FFFFFF",   // Branco
                    "#D9D9D9",
                    "#3FD2CD",   // Azul
                    "#FF7FFF"   // Magenta

            };



            private View barraUm;

            private View barraDois;


            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                View QuadradoAdicioneAlgo = findViewById(R.id.Fundo_Adicione_Algo);
                TextView AdicioneAlgoText = findViewById(R.id.AdicioneAlgo);


                recyclerViewMaterias = findViewById(R.id.recyclerViewMaterias);
                recyclerViewMaterias.setLayoutManager(new LinearLayoutManager(this));
                adicionarMateriaAdapter = new AdicionarMateriaAdapter(listaDeMaterias);
                recyclerViewMaterias.setAdapter(adicionarMateriaAdapter);




                barraUm = findViewById(R.id.barraUm);
                barraDois = findViewById(R.id.barraDois);




                if (existemRegistrosAgrupamento())
                {
                    // Se houver registros, listar e exibir o ListView
                    listarAgrupamento();
                    QuadradoAdicioneAlgo.setVisibility(View.INVISIBLE);
                    AdicioneAlgoText.setVisibility(View.INVISIBLE);
                }
                adicioneAlgoScreen = findViewById(R.id.AdicioneAlgoScreen);

                ImageView btnAdd = findViewById(R.id.Add);
                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showAdicioneAlgoScreenWithAnimation();
                        btnAdd.setVisibility(View.GONE);
                    }
                });


                Button btnSalvar = findViewById(R.id.btnSalvar);
                btnSalvar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        salvarAgrupamento();

                    }
                });


                ImageView imageViewClose = findViewById(R.id.imageView4);
                imageViewClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        btnAdd.setVisibility(View.VISIBLE);

                    }
                });

                colorPreview = findViewById(R.id.colorPreview);

                View btnChangeTextColor = findViewById(R.id.TextoColor);
                btnChangeTextColor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showColorSelectionDialogTexto();
                    }
                });

                View btnChangeButtonColor = findViewById(R.id.Botoes);
                btnChangeButtonColor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showColorSelectionDialogButton();
                    }
                });

                View btnSelectColorFundo = findViewById(R.id.FundoColor);
                btnSelectColorFundo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showColorSelectionDialog();
                    }
                });
                updateColorPreview();



                Button btnAdicionarConteudo = findViewById(R.id.btnAdicionarConteudo);
                btnAdicionarConteudo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Criar uma nova instância de Materia
                        Materia novaMateria = new Materia("", "", 0, 0,""); // Valores padrão vazios

                        int posicaoInserir = listaDeMaterias.size(); // posição da última existente

                        // Adicionar a nova matéria à lista
                        listaDeMaterias.add(posicaoInserir, novaMateria);

                        // Notificar o adaptador sobre a alteração nos dados
                        adicionarMateriaAdapter.notifyItemInserted(posicaoInserir);

                    }
                });
                Button btnAnterior = findViewById(R.id.btnAnterior);

                Button btnProximo = findViewById(R.id.btnProximo);
                btnProximo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mostrarParteDois();


                        ConstraintLayout.LayoutParams layoutParamsSalvar = (ConstraintLayout.LayoutParams) btnSalvar.getLayoutParams();
                        layoutParamsSalvar.topToBottom = R.id.view3;  // Defina o ID correto
                        layoutParamsSalvar.topMargin = (int) getResources().getDimension(R.dimen.margin_top);
                        btnSalvar.setLayoutParams(layoutParamsSalvar);

                        ConstraintLayout.LayoutParams layoutParamsProximo = (ConstraintLayout.LayoutParams) btnProximo.getLayoutParams();
                        layoutParamsProximo.topToBottom = R.id.view3;  // Defina o ID correto
                        layoutParamsProximo.topMargin = (int) getResources().getDimension(R.dimen.margin_top);
                        btnProximo.setLayoutParams(layoutParamsProximo);

                        ConstraintLayout.LayoutParams layoutParamsAnterior = (ConstraintLayout.LayoutParams) btnAnterior.getLayoutParams();
                        layoutParamsAnterior.topToBottom = R.id.view3;  // Defina o ID correto
                        layoutParamsAnterior.topMargin = (int) getResources().getDimension(R.dimen.margin_top);
                        layoutParamsAnterior.endToEnd = R.id.btnSalvar;  // Defina o ID correto
                        layoutParamsAnterior.horizontalBias = 1.0f;
                        btnAnterior.setLayoutParams(layoutParamsAnterior);

                        barraUm.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        barraUm.setAlpha(0.6f);

                        // Configurar a cor e a opacidade para a view4
                        barraDois.setBackgroundColor(Color.parseColor("#120272"));
                        barraDois.setAlpha(1.0f);

                    }
                });

                btnAnterior.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mostrarParteUm();
                        ConstraintLayout.LayoutParams layoutParamsSalvar = (ConstraintLayout.LayoutParams) btnSalvar.getLayoutParams();
                        layoutParamsSalvar.topToBottom = R.id.colorPreview;  // Defina o ID correto
                        layoutParamsSalvar.topMargin = (int) getResources().getDimension(R.dimen.margin_top);
                        btnSalvar.setLayoutParams(layoutParamsSalvar);

                        ConstraintLayout.LayoutParams layoutParamsProximo = (ConstraintLayout.LayoutParams) btnProximo.getLayoutParams();
                        layoutParamsProximo.topToBottom = R.id.colorPreview;  // Defina o ID correto
                        layoutParamsProximo.topMargin = (int) getResources().getDimension(R.dimen.margin_top);
                        btnProximo.setLayoutParams(layoutParamsProximo);

                        ConstraintLayout.LayoutParams layoutParamsAnterior = (ConstraintLayout.LayoutParams) btnAnterior.getLayoutParams();
                        layoutParamsAnterior.topToBottom = R.id.colorPreview;  // Defina o ID correto
                        layoutParamsAnterior.topMargin = (int) getResources().getDimension(R.dimen.margin_top);
                        layoutParamsAnterior.endToEnd = R.id.btnSalvar;  // Defina o ID correto
                        layoutParamsAnterior.horizontalBias = 1.0f;
                        btnAnterior.setLayoutParams(layoutParamsAnterior);

                        barraDois.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        barraDois.setAlpha(0.6f);

                        // Configurar a cor e a opacidade para a view4
                        barraUm.setBackgroundColor(Color.parseColor("#120272"));
                        barraUm.setAlpha(1.0f);


                    }
                });

            }

            private void showAdicioneAlgoScreenWithAnimation() {


                adicioneAlgoScreen.setVisibility(View.VISIBLE);
                adicioneAlgoScreen.setTranslationY(adicioneAlgoScreen.getHeight());

                adicioneAlgoScreen.animate()
                        .translationY(0)
                        .setDuration(300)
                        .start();
            }


            private void showColorSelectionDialogButton() {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                ColorAdapter adapter = new ColorAdapter(this, defaultButtonHexColors);

                builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedButtonColorIndex = which;
                        updateButtonColor();
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = builder.create();

                // Define as dimensões desejadas
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                layoutParams.copyFrom(alertDialog.getWindow().getAttributes());
                layoutParams.width = getResources().getDimensionPixelSize(R.dimen.your_dialog_width); // Substitua com a largura desejada
                layoutParams.height = getResources().getDimensionPixelSize(R.dimen.your_dialog_height); // Substitua com a altura desejada

                alertDialog.show();
                alertDialog.getWindow().setAttributes(layoutParams);

                View textColorButton = findViewById(R.id.Botoes); // Substitua com o ID real do botão TextoColor
                if (textColorButton != null) {
                    int[] location = new int[2];
                    textColorButton.getLocationOnScreen(location);
                    layoutParams.gravity = Gravity.TOP | Gravity.START;
                    layoutParams.x = location[0] + (textColorButton.getWidth() / 2) - (layoutParams.width / 2);
                    layoutParams.y = location[1] - layoutParams.height - getResources().getDimensionPixelSize(R.dimen.additional_margin); // Ajuste aqui
                    alertDialog.getWindow().setAttributes(layoutParams);
                }
            }


            private void showColorSelectionDialogTexto() {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                ColorAdapter adapter = new ColorAdapter(this, defaultFontHexColors);

                builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedFontColorIndex = which;
                        updateColorTexto();
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = builder.create();

                // Define as dimensões desejadas
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                layoutParams.copyFrom(alertDialog.getWindow().getAttributes());
                layoutParams.width = getResources().getDimensionPixelSize(R.dimen.your_dialog_width); // Substitua com a largura desejada
                layoutParams.height = getResources().getDimensionPixelSize(R.dimen.your_dialog_height); // Substitua com a altura desejada

                alertDialog.show();
                alertDialog.getWindow().setAttributes(layoutParams);

                View textColorButton = findViewById(R.id.TextoColor); // Substitua com o ID real do botão TextoColor
                if (textColorButton != null) {
                    int[] location = new int[2];
                    textColorButton.getLocationOnScreen(location);
                    layoutParams.gravity = Gravity.TOP | Gravity.START;
                    layoutParams.x = location[0] + (textColorButton.getWidth() / 2) - (layoutParams.width / 2);
                    layoutParams.y = location[1] - layoutParams.height - getResources().getDimensionPixelSize(R.dimen.additional_margin); // Ajuste aqui
                    alertDialog.getWindow().setAttributes(layoutParams);
                }
            }

            private void updateColorTexto() {
                String corFontHex = defaultFontHexColors[selectedFontColorIndex];

                // Atualizar a cor do texto em NomeCard
                TextInputEditText nomeCardEditText = findViewById(R.id.NomeCard);
                nomeCardEditText.setTextColor(Color.parseColor(corFontHex));
                nomeCardEditText.setHintTextColor(Color.parseColor(corFontHex));

                // Atualizar a cor do texto em textInputEditText
                TextInputEditText textInputEditText = findViewById(R.id.textInputEditText);
                textInputEditText.setTextColor(Color.parseColor(corFontHex));
                textInputEditText.setTextColor(Color.parseColor(corFontHex));
                textInputEditText.setHintTextColor(Color.parseColor(corFontHex));

            }


            private void showColorSelectionDialog() {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                ColorAdapter adapter = new ColorAdapter(this, defaultHexColors);

                builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedFundoColorIndex = which;
                        updateColorPreview();
                        dialog.dismiss();
                    }
                });


                AlertDialog alertDialog = builder.create();

                // Define as dimensões desejadas
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                layoutParams.copyFrom(alertDialog.getWindow().getAttributes());
                layoutParams.width = getResources().getDimensionPixelSize(R.dimen.your_dialog_width); // Substitua com a largura desejada
                layoutParams.height = getResources().getDimensionPixelSize(R.dimen.your_dialog_height); // Substitua com a altura desejada

                alertDialog.show();
                alertDialog.getWindow().setAttributes(layoutParams);

                View textColorButton = findViewById(R.id.FundoColor); // Substitua com o ID real do botão TextoColor
                if (textColorButton != null) {
                    int[] location = new int[2];
                    textColorButton.getLocationOnScreen(location);
                    layoutParams.gravity = Gravity.TOP | Gravity.START;
                    layoutParams.x = location[0] + (textColorButton.getWidth() / 2) - (layoutParams.width / 2);
                    layoutParams.y = location[1] - layoutParams.height - getResources().getDimensionPixelSize(R.dimen.additional_margin); // Ajuste aqui
                    alertDialog.getWindow().setAttributes(layoutParams);
                }
            }

            private void updateButtonColor() {
                String corHex = defaultButtonHexColors[selectedButtonColorIndex];

                // Criar um GradientDrawable
                GradientDrawable drawable = new GradientDrawable();
                drawable.setShape(GradientDrawable.RECTANGLE);
                drawable.setCornerRadius(getResources().getDimension(R.dimen.radius)); // Ajuste conforme necessário

                // Definir a cor do Drawable
                drawable.setColor(Color.parseColor(corHex));

                // Aplicar o Drawable ao View desejado
                View btnEdit = findViewById(R.id.btnEdit1); // Substitua pelo ID correto da sua View
                View btnDetail = findViewById(R.id.btnDetails1); // Substitua pelo ID correto da sua View
                View btnDelete = findViewById(R.id.btnDelete1); // Substitua pelo ID correto da sua View

                btnEdit.setBackground(drawable);
                btnDetail.setBackground(drawable);
                btnDelete.setBackground(drawable);
            }



            private void updateColorPreview() {
                String corFundoHex = defaultHexColors[selectedFundoColorIndex];
                GradientDrawable gradientDrawable = (GradientDrawable) colorPreview.getBackground();
                gradientDrawable.setColor(Color.parseColor(corFundoHex));
            }


            private int inserirAgrupamento(String nomeAgrupamento, String categoria, String corFundoHex, String corBotoesHex, String corTextoHex) {
                View QuadradoAdicioneAlgo = findViewById(R.id.Fundo_Adicione_Algo);
                TextView AdicioneAlgoText = findViewById(R.id.AdicioneAlgo);
                int idAgrupamento = -1; // Valor padrão caso a inserção falhe

                try {
                    DatabaseHelper dbHelper = new DatabaseHelper(this);
                    SQLiteDatabase bancoDados = dbHelper.getWritableDatabase();

                    ContentValues values = new ContentValues();
                    values.put("nome_agrupamento", nomeAgrupamento);
                    values.put("categoria", categoria);
                    values.put("corFundoHex", corFundoHex);
                    values.put("corBotoesHex", corBotoesHex);
                    values.put("corTextoHex", corTextoHex);

                    idAgrupamento = (int) bancoDados.insert("agrupamento", null, values);


                    QuadradoAdicioneAlgo.setVisibility(View.INVISIBLE);
                    AdicioneAlgoText.setVisibility(View.INVISIBLE);
                    bancoDados.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("TAG", "Erro ao inserir dados na tabela agrupamento: " + e.getMessage());
                }

                return idAgrupamento;
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

                            if (nomeAgrupamentoIndex >= 0 && categoriaIndex >= 0 && corFundoHexIndex >= 0 && idAgrupamentoIndex >= 0 && corTextoHexIndex >= 0 && corButtonHexIndex >= 0)  {
                                String nomeAgrupamento = cursor.getString(nomeAgrupamentoIndex);
                                String categoria = cursor.getString(categoriaIndex);
                                String corFundoHex = cursor.getString(corFundoHexIndex);
                                String corBotoesHex = cursor.getString(corButtonHexIndex);
                                String corTextoHex = cursor.getString(corTextoHexIndex);

                                int id_agrupamento = cursor.getInt(idAgrupamentoIndex);

                                Agrupamento agrupamento = new Agrupamento(nomeAgrupamento, categoria, corFundoHex,id_agrupamento, corTextoHex, corBotoesHex);
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

            public void salvarAgrupamento() {
                // Obter os valores dos campos
                valoresNomeMateria.clear();
                valoresDataSemana.clear();
                valoresQuantAulas.clear();

                String nomeAgrupamento = ((TextInputEditText) findViewById(R.id.NomeCard)).getText().toString();
                String categoria = ((TextInputEditText) findViewById(R.id.textInputEditText)).getText().toString();
                String corFundoHex = defaultHexColors[selectedFundoColorIndex];
                String corBotoesHex = defaultButtonHexColors[selectedButtonColorIndex];
                String corTextoHex = defaultFontHexColors[selectedFontColorIndex];


                if (nomeAgrupamento.trim().isEmpty() || categoria.trim().isEmpty()) {
                    // Mostrar mensagem de erro (por exemplo, usando Toast)
                    Toast.makeText(this, "Preencha todos os campos obrigatórios", Toast.LENGTH_SHORT).show();
                    return;
                }

                Set<TextInputEditText> uniqueNomeMateriaEditTexts = new HashSet<>(adicionarMateriaAdapter.getNomeMateriaEditTexts());
                Set<TextInputEditText> uniqueDataSemanaEditTexts = new HashSet<>(adicionarMateriaAdapter.getDataSemanaEditTexts());
                Set<TextInputEditText> uniqueQuantAulasEditTexts = new HashSet<>(adicionarMateriaAdapter.getQuantAulasEditTexts());


                int idAgrupamento = inserirAgrupamento(nomeAgrupamento, categoria, corFundoHex, corBotoesHex, corTextoHex);

                for (TextInputEditText nomeMateriaEditText : uniqueNomeMateriaEditTexts) {
                    String nomeMateria = nomeMateriaEditText.getText().toString();
                    valoresNomeMateria.add(nomeMateria);
                }

                for (TextInputEditText dataSemanaEditText : uniqueDataSemanaEditTexts) {
                    String dataSemana = dataSemanaEditText.getText().toString();
                    valoresDataSemana.add(dataSemana);
                }

                for (TextInputEditText quantAulasEditText : uniqueQuantAulasEditTexts) {
                    String quantAulas = quantAulasEditText.getText().toString();
                    valoresQuantAulas.add(quantAulas);
                }

                inserirMaterias(valoresNomeMateria, valoresDataSemana, idAgrupamento,valoresQuantAulas);

                // Atualizar a lista de agrupamentos
                    listarAgrupamento();

                    limparCampos();

                    // Fechar a tela de adicionar algo
                    adicioneAlgoScreen.setVisibility(View.GONE);

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

            private void limparCampos() {
                // Limpe os campos de nome e categoria (ou faça o que for apropriado)
                ((TextInputEditText) findViewById(R.id.NomeCard)).setText("");
                ((TextInputEditText) findViewById(R.id.textInputEditText)).setText("");
                adicionarMateriaAdapter.limparMaterias();


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

            private void inserirMaterias(List<String> valoresNomeMateria, List<String> valoresDataSemana, int idAgrupamento, List<String>valoresQuantAulas) {
                try {
                    DatabaseHelper dbHelper = new DatabaseHelper(this);
                    SQLiteDatabase bancoDados = dbHelper.getWritableDatabase();

                    for (int i = 0; i < valoresNomeMateria.size(); i++) {
                        String nomeMateria = valoresNomeMateria.get(i);
                        String dataSemana = valoresDataSemana.get(i);
                        String quantAula = valoresQuantAulas.get(i);

                        ContentValues valuesMateria = new ContentValues();
                        valuesMateria.put("nome_materia", nomeMateria);
                        valuesMateria.put("dia_semana", dataSemana);
                        valuesMateria.put("id_agrupamento", idAgrupamento);
                        valuesMateria.put("quantAulas", quantAula);

                        Log.d("TAG","NomeMateriaInsert: " + nomeMateria + " DataSemanaInsert: " + dataSemana + " QuantAula: " + quantAula);


                        bancoDados.insert("materias", null, valuesMateria);
                    }

                    bancoDados.close();

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("TAG", "Erro ao inserir dados na tabela materias: " + e.getMessage());
                }
            }

            private void mostrarParteUm() {


                View TextoColor = findViewById(R.id.TextoColor);
                ImageView textFundo = findViewById(R.id.TextFundo);
                View FundroCriarCard = findViewById(R.id.FundroCriarCard);
                TextView TextCriarCard = findViewById(R.id.TextCriarCard);
                ImageView imageView4 = findViewById(R.id.imageView4);
                CardView SombraCard = findViewById(R.id.SombraCard);
                View colorPreview2 = findViewById(R.id.colorPreview);
                TextInputEditText NomeCard = findViewById(R.id.NomeCard);
                TextInputEditText textInputEditText = findViewById(R.id.textInputEditText);
                ImageView imageView2 = findViewById(R.id.imageView2);
                Button btnSalvar2 = findViewById(R.id.btnSalvar);
                View btnDelete1 = findViewById(R.id.btnDelete1);
                ImageView lixeiraIcon2 = findViewById(R.id.lixeiraIcon322);
                View btnEdit1 = findViewById(R.id.btnEdit1);
                ImageView EditaIcon1 = findViewById(R.id.EditaIcon1);
                View btnDetails1 = findViewById(R.id.btnDetails1);
                ImageView DetalhesIcon1 = findViewById(R.id.DetalhesIcon1);
                TextView TextPersonalizarCores = findViewById(R.id.TextPersonalizarCores);
                View Botoes = findViewById(R.id.Botoes);
                View FundoColor = findViewById(R.id.FundoColor);
                ImageView textBotao = findViewById(R.id.textBotao);
                ImageView TextFundo = findViewById(R.id.textFundo);
                TextView MateriasText = findViewById(R.id.MateriasText);
                View SombraFaixa = findViewById(R.id.SombraFaixa);
                ImageView view2 = findViewById(R.id.view2);
                RecyclerView recyclerViewMaterias1 = findViewById(R.id.recyclerViewMaterias);
                ImageView view3 = findViewById(R.id.view3);
                Button btnAdicionarConteudo2 = findViewById(R.id.btnAdicionarConteudo);



                TextoColor.setVisibility(View.VISIBLE);
                textFundo.setVisibility(View.VISIBLE);
                colorPreview2.setVisibility(View.VISIBLE);
                NomeCard.setVisibility(View.VISIBLE);
                textInputEditText.setVisibility(View.VISIBLE);
                btnSalvar2.setVisibility(View.VISIBLE);
                lixeiraIcon2.setVisibility(View.VISIBLE);
                btnDelete1.setVisibility(View.VISIBLE);
                btnEdit1.setVisibility(View.VISIBLE);
                EditaIcon1.setVisibility(View.VISIBLE);
                btnDetails1.setVisibility(View.VISIBLE);
                DetalhesIcon1.setVisibility(View.VISIBLE);
                TextPersonalizarCores.setVisibility(View.VISIBLE);
                Botoes.setVisibility(View.VISIBLE);
                FundoColor.setVisibility(View.VISIBLE);
                textBotao.setVisibility(View.VISIBLE);
                TextFundo.setVisibility(View.VISIBLE);


                MateriasText.setVisibility(View.GONE);
                SombraFaixa.setVisibility(View.GONE);
                view2.setVisibility(View.GONE);
                recyclerViewMaterias1.setVisibility(View.GONE);
                view3.setVisibility(View.GONE);
                btnAdicionarConteudo2.setVisibility(View.GONE);

            }

            private void mostrarParteDois() {
                View TextoColor = findViewById(R.id.TextoColor);
                ImageView textFundo = findViewById(R.id.TextFundo);
                View FundroCriarCard = findViewById(R.id.FundroCriarCard);
                TextView TextCriarCard = findViewById(R.id.TextCriarCard);
                ImageView imageView4 = findViewById(R.id.imageView4);
                CardView SombraCard = findViewById(R.id.SombraCard);
                View colorPreview2 = findViewById(R.id.colorPreview);
                TextInputEditText NomeCard = findViewById(R.id.NomeCard);
                TextInputEditText textInputEditText = findViewById(R.id.textInputEditText);
                ImageView imageView2 = findViewById(R.id.imageView2);
                Button btnSalvar2 = findViewById(R.id.btnSalvar);
                View btnDelete1 = findViewById(R.id.btnDelete1);
                ImageView lixeiraIcon2 = findViewById(R.id.lixeiraIcon322);
                View btnEdit1 = findViewById(R.id.btnEdit1);
                ImageView EditaIcon1 = findViewById(R.id.EditaIcon1);
                View btnDetails1 = findViewById(R.id.btnDetails1);
                ImageView DetalhesIcon1 = findViewById(R.id.DetalhesIcon1);
                TextView TextPersonalizarCores = findViewById(R.id.TextPersonalizarCores);
                View Botoes = findViewById(R.id.Botoes);
                View FundoColor = findViewById(R.id.FundoColor);
                ImageView textBotao = findViewById(R.id.textBotao);
                ImageView TextFundo = findViewById(R.id.textFundo);
                TextView MateriasText = findViewById(R.id.MateriasText);
                View SombraFaixa = findViewById(R.id.SombraFaixa);
                ImageView view2 = findViewById(R.id.view2);
                RecyclerView recyclerViewMaterias1 = findViewById(R.id.recyclerViewMaterias);
                ImageView view3 = findViewById(R.id.view3);
                Button btnAdicionarConteudo2 = findViewById(R.id.btnAdicionarConteudo);


                MateriasText.setVisibility(View.VISIBLE);
                SombraFaixa.setVisibility(View.VISIBLE);
                view2.setVisibility(View.VISIBLE);
                recyclerViewMaterias1.setVisibility(View.VISIBLE);
                view3.setVisibility(View.VISIBLE);
                btnAdicionarConteudo2.setVisibility(View.VISIBLE);
                btnSalvar2.setVisibility(View.VISIBLE);


                TextoColor.setVisibility(View.GONE);
                textFundo.setVisibility(View.GONE);
                colorPreview2.setVisibility(View.GONE);
                NomeCard.setVisibility(View.GONE);
                textInputEditText.setVisibility(View.GONE);
                lixeiraIcon2.setVisibility(View.GONE);
                btnEdit1.setVisibility(View.GONE);
                EditaIcon1.setVisibility(View.GONE);
                btnDetails1.setVisibility(View.GONE);
                DetalhesIcon1.setVisibility(View.GONE);
                TextPersonalizarCores.setVisibility(View.GONE);
                Botoes.setVisibility(View.GONE);
                FundoColor.setVisibility(View.GONE);
                textBotao.setVisibility(View.GONE);
                TextFundo.setVisibility(View.GONE);
                btnDelete1.setVisibility(View.GONE);


            }

        }
