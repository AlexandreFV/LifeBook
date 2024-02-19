package com.example.appjavavidaacademica;

import android.app.Dialog;
import android.content.ContentValues;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.format.MonthArrayTitleFormatter;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                // Atualizar a variável dataAtual com a nova data selecionada
                dataAtual = date.toString();

                // Obter os lembretes correspondentes a essa data
                List<Lembrete> lembretes = obterLembretesPorData(dataAtual);

                // Atualizar a lista de lembretes no RecyclerView
                lembreteAdapter1.setLembretes(lembretes);
                lembreteAdapter1.notifyDataSetChanged();



                // Obter todas as datas dos lembretes
                List<String> lembretes1 = obterDatasLembretes();

                // Criar um decorador personalizado para adicionar um ponto vermelho nos dias com lembretes
                DayViewDecorator decorator = new DayViewDecorator() {
                    @Override
                    public boolean shouldDecorate(CalendarDay day) {
                        // Verificar se a data do dia corresponde a uma data de lembrete
                        return lembretes1.contains(day.toString());
                    }

                    @Override
                    public void decorate(DayViewFacade view) {
                        // Adicionar um ponto vermelho ao dia
                        view.addSpan(new DotSpan(5, Color.RED));
                    }
                };

                // Adicionar o decorador ao MaterialCalendarView
                calendarView.addDecorator(decorator);



            }
        });


        // Obter todas as datas dos lembretes
        List<String> lembretes1 = obterDatasLembretes();

        // Criar um decorador personalizado para adicionar um ponto vermelho nos dias com lembretes
        DayViewDecorator decorator = new DayViewDecorator() {
            @Override
            public boolean shouldDecorate(CalendarDay day) {
                // Verificar se a data do dia corresponde a uma data de lembrete
                return lembretes1.contains(day.toString());
            }

            @Override
            public void decorate(DayViewFacade view) {
                // Adicionar um ponto vermelho ao dia
                view.addSpan(new DotSpan(5, Color.RED));
            }
        };

        // Adicionar o decorador ao MaterialCalendarView
        calendarView.addDecorator(decorator);



    }

    private void DialogAddLembrete(){

        // Inflar o layout personalizado
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_lembrete, null);

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

        // Configurar para ocupar toda a tela
        dialog.getWindow().setLayout(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT
        );

        // Exibir o Dialog
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent); // Define o fundo transparente
        dialog.show();
        preencherSpinnerMateria();

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

    private void preencherSpinnerMateria() {
        try {
            dbHelper = new DatabaseHelper(getApplicationContext());
            bancoDados = dbHelper.getReadableDatabase();

            // Consulta à tabela materias para obter os nomes das matérias
            String[] colunasMaterias = {"nome_materia"};
            Cursor cursorMaterias = bancoDados.query("materias", colunasMaterias, null, null, null, null, null);

            // Criar uma lista para armazenar os nomes das matérias
            List<String> nomesMaterias = new ArrayList<>();

            // Adicionar os nomes das matérias à lista
            if (cursorMaterias != null && cursorMaterias.moveToFirst()) {
                do {
                    String nomeMateria = cursorMaterias.getString(cursorMaterias.getColumnIndex("nome_materia"));
                    nomesMaterias.add(nomeMateria);
                } while (cursorMaterias.moveToNext());

                cursorMaterias.close();
            }

            // Criar um ArrayAdapter para o Spinner
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nomesMaterias);

            // Definir o layout do dropdown do Spinner
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // Definir o ArrayAdapter como o adaptador do Spinner
            spinnerMateria.setAdapter(adapter);

            // Exibir os nomes das matérias no Log
            for (String nomeMateria : nomesMaterias) {
                Log.d("NomesMaterias", nomeMateria);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TAG", "Erro ao obter nome da matéria: " + e.getMessage());
        }


    }

    private void AdicionarLembrete(String nomeMateria, String categoria, String descricao, String data, Boolean notif) {
        int idLembrete = -1; // Valor padrão caso a inserção falhe

        try {
            dbHelper = new DatabaseHelper(getApplicationContext());
            bancoDados = dbHelper.getReadableDatabase();

            ContentValues values = new ContentValues();
            values.put("nomeMateria", nomeMateria);
            values.put("categoria", categoria);
            values.put("descr", descricao);
            values.put("data", data);
            values.put("notif", notif);

            idLembrete = (int) bancoDados.insert("lembretes", null, values);
            bancoDados.close();

            if (idLembrete != -1) {
                // Se o lembrete foi adicionado com sucesso, atualize o RecyclerView
                lembreteAdapter.adicionarLembrete(new Lembrete(idLembrete,data,descricao, nomeMateria, notif,categoria));
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
            Cursor cursor = bancoDados.rawQuery("SELECT * FROM lembretes WHERE data = ?", new String[]{data});
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex("id_lembretes"));
                    String nomeMateria = cursor.getString(cursor.getColumnIndex("nomeMateria"));
                    String categoria = cursor.getString(cursor.getColumnIndex("categoria"));
                    String descricao = cursor.getString(cursor.getColumnIndex("descr"));
                    String dataLembrete = cursor.getString(cursor.getColumnIndex("data"));
                    lembretes.add(new Lembrete(id,dataLembrete,descricao, nomeMateria,categoria));
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
            Cursor cursor = bancoDados.rawQuery("SELECT data FROM lembretes", null);
            if (cursor.moveToFirst()) {
                do {
                    String dataLembrete = cursor.getString(cursor.getColumnIndex("data"));
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


}
