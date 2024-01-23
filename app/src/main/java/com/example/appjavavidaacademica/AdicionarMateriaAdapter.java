package com.example.appjavavidaacademica;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AdicionarMateriaAdapter extends RecyclerView.Adapter<AdicionarMateriaAdapter.MateriaViewHolder> {
    private List<Materia> listaDeMaterias;
    private List<TextView> nomeMateriaEditTexts;
    private List<TextView> spinnerDataMateriaList;
    private List<TextView> quantAulasEditTexts;


    public AdicionarMateriaAdapter(List<Materia> listaDeMaterias) {
        this.listaDeMaterias = listaDeMaterias;
        nomeMateriaEditTexts = new ArrayList<>();
        spinnerDataMateriaList = new ArrayList<>();
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
        for (TextView editText : nomeMateriaEditTexts) {
            editText.setText("");
        }

        for (TextView editText : quantAulasEditTexts) {
            editText.setText("");
        }
    }

    public void adicionarMateria(Materia materia) {
        listaDeMaterias.add(materia);
        notifyItemInserted(listaDeMaterias.size() - 1);
    }

    public List<Materia> getListaDeMaterias() {
        return listaDeMaterias;
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

        holder.nomeMateriaTextView.setText(materia.getNomeMateria());  // Substitua getNome() pelo método correto para obter o nome da matéria
        holder.quantAulaTextView.setText(String.valueOf(materia.getQuantAulas()));  // Substitua getQuantAula() pelo método correto para obter a quantidade de aulas
        holder.spinnerDataMateria.setText(materia.getDataMateria());


    }


    public List<TextView> getNomeMateriaEditTexts() {
        return nomeMateriaEditTexts;
    }

    public List<TextView> getSpinnerDataMateriaList() {  // Alteração aqui
        return spinnerDataMateriaList;
    }

    public List<TextView> getQuantAulasEditTexts() {
        return quantAulasEditTexts;
    }


    @Override
    public int getItemCount() {
        return listaDeMaterias.size();
    }

    public class MateriaViewHolder extends RecyclerView.ViewHolder {
        public TextView nomeMateriaTextView;

        public TextView quantAulaTextView;

        public TextView spinnerDataMateria;


        public MateriaViewHolder(@NonNull View itemView) {
            super(itemView);
            nomeMateriaTextView = itemView.findViewById(R.id.textViewNomeMateria);
            spinnerDataMateria = itemView.findViewById(R.id.spinnerDataMateria);
            quantAulaTextView = itemView.findViewById(R.id.textViewQuantAulas);
        }


    }

}
