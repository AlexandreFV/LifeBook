package com.example.appjavavidaacademica;

import android.app.Dialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class LembreteAdapter extends RecyclerView.Adapter<LembreteAdapter.LembreteViewHolder> {

    private List<Lembrete> lembretes;
    private Context context;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase bancoDados;


    private int posicaoDoItemClicado = RecyclerView.NO_POSITION;


    public LembreteAdapter(Context context) {
        this.context = context;
        this.lembretes = new ArrayList<>();
    }

    public void adicionarLembrete(Lembrete lembrete) {
        lembretes.add(lembrete);
        notifyDataSetChanged();
    }

    // Método para atualizar a lista de lembretes
    public void setLembretes(List<Lembrete> lembretes) {
        this.lembretes = lembretes;
    }


    @NonNull
    @Override
    public LembreteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_lembrete_adicionado, parent, false);
        return new LembreteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LembreteViewHolder holder, int position) {
        Lembrete lembrete = lembretes.get(position);
        holder.bind(lembrete);

        holder.textViewNomeMateria.setText(lembrete.getNomeMateria());
        holder.textViewCategoria.setText(lembrete.getCategoria());
        holder.textViewDescricao.setText(String.valueOf(lembrete.getDescricao()));

        // Define a String no TextView
        holder.textViewData.setText(lembrete.getData());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Atualizar a posição do item clicado
                posicaoDoItemClicado = position;
                // Notificar o adaptador sobre a mudança
                notifyDataSetChanged();
            }
        });

        holder.fundoBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    // Obtém o objeto Faltas correspondente à posição
                    Lembrete lembrete = lembretes.get(position);
                    // Exibe o AlertDialog de confirmação de exclusão
                    showDeleteConfirmationDialog(holder.itemView.getContext(), position);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return lembretes.size();
    }

    public class LembreteViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewNomeMateria;
        private TextView textViewCategoria;
        private TextView textViewDescricao;

        private TextView textViewData;

        private View ColapseView;


        public ImageView pontosIcon;

        public View ButtonFalteiMateria;

        public View fundoBtnDelete;
        public View fundoEdit;

        public ImageView lixeriaIconFundoDelete;

        public ImageView EditarMateria;

        public TextView btnFalteiMateriaAdicionada;

        public ImageView btnCloseAdicionarFalta;


        public LembreteViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNomeMateria = itemView.findViewById(R.id.NomeMateriaAdicionadoLembrete);
            textViewCategoria = itemView.findViewById(R.id.CategoriaLembreteAdicionado);
            textViewDescricao = itemView.findViewById(R.id.DescrAdicionadoLembrete);
            textViewData = itemView.findViewById(R.id.DataMateriaAdicionada);
            ColapseView = itemView.findViewById(R.id.ColapseView);
            pontosIcon = itemView.findViewById(R.id.pontosIcon);
            fundoBtnDelete = itemView.findViewById(R.id.fundoBtnDelete);
            fundoEdit = itemView.findViewById(R.id.fundoEdit);
            lixeriaIconFundoDelete = itemView.findViewById(R.id.lixeriaIconFundoDelete);
            EditarMateria = itemView.findViewById(R.id.EditarMateria);
            btnCloseAdicionarFalta = itemView.findViewById(R.id.btnCloseAdicionarFalta);

            ColapseView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toggleVisibility(textViewCategoria);
                    toggleVisibility(textViewDescricao);
                    toggleVisibility(pontosIcon);


                    if (fundoEdit.getVisibility() == View.VISIBLE) {
                        toggleVisibility(fundoEdit); // Substitua "icon1" pelo ID do primeiro ícone
                        toggleVisibility(fundoBtnDelete); // Substitua "icon2" pelo ID do segundo ícone

                        toggleVisibility(lixeriaIconFundoDelete); // Substitua "icon3" pelo ID do terceiro ícone
                        toggleVisibility(EditarMateria); // Substitua "icon3" pelo ID do terceiro ícone
                        toggleVisibility(pontosIcon);

                    }

                }
            });

            pontosIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Adicione a lógica para exibir os três ícones acima do layout existente
                    toggleVisibility(fundoEdit); // Substitua "icon1" pelo ID do primeiro ícone
                    toggleVisibility(fundoBtnDelete); // Substitua "icon2" pelo ID do segundo ícone

                    toggleVisibility(lixeriaIconFundoDelete); // Substitua "icon3" pelo ID do terceiro ícone
                    toggleVisibility(EditarMateria); // Substitua "icon3" pelo ID do terceiro ícone


                }
            });
        }

        public void bind(Lembrete lembrete) {
            textViewNomeMateria.setText(lembrete.getNomeMateria());
            textViewCategoria.setText(lembrete.getCategoria());
            textViewDescricao.setText(lembrete.getDescricao());
            textViewData.setText(lembrete.getData());

        }
    }

    private void toggleVisibility(View view) {
        if (view.getVisibility() == View.VISIBLE) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
        }

    }


    private void showDeleteConfirmationDialog(Context context, int position) {
        // Inflar o layout do dialog personalizado

        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_deletar_lembrete, null);


        // Configurar elementos dentro do layout customizado
        TextView textExcluirFalta = dialogView.findViewById(R.id.TextLembreteExcluir);
        TextView textDesejaFalta = dialogView.findViewById(R.id.TextDesejaCard);
        TextView textNomeMateriaVariavel = dialogView.findViewById(R.id.TextNomeCardVariavel);
        View buttonRemover = dialogView.findViewById(R.id.buttonAdicionar);
        View buttonCancelar = dialogView.findViewById(R.id.buttonCancelarCard);
        TextView textNomeMateria = dialogView.findViewById(R.id.textView2);
        TextView TextDataFaltaApagar = dialogView.findViewById(R.id.TextDataFaltaApagar);
        TextView TextDataLembreteVariavel = dialogView.findViewById(R.id.TextDataFaltaVariavel);
        TextView TextQuantFaltasApagar = dialogView.findViewById(R.id.TextQuantFaltasApagar);
        TextView TextQuantFaltasVariavel = dialogView.findViewById(R.id.TextQuantFaltasVariavel);
        TextView TextMotivoFaltasApagar = dialogView.findViewById(R.id.TextMotivoFaltasApagar);
        TextView TextMotivoFaltasVariavel = dialogView.findViewById(R.id.TextMotivoFaltasVariavel);

        Lembrete lembrete = lembretes.get(position);
        String nomeMateria = lembrete.getNomeMateria();
        String categoria = lembrete.getCategoria();
        String dataFalta = String.valueOf(lembrete.getDescricao());
        String descricao = lembrete.getDescricao();

        TextMotivoFaltasVariavel.setText(descricao);
        TextQuantFaltasVariavel.setText(categoria);
        TextDataLembreteVariavel.setText(dataFalta);
        textNomeMateriaVariavel.setText(nomeMateria);

        // Criar o Dialog personalizado
        final Dialog customDialog = new Dialog(context, android.R.style.Theme_Light_NoTitleBar_Fullscreen); // Define o estilo para ocupar a tela inteira
        customDialog.setContentView(dialogView);

        // Configurar ações para os botões
        buttonRemover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chama o método de exclusão (ou implemente a lógica desejada)
                deleteItem(position);
                int id = lembrete.getIdLembrete();
                LembretesTela lembretesTela = new LembretesTela();

                deletarLembrete(id,lembretesTela);
                customDialog.dismiss();
            }
        });

        buttonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.dismiss();
            }
        });

        // Exibir o Dialog personalizado
        customDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent); // Define o fundo transparente
        customDialog.show();
    }

    // Método para excluir o item da lista
    private void deleteItem(int position) {
        // Implemente a lógica de exclusão aqui
        lembretes.remove(position);
        notifyItemRemoved(position);
    }

    private void deletarLembrete(int idLembrete, LembretesTela lembretesTela) {
        try {
            dbHelper = new DatabaseHelper(context.getApplicationContext());
            bancoDados = dbHelper.getWritableDatabase();

            // Define a cláusula WHERE para excluir o lembrete com o ID especificado
            String whereClause = "id_lembretes = ?";
            String[] whereArgs = {String.valueOf(idLembrete)};

            // Exclui o lembrete
            bancoDados.delete("lembretes", whereClause, whereArgs);

            bancoDados.close();
            lembretesTela.removerTodosDecoradores();

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TAG", "Erro ao deletar lembrete: " + e.getMessage());
        }
    }





}
