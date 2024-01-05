package com.example.appjavavidaacademica;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class AdicionarMateriaAdapter extends RecyclerView.Adapter<AdicionarMateriaAdapter.MateriaViewHolder> {
    private List<Materia> listaDeMaterias;
    private List<TextInputEditText> nomeMateriaEditTexts;
    private List<Spinner> spinnerDataMateriaList;
    private List<TextInputEditText> quantAulasEditTexts;


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
        for (TextInputEditText editText : nomeMateriaEditTexts) {
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
        spinnerDataMateriaList.add(holder.spinnerDataMateria);
        quantAulasEditTexts.add((TextInputEditText) holder.quantAulaTextView);

        nomeMateriaEditTexts = new ArrayList<>(new HashSet<>(nomeMateriaEditTexts));
        spinnerDataMateriaList = new ArrayList<>(new HashSet<>(spinnerDataMateriaList));
        quantAulasEditTexts = new ArrayList<>(new HashSet<>(quantAulasEditTexts));

        List<String> listaDeDatas = Arrays.asList("Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado", "Domingo");
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(holder.itemView.getContext(), R.layout.spinner_item, listaDeDatas);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinnerDataMateria.setAdapter(spinnerAdapter);

    }


    public List<TextInputEditText> getNomeMateriaEditTexts() {
        return nomeMateriaEditTexts;
    }

    public List<Spinner> getSpinnerDataMateriaList() {  // Alteração aqui
        return spinnerDataMateriaList;
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

        public TextView quantAulaTextView;

        public Spinner spinnerDataMateria;


        public MateriaViewHolder(@NonNull View itemView) {
            super(itemView);
            nomeMateriaTextView = itemView.findViewById(R.id.textViewNomeMateria);
            spinnerDataMateria = itemView.findViewById(R.id.spinnerDataMateria);
            quantAulaTextView = itemView.findViewById(R.id.textViewQuantAulas);
        }


    }

}

