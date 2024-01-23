package com.example.appjavavidaacademica;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FaltasAdapter extends RecyclerView.Adapter<FaltasAdapter.ViewHolder> {

    private List<Faltas> listaDeFaltas;

    private int posicaoDoItemClicado = RecyclerView.NO_POSITION;


    public FaltasAdapter(List<Faltas> listaDeFaltas) {
        this.listaDeFaltas = listaDeFaltas;
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
        ImageView imageViewDelete = holder.itemView.findViewById(R.id.imageViewDelete);
        ImageView imageViewEdit = holder.itemView.findViewById(R.id.imageViewEdit);
        ImageView imageViewDetails = holder.itemView.findViewById(R.id.imageViewDetails);

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

    static class ViewHolder extends RecyclerView.ViewHolder {
        // Declare as Views necessárias para os itens da tabela
        TextView textViewNomeItem;
        TextView textViewDataItem;
        TextView textViewQuantidadeItem;
        TextView textViewMotivoItem;

        ViewHolder(View itemView) {
            super(itemView);
            // Inicialize as Views para os itens da tabela
            textViewNomeItem = itemView.findViewById(R.id.textViewNomeItem);
            textViewDataItem = itemView.findViewById(R.id.textViewDataItem);
            textViewQuantidadeItem = itemView.findViewById(R.id.textViewQuantidadeItem);
            textViewMotivoItem = itemView.findViewById(R.id.textViewMotivoItem);
        }

        void bind(Faltas falta) {
            // Atualize as Views com dados da listaDeFaltas.get(getAdapterPosition())
            textViewNomeItem.setText(falta.getNomeMateriaFaltas());
            textViewDataItem.setText(falta.getData());
            textViewQuantidadeItem.setText(String.valueOf(falta.getQuantidade()));
            textViewMotivoItem.setText(falta.getMotivo());
        }

    }
}
