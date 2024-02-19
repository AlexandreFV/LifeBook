package com.example.appjavavidaacademica;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MateriaAdapterAdicionado extends RecyclerView.Adapter<MateriaAdapterAdicionado.MateriaViewHolderAdicionado> {
    private List<Materia> listaDeMaterias;
    private RecyclerView recyclerView;

    private DetalhesAgrupamento detalhesAgrupamento; // Adicione esta linha

    private List<Materia> listaDeMateriasOriginal;  // Adicione esta linha


    public MateriaAdapterAdicionado(List<Materia> listaDeMaterias, DetalhesAgrupamento detalhesAgrupamento) {
        this.listaDeMaterias = listaDeMaterias;
        this.detalhesAgrupamento = detalhesAgrupamento; // Adicione esta linha
        this.listaDeMateriasOriginal = new ArrayList<>(listaDeMaterias);  // Adicione esta linha


    }

    public void setMaterias(List<Materia> materias) {
        this.listaDeMaterias.addAll(materias);
        notifyDataSetChanged();
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

        if (materia.getQuantAulas() != null && !materia.getQuantAulas().isEmpty()) {
            String quantAulasTexto = materia.getQuantAulas() + " aula" + (materia.getQuantAulas().equals("1") ? "" : "s") + " por dia";
            holder.quantAulaTextView.setText(quantAulasTexto);
            holder.quantAulaTextView.setVisibility(View.GONE);
            holder.pontosIcon.setVisibility(View.GONE);
        }



    }

    public void filtrarDados(String diaSemanaSelecionado, String materiaNomeSelecionada, String quantAulasSelecionada) {
        List<Materia> listaFiltrada = new ArrayList<>();

        // Verificar se o nome do card foi selecionado
        boolean filtrarPorDiaSem = diaSemanaSelecionado != null && !"Todos".equals(diaSemanaSelecionado) && !diaSemanaSelecionado.isEmpty();

        // Verificar se a quantidade de faltas foi selecionada
        boolean filtrarPorNomeMat = materiaNomeSelecionada != null && !"Todos".equals(materiaNomeSelecionada) && !materiaNomeSelecionada.isEmpty();

        // Verificar se o nome da matéria foi selecionado
        boolean filtrarPorQuantAulas = quantAulasSelecionada != null && !"Todos".equals(quantAulasSelecionada) && !quantAulasSelecionada.isEmpty();

        // Usar a listaDeFaltasOriginal em vez de listaDeFaltas
        List<Materia> listaOriginal = new ArrayList<>(listaDeMateriasOriginal);

        // Aplicar o filtro na lista original
        for (Materia materia : listaOriginal) {
            boolean atendeFiltroDiaSem = !filtrarPorDiaSem || materia.getDia_semana().equals(diaSemanaSelecionado);
            boolean atendeFiltroNomeMat = !filtrarPorNomeMat || materia.getNomeMateria().equals(materiaNomeSelecionada);
            boolean atendeFiltroQuantAulas = !filtrarPorQuantAulas || materia.getQuantAulas().equals(quantAulasSelecionada);

            // Adicione a condição para verificar se atende ao filtro de nome da matéria
            if (atendeFiltroDiaSem && atendeFiltroNomeMat && atendeFiltroQuantAulas) {
                listaFiltrada.add(materia);
            }
        }
        // Atualizar a lista exibida no RecyclerView
        listaDeMaterias.clear();
        listaDeMaterias.addAll(listaFiltrada);
        notifyDataSetChanged();

    }


    @Override
    public int getItemCount() {
        return listaDeMaterias.size();
    }

    public class MateriaViewHolderAdicionado extends RecyclerView.ViewHolder {
        public TextView nomeMateriaTextView;
        public TextView diaSemanaTextView;
        public TextView quantAulaTextView;

        public ImageView pontosIcon;

        public View ButtonFalteiMateria;

        public View fundoBtnDelete;
        public View fundoEdit;

        public ImageView lixeriaIconFundoDelete;

        public ImageView EditarMateria;

        public TextView btnFalteiMateriaAdicionada;

        public ImageView btnCloseAdicionarFalta;

        public TextInputEditText inputDataFalta;

        public MateriaViewHolderAdicionado(@NonNull View itemView) {
            super(itemView);
            nomeMateriaTextView = itemView.findViewById(R.id.NomeMateriaAdicionada);
            diaSemanaTextView = itemView.findViewById(R.id.DataMateriaAdicionada);
            quantAulaTextView = itemView.findViewById(R.id.QuantAulaAdicionada);
            pontosIcon = itemView.findViewById(R.id.pontosIcon);
            ButtonFalteiMateria = itemView.findViewById(R.id.ButtonFalteiMateria);
            fundoBtnDelete = itemView.findViewById(R.id.fundoBtnDelete);
            fundoEdit = itemView.findViewById(R.id.fundoEdit);
            lixeriaIconFundoDelete = itemView.findViewById(R.id.lixeriaIconFundoDelete);
            EditarMateria = itemView.findViewById(R.id.EditarMateria);
            btnFalteiMateriaAdicionada = itemView.findViewById(R.id.btnFalteiMateriaAdicionada);
            btnCloseAdicionarFalta = itemView.findViewById(R.id.btnCloseAdicionarFalta);

            nomeMateriaTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toggleVisibility(diaSemanaTextView);
                    toggleVisibility(quantAulaTextView);
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

            ButtonFalteiMateria.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Obter a posição do item no RecyclerView
                    int position = getAdapterPosition();

                    // Certificar-se de que a posição é válida
                    if (position != RecyclerView.NO_POSITION) {
                        // Obter o objeto Materia correspondente à posição
                        Materia materia = listaDeMaterias.get(position);

                        // Chamar a função showFullscreenDialog e passar o nome da matéria
                            showFullscreenDialog(materia.getNomeMateria(), convertStringToInt(materia.getQuantAulas()));
                    }
                }
            });

            fundoBtnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // Obter a posição do item no RecyclerView
                    int position = getAdapterPosition();

                    // Certificar-se de que a posição é válida
                    if (position != RecyclerView.NO_POSITION) {
                        // Obter o objeto Materia correspondente à posição
                        Materia materia = listaDeMaterias.get(position);

                        // Chamar a função showFullscreenDialog e passar o nome da matéria
                        showDialogExcluirMateria(itemView.getContext(), position);
                    }
                }
            });

            fundoEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    // Certificar-se de que a posição é válida
                    if (position != RecyclerView.NO_POSITION) {
                        // Obter o objeto Materia correspondente à posição
                        Materia materia = listaDeMaterias.get(position);

                        // Criar o diálogo de edição
                        final Dialog dialog = new Dialog(fundoEdit.getContext(), android.R.style.Theme_Translucent_NoTitleBar);
                        dialog.setContentView(R.layout.dialog_edit_materia);

                        ImageView buttonCancelar = dialog.findViewById(R.id.buttonCancelarCard);

                        buttonCancelar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Fecha o Dialog
                                dialog.dismiss();
                            }
                        });

                        dialog.show();
                        // Exibir o diálogo de edição
                        showEditarMateria(dialog, materia);
                    }
                }
            });
        }

        public void showDialogExcluirMateria(Context context, int position) {
            View dialogView = LayoutInflater.from(context).inflate(R.layout.layout_excluir_materia, null);

            TextView TextExcluirFalta = dialogView.findViewById(R.id.TextDetalhesFalta);
            View buttonCancelar = dialogView.findViewById(R.id.buttonCancelarCard);
            View fundoDialog = dialogView.findViewById(R.id.fundoDialog);
            TextView TextDesejaFalta = dialogView.findViewById(R.id.TextDesejaCard);
            TextView TextNomeMateriaVariavel = dialogView.findViewById(R.id.TextNomeCardVariavel);
            View buttonRemover = dialogView.findViewById(R.id.buttonAdicionar);

            Materia materia = listaDeMaterias.get(position);
            String nomeMateria = materia.getNomeMateria();

            TextNomeMateriaVariavel.setText(nomeMateria);

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





        public void showFullscreenDialog(String nomeMateria,int quantidadeAulas) {
            // Inflar o layout do modal
            View dialogView1 = LayoutInflater.from(itemView.getContext()).inflate(R.layout.modal_adicionar_falta, null);

            // Criar o Dialog
            final Dialog fullscreenDialog1 = new Dialog(itemView.getContext(), android.R.style.Theme_Translucent_NoTitleBar);
            fullscreenDialog1.setContentView(dialogView1);

            // Configurar para cobrir toda a tela
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(fullscreenDialog1.getWindow().getAttributes());
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;

            fullscreenDialog1.getWindow().setAttributes(layoutParams);
            TextView nomeMateriaFalta = dialogView1.findViewById(R.id.NomeDaMateriaFalta);
            nomeMateriaFalta.setText(nomeMateria);

            // Encontrar e configurar o botão de fechar
            ImageView btnCloseAdicionarFalta = dialogView1.findViewById(R.id.btnCloseAdicionarFalta);
            btnCloseAdicionarFalta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Fechar o Dialog ao clicar no botão de fechar
                    fullscreenDialog1.dismiss();
                }
            });

            Spinner spinnerFaltas = dialogView1.findViewById(R.id.ButtonFaltas);
            configureSpinner(spinnerFaltas, quantidadeAulas);

            inputDataFalta = dialogView1.findViewById(R.id.InputDataFalta);
            configureDefaultDate(inputDataFalta);

            // Exibir o Dialog
            fullscreenDialog1.show();

            Button AdicionarFaltaInsert = dialogView1.findViewById(R.id.AdicionarFaltaInsert);
            AdicionarFaltaInsert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextInputEditText inputDataFalta = dialogView1.findViewById(R.id.InputDataFalta); // Substitua com seu ID real
                    Spinner spinnerFaltas = dialogView1.findViewById(R.id.ButtonFaltas); // Substitua com seu ID real
                    EditText editTextDescricao = dialogView1.findViewById(R.id.editTextDescricao); // Substitua com seu ID real

                    String dataFalta = inputDataFalta.getText().toString(); // Obtendo a data do TextInputEditText
                    int quantidadeFaltas = Integer.parseInt(spinnerFaltas.getSelectedItem().toString()); // Obtendo a quantidade do Spinner
                    String motivoFalta = editTextDescricao.getText().toString(); // Obtendo o motivo do EditText

                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION) {
                        Materia materia = listaDeMaterias.get(position);

                        // Chame a função para inserir falta, passando os valores
                        inserirFalta(dataFalta, materia.getId(), quantidadeFaltas, motivoFalta);
                    }
                    fullscreenDialog1.dismiss();

                }
            });
        }

        private void showEditarMateria(final Dialog dialog3,final Materia materia) {


            // Obter referências para os EditTexts e Spinner no layout do diálogo
            EditText editTextNomeMateria = dialog3.findViewById(R.id.textViewNomeMateria);
            EditText editTextQuantAula = dialog3.findViewById(R.id.textViewQuantAulas);
            Spinner spinnerDiaSemana = dialog3.findViewById(R.id.spinnerDataMateria);

            // Configurar o adaptador para o Spinner com a lista de dias da semana
            List<String> listaDeDiasSemana = Arrays.asList("Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado", "Domingo");
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(detalhesAgrupamento.getApplicationContext(), R.layout.spinner_preto, listaDeDiasSemana);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerDiaSemana.setAdapter(spinnerAdapter);

            editTextNomeMateria.setText(materia.getNomeMateria());
            editTextQuantAula.setText(String.valueOf(materia.getQuantAulas()));
            spinnerDiaSemana.setSelection(listaDeDiasSemana.indexOf(materia.getDataMateria()));


            View buttonSalvarMateria = dialog3.findViewById(R.id.buttonAdicionar);
            buttonSalvarMateria.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String novoNomeMateria = editTextNomeMateria.getText().toString();
                    String novaQuantAula = editTextQuantAula.getText().toString();
                    String novoDiaSemana = spinnerDiaSemana.getSelectedItem().toString();


                    atualizarDadosMateria(materia.getId(), novoNomeMateria, novoDiaSemana, Integer.parseInt(novaQuantAula));
                    dialog3.dismiss();



                }
            });
        }



        private int convertStringToInt(String value) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                e.printStackTrace(); // Tratar erro de conversão (por exemplo, logar ou lançar exceção)
                return 0; // Valor padrão em caso de falha na conversão
            }
        }


        private void configureDefaultDate(TextInputEditText editText) {
            // Obter a data atual
            Calendar calendar = Calendar.getInstance();
            Date currentDate = calendar.getTime();

            // Formatando a data para o formato desejado (dd/MM/yyyy)
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String formattedDate = dateFormat.format(currentDate);

            // Definir o valor padrão no TextInputEditText
            editText.setText(formattedDate);
        }


        private void configureSpinner(Spinner spinner, int quantidadeAulas) {
            // Crie um array de strings com as opções de 1 a quantidadeAulas
            String[] opcoes = new String[quantidadeAulas];
            for (int i = 0; i < quantidadeAulas; i++) {
                opcoes[i] = String.valueOf(i + 1);
            }

            // Crie um ArrayAdapter usando o array de opções e o layout padrão do Spinner
            ArrayAdapter<String> adapter = new ArrayAdapter<>(itemView.getContext(), android.R.layout.simple_spinner_item, opcoes);

            // Configure o layout para as opções do Spinner
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // Atribua o ArrayAdapter ao Spinner
            spinner.setAdapter(adapter);
        }

        private void deleteItem(int position) {
            // Implemente a lógica de exclusão aqui
            listaDeMaterias.remove(position);
            notifyItemRemoved(position);
        }


        private long inserirFalta(String data, int idMateria, int quantidade, String motivo) {
            long resultadoInsercao = -1; // Valor padrão caso a inserção falhe

            try {
                DatabaseHelper dbHelper = new DatabaseHelper(itemView.getContext());
                SQLiteDatabase bancoDados = dbHelper.getWritableDatabase();

                ContentValues values = new ContentValues();
                values.put("data", data);
                values.put("id_materia", idMateria);
                values.put("quantidade", quantidade);
                values.put("motivo", motivo);

                resultadoInsercao = bancoDados.insert("faltas", null, values);
                Log.d("TAG","Data: " + data);
                Log.d("TAG","id_materia: " + idMateria);
                Log.d("TAG","quantidade: " + quantidade);
                Log.d("TAG","motivo: " + motivo);

                bancoDados.close();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("TAG", "Erro ao inserir dados na tabela faltas: " + e.getMessage());
            }

            detalhesAgrupamento.exibirMensagemComAnimacaoDetalhesCard();
            return resultadoInsercao;
        }




        public void bind(Materia materia) {
            nomeMateriaTextView.setText(materia.getNomeMateria());
            diaSemanaTextView.setText(materia.getDataMateria());

            if (materia.getQuantAulas() != null && !materia.getQuantAulas().isEmpty()) {
                String quantAulasTexto = materia.getQuantAulas() + " aula" + (materia.getQuantAulas().equals("1") ? "" : "s") + " por dia";
                quantAulaTextView.setText(quantAulasTexto);
                quantAulaTextView.setVisibility(View.GONE);
            } else {
                quantAulaTextView.setVisibility(View.GONE);
            }

            // Inicialmente, torne as informações invisíveis
            diaSemanaTextView.setVisibility(View.GONE);
            pontosIcon.setVisibility(View.GONE);
        }

    }

        public void onMateriaClick(View view) {
            int position = ((RecyclerView) view.getParent()).getChildAdapterPosition(view);
            MateriaViewHolderAdicionado holder = (MateriaViewHolderAdicionado) recyclerView.findViewHolderForAdapterPosition(position);

            // Alternar a visibilidade das informações adicionais
            toggleVisibility(holder.diaSemanaTextView);
            toggleVisibility(holder.quantAulaTextView);
            toggleVisibility(holder.pontosIcon);

        }

        private void toggleVisibility(View view) {
            if (view.getVisibility() == View.VISIBLE) {
                view.setVisibility(View.GONE);
            } else {
                view.setVisibility(View.VISIBLE);
            }

        }

    private void excluirMateriaDoBanco(int idMateria, int position) {
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(detalhesAgrupamento.getApplicationContext()); // Substitua mContext pelo contexto adequado
            SQLiteDatabase bancoDados = dbHelper.getWritableDatabase();

            // Excluir o registro com base no ID
            bancoDados.delete("materias", "id_materia=?", new String[]{String.valueOf(idMateria)});

            bancoDados.close();

            // Remover o item da lista
            listaDeMaterias.remove(position);

            // Notificar o adaptador sobre a alteração nos dados
            notifyItemRemoved(position);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TAG", "Erro ao excluir dados na tabela materias: " + e.getMessage());
        }
    }

    private void excluirFaltasPorIdMateria(int idMateria) {
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(detalhesAgrupamento.getApplicationContext()); // Substitua mContext pelo contexto adequado
            SQLiteDatabase bancoDados = dbHelper.getWritableDatabase();

            // Excluir registros da tabela 'faltas' onde id_tal = id_materia
            bancoDados.delete("faltas", "id_materia=?", new String[]{String.valueOf(idMateria)});

            bancoDados.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TAG", "Erro ao excluir dados na tabela faltas: " + e.getMessage());
        }
    }

            private void atualizarDadosMateria(int idMateria, String novoNomeMateria, String novoDiaSemana, int novaQuantAula) {
            try {
                DatabaseHelper dbHelper = new DatabaseHelper(detalhesAgrupamento.getApplicationContext()); // Substitua mContext pelo contexto adequado
                SQLiteDatabase bancoDados = dbHelper.getWritableDatabase();
                int position = encontrarPosicaoDaMateria(idMateria);

                // Atualizar o registro na tabela 'materias'
                ContentValues values = new ContentValues();
                values.put("nome_materia", novoNomeMateria);
                values.put("dia_semana", novoDiaSemana);
                values.put("quantAulas", novaQuantAula);

                bancoDados.update("materias", values, "id_materia=?", new String[]{String.valueOf(idMateria)});

                if (position != RecyclerView.NO_POSITION) {
                    // Atualize os dados da matéria na lista
                    Materia materia = listaDeMaterias.get(position);
                    materia.setNomeMateria(novoNomeMateria);
                    materia.setDia_semana(novoDiaSemana);
                    materia.setQuantAulas(String.valueOf(novaQuantAula)); // Convertendo para String aqui

                    // Notifique o adapter sobre a alteração na posição específica
                    notifyItemChanged(position);

                    // Chame a função toggleVisibility para os componentes desejados
                    MateriaViewHolderAdicionado holder = (MateriaViewHolderAdicionado) recyclerView.findViewHolderForAdapterPosition(position);

                    toggleVisibility(holder.diaSemanaTextView);
                    toggleVisibility(holder.quantAulaTextView);
                    toggleVisibility(holder.pontosIcon);
                    toggleVisibility(holder.pontosIcon);

                    toggleVisibility(holder.fundoEdit); // Substitua "icon1" pelo ID do primeiro ícone
                    toggleVisibility(holder.fundoBtnDelete); // Substitua "icon2" pelo ID do segundo ícone
                    toggleVisibility(holder.ButtonFalteiMateria); // Substitua "icon3" pelo ID do terceiro ícone

                    toggleVisibility(holder.lixeriaIconFundoDelete); // Substitua "icon3" pelo ID do terceiro ícone
                    toggleVisibility(holder.EditarMateria); // Substitua "icon3" pelo ID do terceiro ícone
                    toggleVisibility(holder.btnFalteiMateriaAdicionada); // Substitua "icon3" pelo ID do terceiro ícone


                }

                bancoDados.close();


            } catch (Exception e) {
                e.printStackTrace();
                Log.e("TAG", "Erro ao atualizar dados na tabela materias: " + e.getMessage());
            }
        }

    public void atualizarLista(List<Materia> novaLista) {
        listaDeMaterias.clear();
        listaDeMaterias.addAll(novaLista);
        notifyDataSetChanged();
    }


    private int encontrarPosicaoDaMateria(int idMateria) {
        // Percorra a lista de matérias para encontrar a posição da matéria com o ID especificado
        for (int i = 0; i < listaDeMaterias.size(); i++) {
            if (listaDeMaterias.get(i).getId() == idMateria) {
                return i; // Retorne a posição se encontrar a matéria com o ID especificado
            }
        }
        return RecyclerView.NO_POSITION; // Retorne RecyclerView.NO_POSITION se a matéria não for encontrada
    }

    public void resetarListaOriginal() {
        listaDeMaterias.clear();
        listaDeMaterias.addAll(listaDeMateriasOriginal);
        notifyDataSetChanged();
    }

}




