package com.example.appjavavidaacademica;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.database.sqlite.SQLiteDatabase;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MateriaAdapterAdicionado extends RecyclerView.Adapter<MateriaAdapterAdicionado.MateriaViewHolderAdicionado> {
    private List<Materia> listaDeMaterias;
    private RecyclerView recyclerView;

    private DetalhesAgrupamento detalhesAgrupamento; // Adicione esta linha



    public MateriaAdapterAdicionado(List<Materia> listaDeMaterias, DetalhesAgrupamento detalhesAgrupamento) {
        this.listaDeMaterias = listaDeMaterias;
        this.detalhesAgrupamento = detalhesAgrupamento; // Adicione esta linha

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
        }


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

        }


        public void showFullscreenDialog(String nomeMateria,int quantidadeAulas) {
            // Inflar o layout do modal
            View dialogView = LayoutInflater.from(itemView.getContext()).inflate(R.layout.modal_adicionar_falta, null);

            // Criar o Dialog
            final Dialog fullscreenDialog = new Dialog(itemView.getContext(), android.R.style.Theme_Translucent_NoTitleBar);
            fullscreenDialog.setContentView(dialogView);

            // Configurar para cobrir toda a tela
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(fullscreenDialog.getWindow().getAttributes());
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;

            fullscreenDialog.getWindow().setAttributes(layoutParams);
            TextView nomeMateriaFalta = dialogView.findViewById(R.id.NomeDaMateriaFalta);
            nomeMateriaFalta.setText(nomeMateria);

            // Encontrar e configurar o botão de fechar
            ImageView btnCloseAdicionarFalta = dialogView.findViewById(R.id.btnCloseAdicionarFalta);
            btnCloseAdicionarFalta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Fechar o Dialog ao clicar no botão de fechar
                    fullscreenDialog.dismiss();
                }
            });

            Spinner spinnerFaltas = dialogView.findViewById(R.id.ButtonFaltas);
            configureSpinner(spinnerFaltas, quantidadeAulas);

            inputDataFalta = dialogView.findViewById(R.id.InputDataFalta);
            configureDefaultDate(inputDataFalta);

            // Exibir o Dialog
            fullscreenDialog.show();

            Button AdicionarFaltaInsert = dialogView.findViewById(R.id.AdicionarFaltaInsert);
            AdicionarFaltaInsert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextInputEditText inputDataFalta = dialogView.findViewById(R.id.InputDataFalta); // Substitua com seu ID real
                    Spinner spinnerFaltas = dialogView.findViewById(R.id.ButtonFaltas); // Substitua com seu ID real
                    EditText editTextDescricao = dialogView.findViewById(R.id.editTextDescricao); // Substitua com seu ID real

                    String dataFalta = inputDataFalta.getText().toString(); // Obtendo a data do TextInputEditText
                    int quantidadeFaltas = Integer.parseInt(spinnerFaltas.getSelectedItem().toString()); // Obtendo a quantidade do Spinner
                    String motivoFalta = editTextDescricao.getText().toString(); // Obtendo o motivo do EditText

                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION) {
                        Materia materia = listaDeMaterias.get(position);

                        // Chame a função para inserir falta, passando os valores
                        inserirFalta(dataFalta, materia.getId(), quantidadeFaltas, motivoFalta);
                    }
                    fullscreenDialog.dismiss();

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


    }



