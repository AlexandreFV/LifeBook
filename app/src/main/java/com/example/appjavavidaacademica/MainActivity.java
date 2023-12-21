        package com.example.appjavavidaacademica;

        import androidx.appcompat.app.AppCompatActivity;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;

        import android.app.AlertDialog;
        import android.content.DialogInterface;
        import android.content.Intent;
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
            private MateriaAdapter materiaAdapter;
            private List<Materia> listaDeMaterias = new ArrayList<>();

            private List<String> valoresNomeMateria = new ArrayList<>();

            private List<String> valoresDataSemana = new ArrayList<>();


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

            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);

                View QuadradoAdicioneAlgo = findViewById(R.id.Fundo_Adicione_Algo);
                TextView AdicioneAlgoText = findViewById(R.id.AdicioneAlgo);


                recyclerViewMaterias = findViewById(R.id.recyclerViewMaterias);
                recyclerViewMaterias.setLayoutManager(new LinearLayoutManager(this));
                materiaAdapter = new MateriaAdapter(listaDeMaterias);
                recyclerViewMaterias.setAdapter(materiaAdapter);


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
                        Materia novaMateria = new Materia("", "", 0, 0); // Valores padrão vazios

                        // Adicionar a nova matéria à lista
                        listaDeMaterias.add(novaMateria);

                        // Notificar o adaptador sobre a alteração nos dados
                        materiaAdapter.notifyDataSetChanged();

                        // Rolar até a posição da nova matéria (opcional)
                        int novaPosicao = listaDeMaterias.size() - 1;
                        recyclerViewMaterias.scrollToPosition(novaPosicao);
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

                Set<TextInputEditText> uniqueNomeMateriaEditTexts = new HashSet<>(materiaAdapter.getNomeMateriaEditTexts());
                Set<TextInputEditText> uniqueDataSemanaEditTexts = new HashSet<>(materiaAdapter.getDataSemanaEditTexts());


                int idAgrupamento = inserirAgrupamento(nomeAgrupamento, categoria, corFundoHex, corBotoesHex, corTextoHex);

                for (TextInputEditText nomeMateriaEditText : uniqueNomeMateriaEditTexts) {
                    String nomeMateria = nomeMateriaEditText.getText().toString();
                    valoresNomeMateria.add(nomeMateria);
                }

                for (TextInputEditText dataSemanaEditText : uniqueDataSemanaEditTexts) {
                    String dataSemana = dataSemanaEditText.getText().toString();
                    valoresDataSemana.add(dataSemana);
                }

                inserirMaterias(valoresNomeMateria, valoresDataSemana, idAgrupamento);



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
                ((TextInputEditText) findViewById(R.id.textViewNomeMateria)).setText("");
                ((TextInputEditText) findViewById(R.id.textViewDataMateria)).setText("");

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

            private void inserirMaterias(List<String> valoresNomeMateria, List<String> valoresDataSemana, int idAgrupamento) {
                try {
                    DatabaseHelper dbHelper = new DatabaseHelper(this);
                    SQLiteDatabase bancoDados = dbHelper.getWritableDatabase();

                    for (int i = 0; i < valoresNomeMateria.size(); i++) {
                        String nomeMateria = valoresNomeMateria.get(i);
                        String dataSemana = valoresDataSemana.get(i);


                        ContentValues valuesMateria = new ContentValues();
                        valuesMateria.put("nome_materia", nomeMateria);
                        valuesMateria.put("dia_semana", dataSemana);
                        valuesMateria.put("id_agrupamento", idAgrupamento);

                        Log.d("TAG","NomeMateriaInsert: " + nomeMateria + "DataSemanaInsert: " + dataSemana );


                        bancoDados.insert("materias", null, valuesMateria);
                    }

                    bancoDados.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("TAG", "Erro ao inserir dados na tabela materias: " + e.getMessage());
                }
            }


        }
