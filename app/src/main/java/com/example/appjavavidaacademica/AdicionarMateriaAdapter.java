package com.example.appjavavidaacademica;

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

public class AdicionarMateriaAdapter extends RecyclerView.Adapter<AdicionarMateriaAdapter.MateriaViewHolder> {
    private List<Materia> listaDeMaterias;
    private List<TextInputEditText> nomeMateriaEditTexts;
    private List<TextInputEditText> dataSemanaEditTexts;
    private List<TextInputEditText> quantAulasEditTexts;


    public AdicionarMateriaAdapter(List<Materia> listaDeMaterias) {
        this.listaDeMaterias = listaDeMaterias;
        nomeMateriaEditTexts = new ArrayList<>();
        dataSemanaEditTexts = new ArrayList<>();
        quantAulasEditTexts = new ArrayList<>();
    }

    public void atualizarListaDeMaterias(List<Materia> novaLista) {
        listaDeMaterias.clear();
        listaDeMaterias.addAll(novaLista);
        notifyDataSetChanged();
    }


    public void limparMaterias() {
        listaDeMaterias.clear();
        notifyDataSetChanged();
        for (TextInputEditText editText : nomeMateriaEditTexts) {
            editText.setText("");
        }

        for (TextInputEditText editText : dataSemanaEditTexts) {
            editText.setText("");
        }

        for (TextInputEditText editText : quantAulasEditTexts) {
            editText.setText("");
        }
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
        quantAulasEditTexts.add((TextInputEditText) holder.quantAulaTextView);

        nomeMateriaEditTexts = new ArrayList<>(new HashSet<>(nomeMateriaEditTexts));
        dataSemanaEditTexts = new ArrayList<>(new HashSet<>(dataSemanaEditTexts));
        quantAulasEditTexts = new ArrayList<>(new HashSet<>(quantAulasEditTexts));

    }


    public List<TextInputEditText> getNomeMateriaEditTexts() {
        return nomeMateriaEditTexts;
    }

    public List<TextInputEditText> getDataSemanaEditTexts() {
        return dataSemanaEditTexts;
    }
    public List<TextInputEditText> getQuantAulasEditTexts() {
        return quantAulasEditTexts;
    }


    @Override
    public int getItemCount() {
        return listaDeMaterias.size();
    }

    public class MateriaViewHolder extends RecyclerView.ViewHolder {
        public TextView nomeMateriaTextView;
        public TextView diaSemanaTextView;

        public TextView quantAulaTextView;

        public MateriaViewHolder(@NonNull View itemView) {
            super(itemView);
            nomeMateriaTextView = itemView.findViewById(R.id.textViewNomeMateria);
            diaSemanaTextView = itemView.findViewById(R.id.textViewDataMateria);
            quantAulaTextView = itemView.findViewById(R.id.textViewQuantAulas);

            // Adicione mais elementos do layout conforme necessário
        }


    }

}

