package com.example.appjavavidaacademica;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MateriaAdapter extends RecyclerView.Adapter<MateriaAdapter.MateriaViewHolder> {
    private List<Materia> listaDeMaterias;
    private List<TextInputEditText> nomeMateriaEditTexts;
    private List<TextInputEditText> dataSemanaEditTexts;
    public MateriaAdapter(List<Materia> listaDeMaterias) {
        this.listaDeMaterias = listaDeMaterias;
        nomeMateriaEditTexts = new ArrayList<>();
        dataSemanaEditTexts = new ArrayList<>();
    }



    @NonNull
    @Override
    public MateriaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meio, parent, false);
        return new MateriaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MateriaViewHolder holder, int position) {
        Materia materia = listaDeMaterias.get(position);

        nomeMateriaEditTexts.add((TextInputEditText) holder.nomeMateriaTextView);
        dataSemanaEditTexts.add((TextInputEditText) holder.diaSemanaTextView);

        nomeMateriaEditTexts = new ArrayList<>(new HashSet<>(nomeMateriaEditTexts));
        dataSemanaEditTexts = new ArrayList<>(new HashSet<>(dataSemanaEditTexts));
    }


    public List<TextInputEditText> getNomeMateriaEditTexts() {
        return nomeMateriaEditTexts;
    }

    public List<TextInputEditText> getDataSemanaEditTexts() {
        return dataSemanaEditTexts;
    }

    @Override
    public int getItemCount() {
        return listaDeMaterias.size();
    }

    public class MateriaViewHolder extends RecyclerView.ViewHolder {
        public TextView nomeMateriaTextView;
        public TextView diaSemanaTextView;

        public MateriaViewHolder(@NonNull View itemView) {
            super(itemView);
            nomeMateriaTextView = itemView.findViewById(R.id.textViewNomeMateria);
            diaSemanaTextView = itemView.findViewById(R.id.textViewDataMateria);

            // Adicione mais elementos do layout conforme necessário
        }

    }

}

