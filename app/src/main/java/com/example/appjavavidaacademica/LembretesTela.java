package com.example.appjavavidaacademica;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.format.MonthArrayTitleFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class LembretesTela extends AppCompatActivity {
    private MaterialCalendarView calendarView;

    private SQLiteDatabase bancoDados;

    private Spinner spinnerMateria;

    private Spinner spinnerCategoria;

    private TextInputEditText textViewInput;

    private DatabaseHelper dbHelper;

    private CheckBox checkBoxNotif;

    private LembreteAdapter lembreteAdapter;

    private RecyclerView recyclerView;

    private String dataAtual;

    private List<String> listaMateria;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lembretes_layout);

        CalendarDay currentDay = CalendarDay.today();

        layoutInferiorBotoes barraInferior = new layoutInferiorBotoes(this, findViewById(R.id.includeDesempenhoInf), findViewById(R.id.AdicioneAlgoScreen));

        calendarView = findViewById(R.id.calendarView);
        calendarView.setWeekDayLabels(new String[]{"S", "T", "Q", "Q", "S","S", "D"});
        calendarView.setTitleFormatter(new MonthArrayTitleFormatter(new String[]{"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"}));
        calendarView.setSelectedDate(currentDay);
        calendarView.setSelectionColor(Color.BLUE);
        calendarView.setHeaderTextAppearance(R.style.MyHeaderTextAppearance);
        calendarView.setWeekDayTextAppearance(R.style.MyHeaderTextAppearance);

        // Criar uma lista para armazenar os nomes das matérias
        listaMateria = new ArrayList<>();

        ImageView imageView11 = findViewById(R.id.imageView11);

        imageView11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogAddLembrete();
            }
        });

        exibirLembretes();

        recyclerView = findViewById(R.id.LembreteRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dataAtual = calendarView.getSelectedDate().toString();
        List<Lembrete> listaDeLembretes = obterLembretesPorData(dataAtual);

        LembreteAdapter lembreteAdapter1 = new LembreteAdapter(this);
        recyclerView.setAdapter(lembreteAdapter1);

        for (Lembrete lembrete : listaDeLembretes) {
            lembreteAdapter1.adicionarLembrete(lembrete);
        }

        // Atribuir um novo adaptador à variável lembreteAdapter
        lembreteAdapter = new LembreteAdapter(this);
        recyclerView.setAdapter(lembreteAdapter);

        List<Lembrete> todosLembretes = obterTodosLembretes();

        List<DayViewDecorator> decorators = new ArrayList<>();

        for (Lembrete lembrete : todosLembretes) {
            decorators.add(new CustomDayDecorator(Collections.singletonList(lembrete)));
        }
        calendarView.addDecorators(decorators);

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                // Atualizar a variável dataAtual com a nova data selecionada
                dataAtual = date.toString();

                // Atualizar o RecyclerView com os lembretes correspondentes a essa data
                List<Lembrete> lembretes = obterLembretesPorData(dataAtual);
                lembreteAdapter.setLembretes(lembretes);
                lembreteAdapter.notifyDataSetChanged();

                // Atualizar os decoradores do calendário
                List<DayViewDecorator> decorators = new ArrayList<>();
                decorators.add(new CustomDayDecorator(lembretes));
                calendarView.addDecorators(decorators);



            }

        });

        ImageView imageViewClose = findViewById(R.id.imageCloseLembrete2);
        imageViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LembretesTela.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });

    }


    private void DialogAddLembrete(){
        View dialogView;
        preencherSpinnerMateria();

        if (listaMateria.isEmpty()) {
            dialogView = getLayoutInflater().inflate(R.layout.layout_npossui_lembrete, null);

            // Criar um Dialog
            final Dialog dialog = new Dialog(this, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
            dialog.setContentView(dialogView);

            TextView TextLembreteExcluir = dialogView.findViewById(R.id.TextLembreteExcluir);
            View fundoDialog = dialog.findViewById(R.id.fundoDialog);
            TextView TextDesejaCard = dialog.findViewById(R.id.TextDesejaCard);
            View linhaMateria = dialog.findViewById(R.id.linhaMateria);


            // Configurar para ocupar toda a tela
            dialog.getWindow().setLayout(
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT
            );

            // Exibir o Dialog
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent); // Define o fundo transparente
            dialog.show();

            ImageView buttonCancelarCard = dialogView.findViewById(R.id.buttonCancelarCard);
            buttonCancelarCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        } else if (!listaMateria.isEmpty()) {

            dialogView = getLayoutInflater().inflate(R.layout.dialog_add_lembrete, null);

            // Criar um Dialog
            final Dialog dialog = new Dialog(this, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
            dialog.setContentView(dialogView);

            checkBoxNotif = dialogView.findViewById(R.id.checkBoxNotif);
            textViewInput = dialog.findViewById(R.id.textViewNomeMateria);
            spinnerMateria = dialog.findViewById(R.id.SpinnerMateria);
            spinnerCategoria = dialog.findViewById(R.id.spinnerLembreteCategoria);
            List<String> Categorias = Arrays.asList("Avaliação","Trabalho","TCC","Reunião");

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Categorias);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCategoria.setAdapter(adapter);
            preencherSpinnerMateria();

            // Configurar para ocupar toda a tela
            dialog.getWindow().setLayout(
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT
            );

            // Exibir o Dialog
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent); // Define o fundo transparente
            dialog.show();

            ImageView buttonCancelarCard = dialogView.findViewById(R.id.buttonCancelarCard);
            buttonCancelarCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            View buttonAdicionar = dialogView.findViewById(R.id.buttonAdicionar);
            buttonAdicionar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String nomeMateria = (String) spinnerMateria.getSelectedItem();
                    String Categoria = (String) spinnerCategoria.getSelectedItem();
                    String Descricao = textViewInput.getText().toString();
                    String Data = calendarView.getSelectedDate().toString(); // Obtém a data selecionada no MaterialCalendarView
                    Boolean notif = checkBoxNotif.isChecked();



                    AdicionarLembrete(nomeMateria,Categoria,Descricao,Data,notif);
                    dialog.dismiss();
                }
            });

        }

    }

    private List<String> preencherSpinnerMateria() {
        try {
            dbHelper = new DatabaseHelper(getApplicationContext());
            bancoDados = dbHelper.getReadableDatabase();

            listaMateria = new ArrayList<>();

            // Consulta à tabela materias para obter os nomes das matérias
            String[] colunasMaterias = {"nome_materia"};
            Cursor cursorMaterias = bancoDados.query("materias", colunasMaterias, null, null, null, null, null);

            // Adicionar os nomes das matérias à lista
            if (cursorMaterias != null && cursorMaterias.moveToFirst()) {
                do {
                    String nomeMateria = cursorMaterias.getString(cursorMaterias.getColumnIndex("nome_materia"));
                    listaMateria.add(nomeMateria);
                } while (cursorMaterias.moveToNext());

                cursorMaterias.close();
            }

            // Criar um ArrayAdapter para o Spinner
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listaMateria);

            // Definir o layout do dropdown do Spinner
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // Definir o ArrayAdapter como o adaptador do Spinner
            spinnerMateria.setAdapter(adapter);

            // Exibir os nomes das matérias no Log
            for (String nomeMateria : listaMateria) {
                Log.d("NomesMaterias", nomeMateria);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TAG", "Erro ao obter nome da matéria: " + e.getMessage());
        }

        return listaMateria;
    }


    private void AdicionarLembrete(String nomeMateria, String categoria, String descricao, String data, Boolean notif) {
        int idLembrete = -1; // Valor padrão caso a inserção falhe

        try {
            dbHelper = new DatabaseHelper(getApplicationContext());
            bancoDados = dbHelper.getReadableDatabase();

            String dataCalendarDay = data;


            // Extrair ano, mês e dia da data CalendarDay
            String[] parts = data.split("-");
            int ano = Integer.parseInt(parts[0].substring(parts[0].length() - 4)); // Extrair os últimos 4 caracteres (ano)
            int mes = Integer.parseInt(parts[1]);
            int dia = Integer.parseInt(parts[2].substring(0, parts[2].length() - 1)); // Remover o último caractere '}'

            // Formate a data para o formato desejado
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            Date date12 = new GregorianCalendar(ano, mes - 1, dia).getTime(); // Mês - 1 porque o mês começa em 0
            String dataFormatada = sdf.format(date12);


            ContentValues values = new ContentValues();
            values.put("nomeMateria", nomeMateria);
            values.put("categoria", categoria);
            values.put("descr", descricao);
            values.put("data", dataFormatada);
            values.put("dataCalendarDay", dataCalendarDay);
            values.put("notif", notif);

            idLembrete = (int) bancoDados.insert("lembretes", null, values);
            bancoDados.close();

            if (idLembrete != -1) {
                // Se o lembrete foi adicionado com sucesso, atualize o RecyclerView
                lembreteAdapter.adicionarLembrete(new Lembrete(idLembrete,data,descricao, nomeMateria, notif,categoria,dataCalendarDay));
                lembreteAdapter.notifyDataSetChanged();
            }

        }catch (Exception e) {
            e.printStackTrace();
            Log.e("TAG", "Erro ao inserir dados na tabela agrupamento: " + e.getMessage());
        }
    }

    private void exibirLembretes() {
        try {
            dbHelper = new DatabaseHelper(this);
            bancoDados = dbHelper.getReadableDatabase();

            // Consulta à tabela lembretes para obter todos os lembretes
            String[] colunasLembretes = {"nomeMateria", "categoria", "descr","data","notif"};
            Cursor cursorLembretes = bancoDados.query("lembretes", colunasLembretes, null, null, null, null, null);

            // Exibir os lembretes no Log
            if (cursorLembretes != null && cursorLembretes.moveToFirst()) {
                do {
                    String nomeMateria = cursorLembretes.getString(cursorLembretes.getColumnIndex("nomeMateria"));
                    String categoria = cursorLembretes.getString(cursorLembretes.getColumnIndex("categoria"));
                    String descricao = cursorLembretes.getString(cursorLembretes.getColumnIndex("descr"));
                    String data = cursorLembretes.getString(cursorLembretes.getColumnIndex("data"));
                    String notif = cursorLembretes.getString(cursorLembretes.getColumnIndex("notif"));

                    Log.d("Lembretes", "Nome da matéria: " + nomeMateria + ", Categoria: " + categoria + ", Descrição: " + descricao + ", Data: " + data + ", Noti: " + notif);

                } while (cursorLembretes.moveToNext());


                cursorLembretes.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TAG", "Erro ao obter lembretes: " + e.getMessage());
        }
    }

    private List<Lembrete> obterLembretesPorData(String data) {
        List<Lembrete> lembretes = new ArrayList<>();

        try {
            dbHelper = new DatabaseHelper(this);
            bancoDados = dbHelper.getReadableDatabase();
            Cursor cursor = bancoDados.rawQuery("SELECT * FROM lembretes WHERE dataCalendarDay = ?", new String[]{data});
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex("id_lembretes"));
                    String nomeMateria = cursor.getString(cursor.getColumnIndex("nomeMateria"));
                    String categoria = cursor.getString(cursor.getColumnIndex("categoria"));
                    String descricao = cursor.getString(cursor.getColumnIndex("descr"));
                    String data2 = cursor.getString(cursor.getColumnIndex("data"));
                    String dataCalendarDay = cursor.getString(cursor.getColumnIndex("dataCalendarDay"));
                    lembretes.add(new Lembrete(id,data2,descricao, nomeMateria,categoria,dataCalendarDay));
                } while (cursor.moveToNext());
            }
            cursor.close();
            bancoDados.close();
        }catch (Exception e) {
            e.printStackTrace();
            Log.e("TAG", "Erro ao obter lembretes por data: " + e.getMessage());
        }
        return lembretes;
    }

    private List<String> obterDatasLembretes() {
        List<String> datas = new ArrayList<>();

        try {
            dbHelper = new DatabaseHelper(this);
            bancoDados = dbHelper.getReadableDatabase();
            Cursor cursor = bancoDados.rawQuery("SELECT dataCalendarDay FROM lembretes", null);
            if (cursor.moveToFirst()) {
                do {
                    String dataLembrete = cursor.getString(cursor.getColumnIndex("dataCalendarDay"));
                    datas.add(dataLembrete);
                } while (cursor.moveToNext());
            }
            cursor.close();
            bancoDados.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TAG", "Erro ao obter datas dos lembretes: " + e.getMessage());
        }
        return datas;
    }


    public void removerTodosDecoradores() {

        calendarView.removeDecorators();

    }

    private List<Lembrete> obterTodosLembretes() {
        List<Lembrete> lembretes = new ArrayList<>();

        try {
            dbHelper = new DatabaseHelper(this);
            bancoDados = dbHelper.getReadableDatabase();
            Cursor cursor = bancoDados.rawQuery("SELECT * FROM lembretes", null);
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex("id_lembretes"));
                    String nomeMateria = cursor.getString(cursor.getColumnIndex("nomeMateria"));
                    String categoria = cursor.getString(cursor.getColumnIndex("categoria"));
                    String descricao = cursor.getString(cursor.getColumnIndex("descr"));
                    String data = cursor.getString(cursor.getColumnIndex("data"));
                    String dataCalendarDay = cursor.getString(cursor.getColumnIndex("dataCalendarDay"));
                    boolean notif = cursor.getInt(cursor.getColumnIndex("notif")) == 1;
                    lembretes.add(new Lembrete(id, data, descricao, nomeMateria, categoria, dataCalendarDay));
                } while (cursor.moveToNext());
            }
            cursor.close();
            bancoDados.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TAG", "Erro ao obter todos os lembretes: " + e.getMessage());
        }
        return lembretes;
    }



}
