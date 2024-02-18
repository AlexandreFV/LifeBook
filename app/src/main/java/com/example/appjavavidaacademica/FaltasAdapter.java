package com.example.appjavavidaacademica;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FaltasAdapter extends RecyclerView.Adapter<FaltasAdapter.ViewHolder> {

    private List<Faltas> listaDeFaltas;
    private List<Faltas> listaDeFaltasFiltrada;
    private List<Faltas> listaDeFaltasOriginal;  // Adicione esta linha


    private int posicaoDoItemClicado = RecyclerView.NO_POSITION;


    public FaltasAdapter(List<Faltas> listaDeFaltas) {

        this.listaDeFaltas = listaDeFaltas;
        this.listaDeFaltasOriginal = new ArrayList<>(listaDeFaltas);  // Adicione esta linha
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_table_faltas, parent, false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Faltas falta = listaDeFaltas.get(position);
        // Preencher as Views com os dados da falta
        holder.textViewNomeItem.setText(falta.getNomeMateriaFaltas());
        holder.textViewDataItem.setText(falta.getData());
        holder.textViewQuantidadeItem.setText(String.valueOf(falta.getQuantidade()));
        holder.textViewMotivoItem.setText(falta.getMotivo());

        LinearLayout layoutSobreposicao = holder.itemView.findViewById(R.id.layoutSobreposicao);
        ImageView imageViewEdit = holder.itemView.findViewById(R.id.imageViewEdit);
        ImageView imageViewDetails = holder.itemView.findViewById(R.id.imageViewDetails);
        ImageView imageViewDelete = holder.itemView.findViewById(R.id.imageViewDelete);


        if (position == posicaoDoItemClicado) {
            layoutSobreposicao.setVisibility(View.VISIBLE);

            addGradientBackground(imageViewDelete);
            addGradientBackground(imageViewEdit);
            addGradientBackground(imageViewDetails);

        } else {
            layoutSobreposicao.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Atualizar a posição do item clicado
                posicaoDoItemClicado = position;
                // Notificar o adaptador sobre a mudança
                notifyDataSetChanged();
            }
        });

        holder.imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    // Obtém o objeto Faltas correspondente à posição
                    Faltas falta = listaDeFaltas.get(position);
                    // Exibe o AlertDialog de confirmação de exclusão
                    showDeleteConfirmationDialog(holder.itemView.getContext(), position);
                }
            }
        });

        holder.imageViewDetails.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    // Obtém o objeto Faltas correspondente à posição
                    Faltas falta = listaDeFaltas.get(position);
                    // Exibe o AlertDialog de confirmação de exclusão
                    showDetailsFalta(holder.itemView.getContext(), position);
                }
            }
        });

        }

    public void resetarListaOriginal() {
        listaDeFaltas.clear();
        listaDeFaltas.addAll(listaDeFaltasOriginal);
        notifyDataSetChanged();
    }

    private void addGradientBackground(ImageView imageView) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(Color.parseColor("#DCDCDC")); // Substitua com a cor desejada
        gradientDrawable.setShape(GradientDrawable.OVAL); // Define a forma como oval (círculo)

        // Calcula o raio para torná-lo um círculo (pode variar dependendo do tamanho da ImageView)
        int radius = imageView.getWidth() / 2;
        gradientDrawable.setCornerRadius(radius);


        // Define o GradientDrawable como plano de fundo da ImageView
        imageView.setBackground(gradientDrawable);
    }

    @Override
    public int getItemCount() {
        return listaDeFaltas.size();
    }


    public void filtrarDados(String cardSelecionado, String quantidadeFaltas, String nomeMateria) {
        List<Faltas> listaFiltrada = new ArrayList<>();

        // Verificar se o nome do card foi selecionado
        boolean filtrarPorCard = cardSelecionado != null && !"Todos".equals(cardSelecionado) && !cardSelecionado.isEmpty();

        // Verificar se a quantidade de faltas foi selecionada
        boolean filtrarPorQuantidade = quantidadeFaltas != null && !"Todos".equals(quantidadeFaltas) && !quantidadeFaltas.isEmpty();

        // Verificar se o nome da matéria foi selecionado
        boolean filtrarPorNomeMateria = nomeMateria != null && !"Todos".equals(nomeMateria) && !nomeMateria.isEmpty();

        // Usar a listaDeFaltasOriginal em vez de listaDeFaltas
        List<Faltas> listaOriginal = new ArrayList<>(listaDeFaltasOriginal);

        // Aplicar o filtro na lista original
        for (Faltas falta : listaOriginal) {
            boolean atendeFiltroCard = !filtrarPorCard || falta.getNomeAgrupamento().equals(cardSelecionado);
            boolean atendeFiltroQuantidade = !filtrarPorQuantidade || falta.getQuantidade() == Integer.parseInt(quantidadeFaltas);
            boolean atendeFiltroNomeMateria = !filtrarPorNomeMateria || falta.getNomeMateriaFaltas().equals(nomeMateria);

            // Adicione a condição para verificar se atende ao filtro de nome da matéria
            if (atendeFiltroCard && atendeFiltroQuantidade && atendeFiltroNomeMateria) {
                listaFiltrada.add(falta);
            }
        }
        // Atualizar a lista exibida no RecyclerView
        listaDeFaltas.clear();
        listaDeFaltas.addAll(listaFiltrada);
        notifyDataSetChanged();

    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        // Declare as Views necessárias para os itens da tabela
        TextView textViewNomeItem;
        TextView textViewDataItem;
        TextView textViewQuantidadeItem;
        TextView textViewMotivoItem;

        ImageView imageViewDelete;

        ImageView imageViewDetails;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Inicialize as Views para os itens da tabela
            textViewNomeItem = itemView.findViewById(R.id.textViewNomeItem);
            textViewDataItem = itemView.findViewById(R.id.textViewDataItem);
            textViewQuantidadeItem = itemView.findViewById(R.id.textViewQuantidadeItem);
            textViewMotivoItem = itemView.findViewById(R.id.textViewMotivoItem);
            imageViewDelete = itemView.findViewById(R.id.imageViewDelete);
            imageViewDetails = itemView.findViewById(R.id.imageViewDetails);

            imageViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        // Obtém o objeto Faltas correspondente à posição
                        Faltas falta = listaDeFaltas.get(position);
                        // Exibe o AlertDialog de confirmação de exclusão
                        showDeleteConfirmationDialog(itemView.getContext(), position);
                    }
                }
            });

            imageViewDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        // Obtém o objeto Faltas correspondente à posição
                        Faltas falta = listaDeFaltas.get(position);
                        // Exibe o AlertDialog de confirmação de exclusão
                        showDetailsFalta(itemView.getContext(), position);
                    }
                }
            });
        }

        void bind(Faltas falta) {
            // Atualize as Views com dados da listaDeFaltas.get(getAdapterPosition())
            textViewNomeItem.setText(falta.getNomeMateriaFaltas());
            textViewDataItem.setText(falta.getData());
            textViewQuantidadeItem.setText(String.valueOf(falta.getQuantidade()));
            textViewMotivoItem.setText(falta.getMotivo());
        }
    }
    private void showDeleteConfirmationDialog(Context context, int position) {
        // Inflar o layout do dialog personalizado
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_delete_falta, null);


        // Configurar elementos dentro do layout customizado
        TextView textExcluirFalta = dialogView.findViewById(R.id.TextDetalhesFalta);
        TextView textDesejaFalta = dialogView.findViewById(R.id.TextDesejaCard);
        TextView textNomeMateriaVariavel = dialogView.findViewById(R.id.TextNomeCardVariavel);
        View buttonRemover = dialogView.findViewById(R.id.buttonRemover);
        View buttonCancelar = dialogView.findViewById(R.id.buttonCancelarCard);
        TextView textNomeMateria = dialogView.findViewById(R.id.textView2);
        TextView TextDataFaltaApagar = dialogView.findViewById(R.id.TextDataFaltaApagar);
        TextView TextDataFaltaVariavel = dialogView.findViewById(R.id.TextDataFaltaVariavel);
        TextView TextQuantFaltasApagar = dialogView.findViewById(R.id.TextQuantFaltasApagar);
        TextView TextQuantFaltasVariavel = dialogView.findViewById(R.id.TextQuantFaltasVariavel);
        TextView TextMotivoFaltasApagar = dialogView.findViewById(R.id.TextMotivoFaltasApagar);
        TextView TextMotivoFaltasVariavel = dialogView.findViewById(R.id.TextMotivoFaltasVariavel);

        Faltas falta = listaDeFaltas.get(position);
        String motivoFalta = falta.getMotivo();
        String dataFalta = falta.getData();
        String quantFalta = String.valueOf(falta.getQuantidade());
        String nomeMateria = falta.getNomeMateriaFaltas();

        TextMotivoFaltasVariavel.setText(motivoFalta);
        TextQuantFaltasVariavel.setText(quantFalta);
        TextDataFaltaVariavel.setText(dataFalta);
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
        listaDeFaltas.remove(position);
        notifyItemRemoved(position);
    }


    private void showDetailsFalta (Context context, int position) {
        // Inflar o layout do dialog personalizado
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_details_falta, null);

        // Configurar elementos dentro do layout customizado
        TextView TextDetalhesFalta = dialogView.findViewById(R.id.TextDetalhesFalta);
        TextView TextNomeCardFaltaVariavel = dialogView.findViewById(R.id.TextNomeCardFaltaVariavel);
        TextView TextNomeCardFalta = dialogView.findViewById(R.id.TextNomeCardFalta);
        TextView textNomeMateriaVariavel = dialogView.findViewById(R.id.TextNomeCardVariavel);
        View buttonCancelar = dialogView.findViewById(R.id.buttonCancelarCard);
        TextView textNomeMateria = dialogView.findViewById(R.id.textView2);
        TextView TextDataFaltaApagar = dialogView.findViewById(R.id.TextDataFaltaApagar);
        TextView TextDataFaltaVariavel = dialogView.findViewById(R.id.TextDataFaltaVariavel);
        TextView TextQuantFaltasApagar = dialogView.findViewById(R.id.TextQuantFaltasApagar);
        TextView TextQuantFaltasVariavel = dialogView.findViewById(R.id.TextQuantFaltasVariavel);
        TextView TextMotivoFaltasApagar = dialogView.findViewById(R.id.TextMotivoFaltasApagar);
        TextView TextMotivoFaltasVariavel = dialogView.findViewById(R.id.TextMotivoFaltasVariavel);

        Faltas falta = listaDeFaltas.get(position);
        String nomeCard = falta.getNomeAgrupamento();
        String motivoFalta = falta.getMotivo();
        String dataFalta = falta.getData();
        String quantFalta = String.valueOf(falta.getQuantidade());
        String nomeMateria = falta.getNomeMateriaFaltas();

        TextMotivoFaltasVariavel.setText(motivoFalta);
        TextQuantFaltasVariavel.setText(quantFalta);
        TextDataFaltaVariavel.setText(dataFalta);
        textNomeMateriaVariavel.setText(nomeMateria);
        TextNomeCardFaltaVariavel.setText(nomeCard);


        // Criar o Dialog personalizado
        final Dialog customDialog = new Dialog(context, android.R.style.Theme_Light_NoTitleBar_Fullscreen); // Define o estilo para ocupar a tela inteira
        customDialog.setContentView(dialogView);


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


}

