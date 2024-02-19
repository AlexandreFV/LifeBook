package com.example.appjavavidaacademica;

import android.content.Context;
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

    }

    @Override
    public int getItemCount() {
        return lembretes.size();
    }

    public class LembreteViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewNomeMateria;
        private TextView textViewCategoria;
        private TextView textViewDescricao;

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
            /*
            textViewData = itemView.findViewById(R.id.DataAdicionadoLembrete);
             */

            pontosIcon = itemView.findViewById(R.id.pontosIcon);
            ButtonFalteiMateria = itemView.findViewById(R.id.ButtonFalteiMateria);
            fundoBtnDelete = itemView.findViewById(R.id.fundoBtnDelete);
            fundoEdit = itemView.findViewById(R.id.fundoEdit);
            lixeriaIconFundoDelete = itemView.findViewById(R.id.lixeriaIconFundoDelete);
            EditarMateria = itemView.findViewById(R.id.EditarMateria);
            btnFalteiMateriaAdicionada = itemView.findViewById(R.id.btnFalteiMateriaAdicionada);
            btnCloseAdicionarFalta = itemView.findViewById(R.id.btnCloseAdicionarFalta);

            textViewNomeMateria.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toggleVisibility(textViewCategoria);
                    toggleVisibility(textViewDescricao);
                    toggleVisibility(pontosIcon);


                    if (fundoEdit.getVisibility() == View.VISIBLE) {
                        toggleVisibility(fundoEdit); // Substitua "icon1" pelo ID do primeiro ícone
                        toggleVisibility(fundoBtnDelete); // Substitua "icon2" pelo ID do segundo ícone
                        toggleVisibility(ButtonFalteiMateria); // Substitua "icon3" pelo ID do terceiro ícone

                        toggleVisibility(lixeriaIconFundoDelete); // Substitua "icon3" pelo ID do terceiro ícone
                        toggleVisibility(EditarMateria); // Substitua "icon3" pelo ID do terceiro ícone
                        toggleVisibility(btnFalteiMateriaAdicionada); // Substitua "icon3" pelo ID do terceiro ícone
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
                    toggleVisibility(ButtonFalteiMateria); // Substitua "icon3" pelo ID do terceiro ícone

                    toggleVisibility(lixeriaIconFundoDelete); // Substitua "icon3" pelo ID do terceiro ícone
                    toggleVisibility(EditarMateria); // Substitua "icon3" pelo ID do terceiro ícone
                    toggleVisibility(btnFalteiMateriaAdicionada); // Substitua "icon3" pelo ID do terceiro ícone


                }
            });
        }

        public void bind(Lembrete lembrete) {
            textViewNomeMateria.setText(lembrete.getNomeMateria());
            textViewCategoria.setText(lembrete.getCategoria());
            textViewDescricao.setText(lembrete.getDescricao());
            /*
            textViewData.setText(lembrete.getData());
             */
        }
    }

    private void toggleVisibility(View view) {
        if (view.getVisibility() == View.VISIBLE) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
        }

    }
}
