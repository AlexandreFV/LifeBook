package com.example.appjavavidaacademica;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.ViewHolder> {

    private List<Faltas> listaDeFaltas;

    public TableAdapter(List<Faltas> listaDeFaltas) {
        this.listaDeFaltas = listaDeFaltas;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        // Declare as Views necessárias
        TextView textViewDataItem;
        TextView textViewQuantidadeItem;
        TextView textViewMotivoItem;

        ViewHolder(View itemView) {
            super(itemView);
            // Inicialize as Views
            textViewDataItem = itemView.findViewById(R.id.textViewDataItem);
            textViewQuantidadeItem = itemView.findViewById(R.id.textViewQuantidadeItem);
            textViewMotivoItem = itemView.findViewById(R.id.textViewMotivoItem);
        }

        void bind(Faltas falta) {
            // Atualize as Views com dados da listaDeFaltas.get(getAdapterPosition())
            textViewDataItem.setText(falta.getData());
            textViewQuantidadeItem.setText(String.valueOf(falta.getQuantidade()));
            textViewMotivoItem.setText(falta.getMotivo());
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate do layout do item e criação do ViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_table_faltas, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Preencher as Views com dados da listaDeFaltas.get(position)
        holder.bind(listaDeFaltas.get(position));
    }

    @Override
    public int getItemCount() {
        return listaDeFaltas.size();
    }
}
