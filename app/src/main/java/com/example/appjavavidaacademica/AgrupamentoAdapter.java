package com.example.appjavavidaacademica;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class AgrupamentoAdapter extends ArrayAdapter<Agrupamento> {

    private DatabaseHelper dbHelper;
    private MainActivity mainActivity;

    public AgrupamentoAdapter(Context context, List<Agrupamento> agrupamentos, MainActivity mainActivity) {
        super(context, 0, agrupamentos);
        dbHelper = new DatabaseHelper(context);
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.agrupamento_card, parent, false);
        }

        Agrupamento agrupamento = getItem(position);


        TextView nomeAgrupamentoTextView = listItemView.findViewById(R.id.nomeAgrupamentoTextView);
        TextView categoriaTextView = listItemView.findViewById(R.id.TextCategoriaCard);
        TextView corHexTextView = listItemView.findViewById(R.id.corHexTextView);
        View colorSquare = listItemView.findViewById(R.id.colorSquare);
        ImageView iconEscolhido = listItemView.findViewById(R.id.IconImageAdicionada);


        if (agrupamento != null) {


            String corBotoesHex = agrupamento.getCorButtonHex();
            int corBotoesInt = Color.parseColor(corBotoesHex);

            String corTextHex = agrupamento.getCorTextoHex();
            int corTextInt = Color.parseColor(corTextHex);
            int iconEscolhido1 = agrupamento.getIconEscolhido();

            GradientDrawable btnDrawable = new GradientDrawable();
            btnDrawable.setShape(GradientDrawable.RECTANGLE);
            btnDrawable.setCornerRadius(70); // Radius das bordas
            btnDrawable.setColor(corBotoesInt);

            nomeAgrupamentoTextView.setText(agrupamento.getNomeAgrupamento());
            categoriaTextView.setText(agrupamento.getCategoria());
            corHexTextView.setText(agrupamento.getCorFundoHex());

            iconEscolhido.setImageResource(iconEscolhido1);
            nomeAgrupamentoTextView.setTextColor(corTextInt);
            categoriaTextView.setTextColor(corTextInt);

            // Criar um GradientDrawable com bordas arredondadas
            GradientDrawable drawable = new GradientDrawable();
            drawable.setColor(Color.parseColor(agrupamento.getCorFundoHex())); // Cor de fundo
            drawable.setCornerRadius(70); // Radius das bordas

            // Configurar o drawable como plano de fundo da colorSquare
            colorSquare.setBackground(drawable);

            View btnAcaoEdit = listItemView.findViewById(R.id.btnEdit);
            View btnAcaoDetails = listItemView.findViewById(R.id.btnDetails);
            View btnAcaoDelete = listItemView.findViewById(R.id.btnDelete);


            btnAcaoEdit.setBackground(btnDrawable);
            btnAcaoDelete.setBackground(btnDrawable);
            btnAcaoDetails.setBackground(btnDrawable);


            final int agrupamentoId = agrupamento.getId();

            btnAcaoEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Lógica para ação 1 com base no ID do agrupamento
                    // Exemplo: Toast.makeText(getContext(), "Ação 1 clicada para o ID: " + agrupamento.getId(), Toast.LENGTH_SHORT).show();
                    Log.d("AgrupamentoAdapter", "Editar agrupamento com ID: " + agrupamentoId);
                }
            });

            btnAcaoDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Lógica para ação 2 com base no ID do agrupamento
                    // Exemplo: Toast.makeText(getContext(), "Ação 2 clicada para o ID: " + agrupamento.getId(), Toast.LENGTH_SHORT).show();
                    Log.d("AgrupamentoAdapter", "Detalhes do agrupamento com ID: " + agrupamentoId);

                    // Criar um Intent para iniciar a DetalhesAgrupamentoActivity
                    Intent intent = new Intent(getContext(), DetalhesAgrupamento.class);

                    // Passar o ID do agrupamento para a nova atividade
                    intent.putExtra("AGRUPO_ID", agrupamentoId);
                    Log.e("TAG","ID: " + agrupamentoId);
                    getContext().startActivity(intent);
                }
            });

            btnAcaoDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // Exibir um AlertDialog de confirmação
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                    // Inflar o layout personalizado
                    View customDialogView = LayoutInflater.from(getContext()).inflate(R.layout.custom_dialog, null);
                    builder.setView(customDialogView);

                    builder.setTitle("Confirmação de Exclusão do Card:  " + agrupamento.getNomeAgrupamento());

                    // Configurar a mensagem com a cor do texto preto
                    builder.setMessage(Html.fromHtml("<font color='#000000'>A exclusão do Card, apagará todos os dados relacionados, como materias, faltas e notas cadastradas.?</font>"));

                    // Botão de confirmação
                    builder.setPositiveButton("Apagar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Lógica de exclusão aqui
                            excluirAgrupamento(agrupamentoId);
                        }
                    });

                    // Botão de cancelamento
                    builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Fechar o diálogo, sem fazer nada
                            dialog.dismiss();
                        }
                    });


                    // Personalizar o botão "Apagar"
                    AlertDialog alertDialog = builder.create();

                    // Criar e exibir o AlertDialog
                    alertDialog.show();

                    // Personalizar o fundo do AlertDialog para branco
                    alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.white);

                    // Personalizar a cor do texto do título
                    int titleId = getContext().getResources().getIdentifier("alertTitle", "id", "android");
                    TextView titleView = alertDialog.findViewById(titleId);
                    if (titleView != null) {
                        titleView.setTextColor(Color.BLACK);
                    }

                    // Personalizar a cor do texto dos botões
                    Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                    Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);

                    if (positiveButton != null && negativeButton != null) {
                        positiveButton.setTextColor(Color.BLACK);
                        negativeButton.setTextColor(Color.BLACK);
                    }
                }

            });
        }
        return listItemView;
    }

    private void excluirAgrupamento(int agrupamentoId) {
        try {
            // Certifique-se de que agrupamentoId está definido corretamente
            if (agrupamentoId == -1) {
                Log.e("AgrupamentoAdapter", "ID do agrupamento não está definido corretamente.");
                return;
            }

            // Abre ou cria o banco de dados
            SQLiteDatabase bancoDados = dbHelper.getWritableDatabase();

            // Query SQL para excluir o registro com o ID correspondente
            String queryExclusao = "DELETE FROM agrupamento WHERE id_agrupamento = ?";

            // Parâmetros para a query (ID do agrupamento)
            String[] parametros = {String.valueOf(agrupamentoId)};

            // Executa a operação de exclusão
            bancoDados.execSQL(queryExclusao, parametros);

            // Verifica se mainActivity não é nulo antes de chamar o método
            if (mainActivity != null) {
                mainActivity.atualizarListaAgrupamento();
            }

            // Fecha o banco de dados
            bancoDados.close();

            // Exibir uma mensagem ou atualizar a interface conforme necessário
            Log.d("AgrupamentoAdapter", "Exclusão bem-sucedida para o ID: " + agrupamentoId);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("AgrupamentoAdapter", "Erro ao excluir agrupamento: " + e.getMessage());
        }
    }

}
