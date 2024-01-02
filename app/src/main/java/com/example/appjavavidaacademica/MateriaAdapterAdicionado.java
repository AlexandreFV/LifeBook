package com.example.appjavavidaacademica;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MateriaAdapterAdicionado extends RecyclerView.Adapter<MateriaAdapterAdicionado.MateriaViewHolderAdicionado> {
    private List<Materia> listaDeMaterias;
    private RecyclerView recyclerView;
    public MateriaAdapterAdicionado(List<Materia> listaDeMaterias) {
        this.listaDeMaterias = listaDeMaterias;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public MateriaViewHolderAdicionado onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_adicionado_materia, parent, false);
        return new MateriaViewHolderAdicionado(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MateriaViewHolderAdicionado holder, int position) {
        Materia materia = listaDeMaterias.get(position);

        // Configurar o ViewHolder com os dados da Materia
        holder.nomeMateriaTextView.setText(materia.getNomeMateria());
        holder.diaSemanaTextView.setText(materia.getDataMateria());
        holder.quantAulaTextView.setText(materia.getQuantAulas());
    }

    @Override
    public int getItemCount() {
        return listaDeMaterias.size();
    }

    public class MateriaViewHolderAdicionado extends RecyclerView.ViewHolder {
        public TextView nomeMateriaTextView;
        public TextView diaSemanaTextView;
        public TextView quantAulaTextView;


        public MateriaViewHolderAdicionado(@NonNull View itemView) {
            super(itemView);
            nomeMateriaTextView = itemView.findViewById(R.id.NomeMateriaAdicionada);
            diaSemanaTextView = itemView.findViewById(R.id.DataMateriaAdicionada);
            quantAulaTextView = itemView.findViewById(R.id.QuantAulaAdicionada);
            // Adicione mais elementos do layout conforme necessário

            nomeMateriaTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toggleVisibility(diaSemanaTextView);
                    toggleVisibility(quantAulaTextView);
                }
            });
        }

        public void bind(Materia materia) {
            nomeMateriaTextView.setText(materia.getNomeMateria());
            diaSemanaTextView.setText(materia.getDataMateria());
            quantAulaTextView.setText(materia.getQuantAulas());

            // Inicialmente, torne as informações invisíveis
            diaSemanaTextView.setVisibility(View.GONE);
            quantAulaTextView.setVisibility(View.GONE);
        }
    }

        public void onMateriaClick(View view) {
            int position = ((RecyclerView) view.getParent()).getChildAdapterPosition(view);
            MateriaViewHolderAdicionado holder = (MateriaViewHolderAdicionado) recyclerView.findViewHolderForAdapterPosition(position);

            // Alternar a visibilidade das informações adicionais
            toggleVisibility(holder.diaSemanaTextView);
            toggleVisibility(holder.quantAulaTextView);
        }

        private void toggleVisibility(View view) {
            if (view.getVisibility() == View.VISIBLE) {
                view.setVisibility(View.GONE);
            } else {
                view.setVisibility(View.VISIBLE);
            }
        }
    }



