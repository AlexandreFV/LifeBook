package com.example.appjavavidaacademica;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AdicionarTela extends AppCompatActivity {

    public static boolean salvamentoOcorreu = false;


    private AdicionarMateriaAdapter adicionarMateriaAdapter;
    private List<Materia> listaDeMaterias = new ArrayList<>();

    private List<String> valoresNomeMateria = new ArrayList<>();

    private List<String> valoresDataSemana = new ArrayList<>();

    private List<String> valoresQuantAulas = new ArrayList<>();

    private View colorPreview;
    private int selectedFontColorIndex = 0;

    private int selectedButtonColorIndex = 0;

    private int selectedFundoColorIndex = 0;

    private int selectIconIndex = 0;


    private String[] defaultHexColors = {
            "#3FC264",   // Verde
            "#3FD2CD",   // Azul
            "#FFFFFF",   // Branco
            "#FF7FFF"    // Magenta
    };


    private Integer[] defaultIcons = {
            R.drawable.livro,
            R.drawable.matematica,
            R.drawable.natureza,
            R.drawable.batimento_cardiaco,
            R.drawable.equipe,
            R.drawable.cofrinho,
            R.drawable.capacete
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


    private List<String> listaDeCategorias = Arrays.asList(
            "Ensino Fundamental",
            "Ensino Médio",
            "Ensino Médio + Técnico",
            "Ensino Técnico",
            "Ensino Superior",
            "Pós-Graduação"
    );

    private View barraUm;

    private View barraDois;

    private ImageView iconImageView;

    private RecyclerView recyclerViewMaterias;

    private boolean FundoSeg;

    private boolean FundoTer;

    private boolean FundoQua;

    private boolean FundoQui;

    private boolean FundoSex;

    private boolean FundoSab;

    private boolean FundoDom;

    private AlertDialog alertDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adicionar_categoria_materia);
        listaDeMaterias.clear();
        iconImageView = findViewById(R.id.IconImage);

        recyclerViewMaterias = findViewById(R.id.recyclerViewMaterias);
        recyclerViewMaterias.setLayoutManager(new LinearLayoutManager(this));
        adicionarMateriaAdapter = new AdicionarMateriaAdapter(listaDeMaterias);
        recyclerViewMaterias.setAdapter(adicionarMateriaAdapter);

        barraUm = findViewById(R.id.barraUm);
        barraDois = findViewById(R.id.barraDois);

        Spinner spinnerCategoria = findViewById(R.id.spinnerCategoria);
        MultiLineArrayAdapter adapter = new MultiLineArrayAdapter(this, android.R.layout.simple_spinner_item, listaDeCategorias);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(adapter);

        layoutInferiorBotoes barraInferior = new layoutInferiorBotoes(this, findViewById(R.id.includeDetalhesInf), findViewById(R.id.AdicioneAlgoScreen));


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
                Intent intent = new Intent(AdicionarTela.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });

        colorPreview = findViewById(R.id.colorPreview);

        ImageView btnIcon = findViewById(R.id.IconImage);
        btnIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showIconSelectionDialog();
            }
        });

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
            public void onClick(View view) {

                if (FundoSeg == true || FundoTer == true || FundoQua == true || FundoQui == true || FundoSex == true || FundoSab == true || FundoDom == true) {
                    // Criar uma nova instância de Materia com valores padrão vazios
                    final Dialog dialog = new Dialog(AdicionarTela.this, android.R.style.Theme_Translucent_NoTitleBar);
                    dialog.setContentView(R.layout.dialog_add_materia);

                    ImageView buttonCancelar = dialog.findViewById(R.id.buttonCancelarCard);

                    buttonCancelar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Fecha o Dialog
                            dialog.dismiss();
                        }
                    });

                    // Mostra o Dialog
                    dialog.show();
                    exibirDialogoAdicaoMateria(dialog);
                } else {
                    Toast.makeText(getApplicationContext(), "Selecione um dia da semana", Toast.LENGTH_SHORT).show();

                }
            }
        });

        Button btnAnterior = findViewById(R.id.btnAnterior);

        Button btnProximo = findViewById(R.id.btnProximo);
        btnProximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarParteDois();

                // Tornar o botão Próximo não clicável
                btnProximo.setAlpha(0.5f);
                btnAnterior.setAlpha(1.0f);
                btnProximo.setEnabled(false);
                btnAnterior.setEnabled(true);

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
                barraDois.setBackgroundColor(Color.parseColor("#E67E22"));
                barraDois.setAlpha(1.0f);


            }
        });

        btnAnterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarParteUm();

                // Tornar o botão Próximo não clicável
                btnProximo.setAlpha(1.0f);
                btnAnterior.setAlpha(0.5f);
                btnProximo.setEnabled(true);
                btnAnterior.setEnabled(false);
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
                barraUm.setBackgroundColor(Color.parseColor("#E67E22"));
                barraUm.setAlpha(1.0f);


            }
        });

        TextView textoSeg = findViewById(R.id.TextSeg);
        textoSeg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a GradientDrawable for the round background
                GradientDrawable background = new GradientDrawable();
                background.setShape(GradientDrawable.OVAL);  // Set shape to oval
                background.setColor(Color.parseColor("#E91E63"));  // Set the desired background color (adjust as needed)

                // Set the width and height of the drawable
                int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, Resources.getSystem().getDisplayMetrics());
                int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, Resources.getSystem().getDisplayMetrics());

                // Center the drawable behind the text view
                int padding = 5;  // Adjust padding as needed
                int left = textoSeg.getLeft() - (width - textoSeg.getWidth()) / 2 - padding;
                int top = textoSeg.getTop() - (height - textoSeg.getHeight()) / 2 - padding;
                background.setBounds(left, top, left + width + padding, top + height + padding);

                // Set the drawable as the background of the text view
                textoSeg.setBackground(background);

                ViewGroup parentLayout = (ViewGroup) textoSeg.getParent();
                for (int i = 0; i < parentLayout.getChildCount(); i++) {
                    View child = parentLayout.getChildAt(i);
                    if (child instanceof TextView && child != textoSeg) {
                        // Check if the current child is not one of the buttons to ignore
                        if (child.getId() != R.id.btnProximo && child.getId() != R.id.btnAnterior && child.getId() != R.id.btnAdicionarConteudo && child.getId() != R.id.btnSalvar) {
                            child.setBackground(null);
                        }
                    }
                }


                FundoSeg = true;
                FundoTer = false;
                FundoQua = false;
                FundoQui = false;
                FundoSex = false;
                FundoSab = false;
                FundoDom = false;

            }
        });

        TextView textoTer = findViewById(R.id.TextTer);
        textoTer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a drawable for the round background
                GradientDrawable background = new GradientDrawable();
                background.setShape(GradientDrawable.OVAL);  // Set shape to oval for a round background
                background.setColor(Color.parseColor("#7CB9E8"));  // Set the desired background color (replace with your preferred color)

                // Adjust the size of the background to match the text view
                int padding = 0; // Adjust padding as needed
                background.setBounds(textoTer.getLeft() - padding, textoTer.getTop() - padding,
                        textoTer.getRight() + padding, textoTer.getBottom() + padding);

                // Set the drawable as the background of the text view
                textoTer.setBackground(background);

                ViewGroup parentLayout = (ViewGroup) textoTer.getParent();
                for (int i = 0; i < parentLayout.getChildCount(); i++) {
                    View child = parentLayout.getChildAt(i);
                    if (child instanceof TextView && child != textoTer) {
                        // Check if the current child is not one of the buttons to ignore
                        if (child.getId() != R.id.btnProximo && child.getId() != R.id.btnAnterior && child.getId() != R.id.btnAdicionarConteudo && child.getId() != R.id.btnSalvar) {
                            child.setBackground(null);
                        }
                    }
                }

                FundoSeg = false;
                FundoTer = true;
                FundoQua = false;
                FundoQui = false;
                FundoSex = false;
                FundoSab = false;
                FundoDom = false;
            }
        });

        TextView textoQua = findViewById(R.id.TextQua);
        textoQua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a drawable for the round background
                GradientDrawable background = new GradientDrawable();
                background.setShape(GradientDrawable.OVAL);  // Set shape to oval
                background.setColor(Color.parseColor("#FFBB86FC"));  // Set the desired background color (adjust as needed)

                // Set the width and height of the drawable
                int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, Resources.getSystem().getDisplayMetrics());
                int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, Resources.getSystem().getDisplayMetrics());

                // Adjust the position of the drawable to center it behind the text view
                int padding = 5;  // Adjust padding as needed
                int left = textoQua.getLeft() - (width - textoQua.getWidth()) / 2 - padding;
                int top = textoQua.getTop() - (height - textoQua.getHeight()) / 2 - padding;
                background.setBounds(left, top, left + width + padding, top + height + padding);

                // Set the drawable as the background of the text view
                textoQua.setBackground(background);

                ViewGroup parentLayout = (ViewGroup) textoQua.getParent();
                for (int i = 0; i < parentLayout.getChildCount(); i++) {
                    View child = parentLayout.getChildAt(i);
                    if (child instanceof TextView && child != textoQua) {
                        // Check if the current child is not one of the buttons to ignore
                        if (child.getId() != R.id.btnProximo && child.getId() != R.id.btnAnterior && child.getId() != R.id.btnAdicionarConteudo && child.getId() != R.id.btnSalvar) {
                            child.setBackground(null);
                        }
                    }
                }

                FundoSeg = false;
                FundoTer = false;
                FundoQua = true;
                FundoQui = false;
                FundoSex = false;
                FundoSab = false;
                FundoDom = false;
            }
        });


        TextView textoQui = findViewById(R.id.TextQui);
        textoQui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a GradientDrawable for the round background
                GradientDrawable background = new GradientDrawable();
                background.setShape(GradientDrawable.OVAL);  // Set shape to oval
                background.setColor(Color.parseColor("#FF9800"));  // Set the desired background color (adjust as needed)

                // Set the width and height of the drawable
                int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, Resources.getSystem().getDisplayMetrics());
                int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, Resources.getSystem().getDisplayMetrics());

                // Center the drawable behind the text view
                int padding = 5;  // Adjust padding as needed
                int left = textoQui.getLeft() - (width - textoQui.getWidth()) / 2 - padding;
                int top = textoQui.getTop() - (height - textoQui.getHeight()) / 2 - padding;
                background.setBounds(left, top, left + width + padding, top + height + padding);

                // Set the drawable as the background of the text view
                textoQui.setBackground(background);

                ViewGroup parentLayout = (ViewGroup) textoQui.getParent();
                for (int i = 0; i < parentLayout.getChildCount(); i++) {
                    View child = parentLayout.getChildAt(i);
                    if (child instanceof TextView && child != textoQui) {
                        // Check if the current child is not one of the buttons to ignore
                        if (child.getId() != R.id.btnProximo && child.getId() != R.id.btnAnterior && child.getId() != R.id.btnAdicionarConteudo && child.getId() != R.id.btnSalvar) {
                            child.setBackground(null);
                        }
                    }
                }

                FundoSeg = false;
                FundoTer = false;
                FundoQua = false;
                FundoQui = true;
                FundoSex = false;
                FundoSab = false;
                FundoDom = false;
            }
        });

        TextView textoSex = findViewById(R.id.TextSex);
        textoSex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a GradientDrawable for the round background
                GradientDrawable background = new GradientDrawable();
                background.setShape(GradientDrawable.OVAL);  // Set shape to oval
                background.setColor(Color.parseColor("#7CB342"));  // Set the desired background color (adjust as needed)

                // Set the width and height of the drawable
                int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, Resources.getSystem().getDisplayMetrics());
                int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, Resources.getSystem().getDisplayMetrics());

                // Center the drawable behind the text view
                int padding = 5;  // Adjust padding as needed
                int left = textoSex.getLeft() - (width - textoSex.getWidth()) / 2 - padding;
                int top = textoSex.getTop() - (height - textoSex.getHeight()) / 2 - padding;
                background.setBounds(left, top, left + width + padding, top + height + padding);

                // Set the drawable as the background of the text view
                textoSex.setBackground(background);

                ViewGroup parentLayout = (ViewGroup) textoSex.getParent();
                for (int i = 0; i < parentLayout.getChildCount(); i++) {
                    View child = parentLayout.getChildAt(i);
                    if (child instanceof TextView && child != textoSex) {
                        // Check if the current child is not one of the buttons to ignore
                        if (child.getId() != R.id.btnProximo && child.getId() != R.id.btnAnterior && child.getId() != R.id.btnAdicionarConteudo && child.getId() != R.id.btnSalvar) {
                            child.setBackground(null);
                        }
                    }
                }

                FundoSeg = false;
                FundoTer = false;
                FundoQua = false;
                FundoQui = false;
                FundoSex = true;
                FundoSab = false;
                FundoDom = false;
            }
        });

        TextView textoSab = findViewById(R.id.TextSab);
        textoSab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a GradientDrawable for the round background
                GradientDrawable background = new GradientDrawable();
                background.setShape(GradientDrawable.OVAL);  // Set shape to oval
                background.setColor(Color.parseColor("#03A9F4"));  // Set the desired background color (adjust as needed)

                // Set the width and height of the drawable
                int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, Resources.getSystem().getDisplayMetrics());
                int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, Resources.getSystem().getDisplayMetrics());

                // Center the drawable behind the text view
                int padding = 5;  // Adjust padding as needed
                int left = textoSab.getLeft() - (width - textoSab.getWidth()) / 2 - padding;
                int top = textoSab.getTop() - (height - textoSab.getHeight()) / 2 - padding;
                background.setBounds(left, top, left + width + padding, top + height + padding);

                // Set the drawable as the background of the text view
                textoSab.setBackground(background);

                ViewGroup parentLayout = (ViewGroup) textoSab.getParent();
                for (int i = 0; i < parentLayout.getChildCount(); i++) {
                    View child = parentLayout.getChildAt(i);
                    if (child instanceof TextView && child != textoSab) {
                        // Check if the current child is not one of the buttons to ignore
                        if (child.getId() != R.id.btnProximo && child.getId() != R.id.btnAnterior && child.getId() != R.id.btnAdicionarConteudo && child.getId() != R.id.btnSalvar) {
                            child.setBackground(null);
                        }
                    }
                }

                FundoSeg = false;
                FundoTer = false;
                FundoQua = false;
                FundoQui = false;
                FundoSex = false;
                FundoSab = true;
                FundoDom = false;
            }
        });


        TextView textoDom = findViewById(R.id.TextDom);
        textoDom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a GradientDrawable for the round background
                GradientDrawable background = new GradientDrawable();
                background.setShape(GradientDrawable.OVAL);  // Set shape to oval
                background.setColor(Color.parseColor("#FFEB3B"));  // Set the desired background color (adjust as needed)

                // Set the width and height of the drawable
                int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, Resources.getSystem().getDisplayMetrics());
                int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, Resources.getSystem().getDisplayMetrics());

                // Center the drawable behind the text view
                int padding = 5;  // Adjust padding as needed
                int left = textoDom.getLeft() - (width - textoDom.getWidth()) / 2 - padding;
                int top = textoDom.getTop() - (height - textoDom.getHeight()) / 2 - padding;
                background.setBounds(left, top, left + width + padding, top + height + padding);

                // Set the drawable as the background of the text view
                textoDom.setBackground(background);

                ViewGroup parentLayout = (ViewGroup) textoDom.getParent();
                for (int i = 0; i < parentLayout.getChildCount(); i++) {
                    View child = parentLayout.getChildAt(i);
                    if (child instanceof TextView && child != textoDom) {
                        // Check if the current child is not one of the buttons to ignore
                        if (child.getId() != R.id.btnProximo && child.getId() != R.id.btnAnterior && child.getId() != R.id.btnAdicionarConteudo && child.getId() != R.id.btnSalvar) {
                            child.setBackground(null);
                        }
                    }
                }

                FundoSeg = false;
                FundoTer = false;
                FundoQua = false;
                FundoQui = false;
                FundoSex = false;
                FundoSab = false;
                FundoDom = true;
            }
        });

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

        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);


        // Define as dimensões desejadas
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(alertDialog.getWindow().getAttributes());
        layoutParams.width = getResources().getDimensionPixelSize(R.dimen.your_dialog_width); // Substitua com a largura desejada
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT; // Define a altura automaticamente, com base no tamanho necessario do elemento

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

        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);

        // Define as dimensões desejadas
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(alertDialog.getWindow().getAttributes());
        layoutParams.width = getResources().getDimensionPixelSize(R.dimen.your_dialog_width); // Substitua com a largura desejada
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT; // Define a altura automaticamente, com base no tamanho necessario do elemento

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
        Spinner spinnerCategoria = findViewById(R.id.spinnerCategoria);
        ((TextView) spinnerCategoria.getChildAt(0)).setTextColor(Color.parseColor(corFontHex));
        ((TextView) spinnerCategoria.getChildAt(0)).setHintTextColor(Color.parseColor(corFontHex));
                /*
                spinnerCategoria.setPopupBackgroundResource(R.drawable.seu_drawable_de_fundo_para_dropdown);
                */

    }

    private void showIconSelectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        IconAdapter adapter = new IconAdapter(this, defaultIcons);

        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectIconIndex = which;
                updateIcon();
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();

        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);


        // Define as dimensões desejadas
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(alertDialog.getWindow().getAttributes());
        layoutParams.width = getResources().getDimensionPixelSize(R.dimen.your_dialog_width); // Substitua com a largura desejada
        layoutParams.height = getResources().getDimensionPixelSize(R.dimen.your_dialog_height); // Substitua com a altura desejada

        alertDialog.show();
        alertDialog.getWindow().setAttributes(layoutParams);

        View IconButton = findViewById(R.id.IconImage); // Substitua com o ID real do botão TextoColor
        if (IconButton != null) {
            int[] location = new int[2];
            IconButton.getLocationOnScreen(location);
            layoutParams.gravity = Gravity.TOP | Gravity.START;
            layoutParams.x = location[0] + (IconButton.getWidth() / 2) - (layoutParams.width / 2);
            layoutParams.y = location[1] - layoutParams.height - getResources().getDimensionPixelSize(R.dimen.additional_margin); // Ajuste aqui
            alertDialog.getWindow().setAttributes(layoutParams);
        }

    }

    private void updateIcon() {
        Integer iconInteger = defaultIcons[selectIconIndex];

        ImageView iconImageView = findViewById(R.id.IconImage);
        iconImageView.setImageResource(iconInteger);

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

        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);


        // Define as dimensões desejadas
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(alertDialog.getWindow().getAttributes());
        layoutParams.width = getResources().getDimensionPixelSize(R.dimen.your_dialog_width); // Substitua com a largura desejada
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT; // Define a altura automaticamente, com base no tamanho necessario do elemento

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

        // Extrair a cor atual do GradientDrawable
        ColorStateList colorStateList = gradientDrawable.getColor();
        int corAtual = colorStateList.getDefaultColor();

        // Criar uma transição de cor
        int novaCor = Color.parseColor(corFundoHex);
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), corAtual, novaCor);
        colorAnimation.setDuration(1000); // Duração da animação em milissegundos

        // Adicionar um listener para animar a mudança de cor
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                gradientDrawable.setColor((int) animator.getAnimatedValue());
            }
        });

        // Iniciar a animação
        colorAnimation.start();
    }




    private int inserirAgrupamento(String nomeAgrupamento, String categoria, String corFundoHex, String corBotoesHex, String corTextoHex, int iconEscolhido) {
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
            values.put("iconEscolhido", iconEscolhido);

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


    public void salvarAgrupamento() {
        // Obter os valores dos campos
        valoresNomeMateria.clear();
        valoresDataSemana.clear();
        valoresQuantAulas.clear();

        Spinner spinnerCategoria = findViewById(R.id.spinnerCategoria);
        String categoria = spinnerCategoria.getSelectedItem().toString();


        String nomeAgrupamento = ((TextInputEditText) findViewById(R.id.NomeCard)).getText().toString();
        String corFundoHex = defaultHexColors[selectedFundoColorIndex];
        String corBotoesHex = defaultButtonHexColors[selectedButtonColorIndex];
        String corTextoHex = defaultFontHexColors[selectedFontColorIndex];
        int iconEscolhido = defaultIcons[selectIconIndex];

        if (nomeAgrupamento.trim().isEmpty() || categoria.trim().isEmpty()) {
            // Mostrar mensagem de erro (por exemplo, usando Toast)
            Toast.makeText(this, "Preencha todos os campos obrigatórios", Toast.LENGTH_SHORT).show();
            return;
        }

        Set<TextView> uniqueNomeMateriaEditTexts = new HashSet<>(adicionarMateriaAdapter.getNomeMateriaEditTexts());
        Set<TextView> uniqueDataSemanaSpinners = new HashSet<>(adicionarMateriaAdapter.getSpinnerDataMateriaList());
        Set<TextView> uniqueQuantAulasEditTexts = new HashSet<>(adicionarMateriaAdapter.getQuantAulasEditTexts());

        List<Materia> listaDeMaterias = adicionarMateriaAdapter.getListaDeMaterias();


        int idAgrupamento = inserirAgrupamento(nomeAgrupamento, categoria, corFundoHex, corBotoesHex, corTextoHex, iconEscolhido);

        for (Materia materia : listaDeMaterias) {
            String nomeMateria = materia.getNomeMateria();
            String dataSemana = materia.getDataMateria();
            String quantAulas = materia.getQuantAulas();

            // Salve esses valores no banco de dados (substitua esta linha pelo seu método de salvar no banco)
            inserirMaterias(nomeMateria, dataSemana, idAgrupamento, quantAulas);
        }

        for (TextView nomeMateriaEditText : uniqueNomeMateriaEditTexts) {
            String nomeMateria = nomeMateriaEditText.getText().toString();
            valoresNomeMateria.add(nomeMateria);
        }

        for (TextView dataSemanaSpinner : uniqueDataSemanaSpinners) {
            String selectedDate = dataSemanaSpinner.getText().toString();
            valoresDataSemana.add(selectedDate);
        }

        for (TextView quantAulasEditText : uniqueQuantAulasEditTexts) {
            String quantAulas = quantAulasEditText.getText().toString();
            valoresQuantAulas.add(quantAulas);
        }


        // Atualizar a lista de agrupamentos

        limparCampos();

        // Fechar a tela de adicionar algo

        salvamentoOcorreu = true;
        finish();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }


    private void limparCampos() {
        // Limpe os campos de nome e categoria (ou faça o que for apropriado)
        Spinner spinnerCategoria = findViewById(R.id.spinnerCategoria);

        ((TextInputEditText) findViewById(R.id.NomeCard)).setText("");
        spinnerCategoria.setSelection(0);  // Pode ajustar para o índice do item desejado
        adicionarMateriaAdapter.limparMaterias();


    }

    private void inserirMaterias(String nomeMateria, String dataSemana, int idAgrupamento, String quantAula) {
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(this);
            SQLiteDatabase bancoDados = dbHelper.getWritableDatabase();



                ContentValues valuesMateria = new ContentValues();
                valuesMateria.put("nome_materia", nomeMateria);
                valuesMateria.put("dia_semana", dataSemana);
                valuesMateria.put("id_agrupamento", idAgrupamento);
                valuesMateria.put("quantAulas", quantAula);

                Log.d("TAG", "NomeMateriaInsert: " + nomeMateria + " DataSemanaInsert: " + dataSemana + " QuantAula: " + quantAula);

            bancoDados.insert("materias", null, valuesMateria);


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
        View colorPreview2 = findViewById(R.id.colorPreview);
        TextInputEditText NomeCard = findViewById(R.id.NomeCard);
        Spinner textInputEditText = findViewById(R.id.spinnerCategoria);
        iconImageView = findViewById(R.id.IconImage);
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
        /*
        ImageView view2 = findViewById(R.id.view2);

         */

        RecyclerView recyclerViewMaterias1 = findViewById(R.id.recyclerViewMaterias);
        ImageView view3 = findViewById(R.id.view3);
        Button btnAdicionarConteudo2 = findViewById(R.id.btnAdicionarConteudo);
        ImageView Vector = findViewById(R.id.Vector);
        View BarraSuperiorMaterias = findViewById(R.id.BarraSuperiorMaterias);
        View fundoMateriasAdicionadas = findViewById(R.id.fundoMateriasAdicionadas);

        TextView TextSeg = findViewById(R.id.TextSeg);
        TextView TextTer = findViewById(R.id.TextTer);
        TextView TextQua = findViewById(R.id.TextQua);
        TextView TextQui = findViewById(R.id.TextQui);
        TextView TextSex = findViewById(R.id.TextSex);
        TextView TextSab = findViewById(R.id.TextSab);
        TextView TextDom = findViewById(R.id.TextDom);
        View view2sda = findViewById(R.id.view2sda);
        View FundoAddMaterias = findViewById(R.id.FundoAddMaterias);
        TextView TextTituloMateriasAdicionadas = findViewById(R.id.TextTituloMateriasAdicionadas);


        Vector.setVisibility(View.VISIBLE);
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
        iconImageView.setVisibility(View.VISIBLE);

        TextTituloMateriasAdicionadas.setVisibility(View.GONE);
        fundoMateriasAdicionadas.setVisibility(View.GONE);
        MateriasText.setVisibility(View.GONE);
        TextSeg.setVisibility(View.GONE);
        TextTer.setVisibility(View.GONE);
        TextQua.setVisibility(View.GONE);
        TextQui.setVisibility(View.GONE);
        TextSex.setVisibility(View.GONE);
        TextSab.setVisibility(View.GONE);
        TextDom.setVisibility(View.GONE);
        view2sda.setVisibility(View.GONE);
        FundoAddMaterias.setVisibility(View.GONE);


        BarraSuperiorMaterias.setVisibility(View.GONE);
        MateriasText.setVisibility(View.GONE);
        SombraFaixa.setVisibility(View.GONE);
        recyclerViewMaterias1.setVisibility(View.GONE);
        view3.setVisibility(View.GONE);
        btnAdicionarConteudo2.setVisibility(View.GONE);
        /*
        view2.setVisibility(View.GONE);

         */

    }

    private void mostrarParteDois() {
        View TextoColor = findViewById(R.id.TextoColor);
        ImageView textFundo = findViewById(R.id.TextFundo);
        View FundroCriarCard = findViewById(R.id.FundroCriarCard);
        TextView TextCriarCard = findViewById(R.id.TextCriarCard);
        ImageView imageView4 = findViewById(R.id.imageView4);
        View colorPreview2 = findViewById(R.id.colorPreview);
        TextInputEditText NomeCard = findViewById(R.id.NomeCard);
        Spinner textInputEditText = findViewById(R.id.spinnerCategoria);
        iconImageView = findViewById(R.id.IconImage);
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
        /*
        ImageView view2 = findViewById(R.id.view2);
         */
        RecyclerView recyclerViewMaterias1 = findViewById(R.id.recyclerViewMaterias);
        ImageView view3 = findViewById(R.id.view3);
        Button btnAdicionarConteudo2 = findViewById(R.id.btnAdicionarConteudo);
        ImageView Vector = findViewById(R.id.Vector);
        View BarraSuperiorMaterias = findViewById(R.id.BarraSuperiorMaterias);
        TextView TextTituloMateriasAdicionadas = findViewById(R.id.TextTituloMateriasAdicionadas);
        TextView TextSeg = findViewById(R.id.TextSeg);
        TextView TextTer = findViewById(R.id.TextTer);
        TextView TextQua = findViewById(R.id.TextQua);
        TextView TextQui = findViewById(R.id.TextQui);
        TextView TextSex = findViewById(R.id.TextSex);
        TextView TextSab = findViewById(R.id.TextSab);
        TextView TextDom = findViewById(R.id.TextDom);
        View view2sda = findViewById(R.id.view2sda);
        View FundoAddMaterias = findViewById(R.id.FundoAddMaterias);
        View fundoMateriasAdicionadas = findViewById(R.id.fundoMateriasAdicionadas);

        fundoMateriasAdicionadas.setVisibility(View.VISIBLE);
        TextSeg.setVisibility(View.VISIBLE);
        TextTer.setVisibility(View.VISIBLE);
        TextQua.setVisibility(View.VISIBLE);
        TextQui.setVisibility(View.VISIBLE);
        TextSex.setVisibility(View.VISIBLE);
        TextSab.setVisibility(View.VISIBLE);
        TextDom.setVisibility(View.VISIBLE);
        view2sda.setVisibility(View.VISIBLE);
        FundoAddMaterias.setVisibility(View.VISIBLE);
        BarraSuperiorMaterias.setVisibility(View.VISIBLE);
        MateriasText.setVisibility(View.VISIBLE);
        SombraFaixa.setVisibility(View.VISIBLE);
        TextTituloMateriasAdicionadas.setVisibility(View.VISIBLE);

        /*
        view2.setVisibility(View.VISIBLE);
        */

        recyclerViewMaterias1.setVisibility(View.VISIBLE);
        view3.setVisibility(View.VISIBLE);
        btnAdicionarConteudo2.setVisibility(View.VISIBLE);
        btnSalvar2.setVisibility(View.VISIBLE);

        iconImageView.setVisibility(View.GONE);
        Vector.setVisibility(View.GONE);
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


    private void exibirDialogoAdicaoMateria(final Dialog dialog) {


        // Obter referências para os EditTexts e Spinner no layout do diálogo
        EditText editTextNomeMateria = dialog.findViewById(R.id.textViewNomeMateria);
        EditText editTextQuantAula = dialog.findViewById(R.id.textViewQuantAulas);
        Spinner spinnerDiaSemana = dialog.findViewById(R.id.spinnerDataMateria);

        // Configurar o adaptador para o Spinner com a lista de dias da semana
        List<String> listaDeDiasSemana = Arrays.asList("Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado", "Domingo");
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, R.layout.spinner_preto, listaDeDiasSemana);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDiaSemana.setAdapter(spinnerAdapter);

        if (FundoSeg) {
            spinnerDiaSemana.setSelection(0);
        } else if (FundoTer) {
            spinnerDiaSemana.setSelection(1);
        } else if (FundoQua) {
            spinnerDiaSemana.setSelection(2);
        } else if (FundoQui) {
            spinnerDiaSemana.setSelection(3);
        } else if (FundoSex) {
            spinnerDiaSemana.setSelection(4);
        } else if (FundoSab) {
            spinnerDiaSemana.setSelection(5);
        } else if (FundoDom) {
            spinnerDiaSemana.setSelection(6);
        }


        View buttonSalvarMateria = dialog.findViewById(R.id.buttonAdicionar);

        buttonSalvarMateria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nomeMateria = editTextNomeMateria.getText().toString();
                String quantAula = editTextQuantAula.getText().toString();
                String diaSemana = spinnerDiaSemana.getSelectedItem().toString();

                salvarMateria(dialog, nomeMateria, quantAula,  diaSemana);



                if (listaDeMaterias.size() > 2) {
                    // Ajustar a altura da RecyclerView para 150dp
                    ViewGroup.LayoutParams layoutParams = recyclerViewMaterias.getLayoutParams();
                    layoutParams.height = (int) getResources().getDimension(R.dimen.recycler_view_height);
                    recyclerViewMaterias.setLayoutParams(layoutParams);
                }
            }
        });
    }


    private void salvarMateria(Dialog dialog,String nomeMateria,String quantAula, String diaSemana) {
        // Lógica para salvar a matéria
        EditText editTextNomeMateria = dialog.findViewById(R.id.textViewNomeMateria);
        EditText editTextQuantAula = dialog.findViewById(R.id.textViewQuantAulas);
        Spinner spinnerDiaSemana = dialog.findViewById(R.id.spinnerDataMateria);


        if (!TextUtils.isEmpty(nomeMateria) && !TextUtils.isEmpty(quantAula)) {
            // Criar uma nova instância de Materia com os valores inseridos
            Materia novaMateria = new Materia(nomeMateria, diaSemana, 0, 0, quantAula);

            // Adicionar a nova matéria ao adaptador
            adicionarMateriaAdapter.adicionarMateria(novaMateria);

            // Notificar o adaptador sobre a alteração nos dados
            adicionarMateriaAdapter.notifyItemInserted(listaDeMaterias.size() - 1);
            recyclerViewMaterias.smoothScrollToPosition(listaDeMaterias.size() - 1);

            dialog.dismiss();
        } else {
            Toast.makeText(getApplicationContext(), "Preencha todos os campos", Toast.LENGTH_SHORT).show();

        }
    }

}
