package com.example.appjavavidaacademica;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Arrays;
import java.util.List;

public class criarFormulario {

    public static void criarFormulario(Context context, ViewGroup container, List<Materia> materias) {
        container.removeAllViews(); // Limpar contêiner existente antes de adicionar novos itens

        for (Materia materia : materias) {
            // Inflar o layout do item_meio.xml para cada matéria
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_meio, container, false);

            // Referenciar os elementos do layout
            EditText nomeMateriaEditText = itemView.findViewById(R.id.textViewNomeMateria);
            Spinner spinnerDataMateria = itemView.findViewById(R.id.spinnerDataMateria);
            EditText quantAulasEditText = itemView.findViewById(R.id.textViewQuantAulas);

            // Configurar o Spinner com os dados necessários
            List<String> listaDeDatas = Arrays.asList("Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado", "Domingo");
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, listaDeDatas);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerDataMateria.setAdapter(spinnerAdapter);

            // Preencher os elementos com os dados da Materia
            nomeMateriaEditText.setText(materia.getNomeMateria());
            quantAulasEditText.setText(String.valueOf(materia.getQuantAulas()));
            spinnerDataMateria.setSelection(listaDeDatas.indexOf(materia.getDataMateria()));

            // Adicionar o layout do item ao contêiner principal
            container.addView(itemView);
        }
    }
}