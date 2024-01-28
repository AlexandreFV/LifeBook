package com.example.appjavavidaacademica;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DesempenhoActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;

    private String selectedAgrupamento = null;
    private String selectedGraph = null;

    private ImageView imageCloseDesempenho;

    private View fundoOpcaoSelecionadaGrafico;
    private TextView TextSelecione;

    private TextView TextBarrasVerticais;

    private TextView TextBarraHorizontal;

    private TextView TextPizza;

    private TextView TextSelecioneCard;

    private TextView TextNomeCardSpinnerSelected;

    private View listaFaltasButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.desempenho);

        final ImageView imageView = findViewById(R.id.CardIconDesempenho);
        final Spinner spinner = findViewById(R.id.spinnerAgrupamentos);
        final Spinner spinnerGrafico = findViewById(R.id.GraficoSpinnerDesempenho);
        final ImageView graficoImageView = findViewById(R.id.GraficoIconDesempenho);
        fundoOpcaoSelecionadaGrafico = findViewById(R.id.FundoOpcaoSelecionadaGrafico);
        TextSelecione = findViewById(R.id.TextSelecione);
        TextBarrasVerticais = findViewById(R.id.TextBarrasVerticais);
        TextBarraHorizontal = findViewById(R.id.TextBarraHorizontal);
        TextPizza = findViewById(R.id.TextPizza);
        TextSelecioneCard = findViewById(R.id.TextSelecioneCard);
        TextNomeCardSpinnerSelected = findViewById(R.id.TextNomeCardSpinnerSelected);
        listaFaltasButton = findViewById(R.id.ButtonListaFaltas);

        dbHelper = new DatabaseHelper(this);
        String[] agrupamentos = obterAgrupamentosDoBancoDeDados();

        List<String> agrupamentosList = new ArrayList<>();
        agrupamentosList.add("");  // Adicione um item vazio como o primeiro item
        agrupamentosList.addAll(Arrays.asList(agrupamentos));

        layoutInferiorBotoes barraInferior = new layoutInferiorBotoes(this, findViewById(R.id.includeDesempenhoInf), findViewById(R.id.AdicioneAlgoScreen));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_preto, agrupamentosList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        listaFaltasButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar a atividade DesempenhoActivity quando o botão desempenho for clicado
                Intent intent = new Intent(DesempenhoActivity.this, ListaFaltas.class);
                startActivity(intent);
            }
        });


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.performClick();
            }
        });


        selectedAgrupamento = null;
        selectedGraph = null;

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedAgrupamento = (String) parentView.getItemAtPosition(position);
                TextNomeCardSpinnerSelected.setText(selectedAgrupamento);

                if (selectedAgrupamento != null && selectedAgrupamento != "") {
                    graficoImageView.setEnabled(true);
                    spinnerGrafico.setEnabled(true);
                    graficoImageView.setAlpha(1f);
                    spinnerGrafico.setAlpha(1f);
                    TextSelecione.setAlpha(1f);
                    fundoOpcaoSelecionadaGrafico.setAlpha(1f);
                    TextSelecione.setAlpha(1f);
                    TextBarrasVerticais.setAlpha(1f);
                    TextPizza.setAlpha(1f);
                    TextBarraHorizontal.setAlpha(1f);
                    TextSelecioneCard.setAlpha(0f);
                    TextNomeCardSpinnerSelected.setVisibility(View.VISIBLE);
                } else {
                    // Caso contrário, desabilite a ImageView e o Spinner do gráfico
                    graficoImageView.setEnabled(false);
                    spinnerGrafico.setEnabled(false);
                    graficoImageView.setAlpha(0.1f);
                    spinnerGrafico.setAlpha(0.1f);
                    TextSelecione.setAlpha(0.1f);
                    fundoOpcaoSelecionadaGrafico.setAlpha(0.1f);
                    TextSelecione.setAlpha(0.0f);
                    TextBarrasVerticais.setAlpha(0.0f);
                    TextPizza.setAlpha(0.0f);
                    TextBarraHorizontal.setAlpha(0.0f);
                    TextSelecioneCard.setAlpha(1f);
                    TextNomeCardSpinnerSelected.setVisibility(View.GONE);


                }
                View fundoOpcaoSelecionadaCard = findViewById(R.id.TextSelecione);
                updateChartIfReady();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                selectedAgrupamento = null;
                updateChartIfReady();
            }
        });

        graficoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerGrafico.performClick();



            }
        });


        List<String> opcoesGraficos = new ArrayList<>(Arrays.asList("", "Gráfico de Barras Horizontal", "Gráfico de Barras Vertical", "Gráfico de Pizza"));
        ArrayAdapter<String> graficoAdapter = new ArrayAdapter<>(this, R.layout.spinner_preto, opcoesGraficos);
        graficoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGrafico.setAdapter(graficoAdapter);

        spinnerGrafico.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                selectedGraph = (String) parentView.getItemAtPosition(position);
                updateChartIfReady();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                selectedGraph = null;
                updateChartIfReady();
            }
        });

        ImageView imageCloseDesempenho = findViewById(R.id.imageCloseDesempenho);
        imageCloseDesempenho.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View v) {
                    finish();
                    Intent intent = new Intent(DesempenhoActivity.this, MainActivity.class);
                    startActivity(intent);
                }
        });
}

    public void exibirInformacoesFaltas(List<Faltas> faltasList, String selectedGraph) {
        StringBuilder informacoesFaltas = new StringBuilder();

        PieChart pieChart = findViewById(R.id.pieChart);
        List<PieEntry> entries = new ArrayList<>();

        BarChart barChart = findViewById(R.id.barChart); // Certifique-se de que você tenha um BarChart no seu layout XML
        barChart.setVisibility(View.VISIBLE);

        HorizontalBarChart horizontalBarChart = findViewById(R.id.horizontalBarChart); // Certifique-se de ter um HorizontalBarChart no seu layout XML


        switch (selectedGraph) {
            case "Gráfico de Barras Horizontal":
                pieChart.setVisibility(View.GONE);
                barChart.setVisibility(View.GONE);
                horizontalBarChart.setVisibility(View.VISIBLE);
                fundoOpcaoSelecionadaGrafico.setVisibility(View.VISIBLE);
                TextBarraHorizontal.setVisibility(View.VISIBLE);
                TextBarrasVerticais.setVisibility(View.GONE);
                TextPizza.setVisibility(View.GONE);
                TextSelecione.setVisibility(View.GONE);

                exibirGraficoBarrasHorizontal(faltasList);


                break;
            case "Gráfico de Barras Vertical":
                pieChart.setVisibility(View.GONE);
                barChart.setVisibility(View.VISIBLE);
                horizontalBarChart.setVisibility(View.GONE);
                fundoOpcaoSelecionadaGrafico.setVisibility(View.VISIBLE);
                TextBarraHorizontal.setVisibility(View.GONE);
                TextBarrasVerticais.setVisibility(View.VISIBLE);
                TextPizza.setVisibility(View.GONE);
                TextSelecione.setVisibility(View.GONE);

                exibirGraficoBarrasVerticais(faltasList);


                break;
            case "Gráfico de Pizza":
                barChart.setVisibility(View.GONE);
                pieChart.setVisibility(View.VISIBLE);
                horizontalBarChart.setVisibility(View.GONE);
                fundoOpcaoSelecionadaGrafico.setVisibility(View.VISIBLE);
                TextBarraHorizontal.setVisibility(View.GONE);
                TextBarrasVerticais.setVisibility(View.GONE);
                TextPizza.setVisibility(View.VISIBLE);
                TextSelecione.setVisibility(View.GONE);

                exibirGraficoPizza(faltasList);


                break;
            default:
                fundoOpcaoSelecionadaGrafico.setVisibility(View.VISIBLE);
                TextBarraHorizontal.setVisibility(View.GONE);
                TextBarrasVerticais.setVisibility(View.GONE);
                TextPizza.setVisibility(View.GONE);
                TextSelecione.setVisibility(View.VISIBLE);
                // Adicione lógica para outros tipos de gráficos conforme necessário
                break;
        }

        if(selectedGraph != ""){
            // Tornar visíveis os itens desejados quando um gráfico for selecionado
            View fundoGrafico = findViewById(R.id.fundoGrafico);
            TextView TextQuantFaltasGrafico = findViewById(R.id.TextQuantFaltasGrafico);
            TextView TextParabens = findViewById(R.id.TextParabens);

            fundoGrafico.setVisibility(View.VISIBLE);
            TextQuantFaltasGrafico.setVisibility(View.VISIBLE);
            TextParabens.setVisibility(View.VISIBLE);
        }

    }

    public void exibirGraficoBarrasHorizontal(List<Faltas> faltasList) {
        StringBuilder informacoesFaltas = new StringBuilder();

        HorizontalBarChart horizontalBarChart = findViewById(R.id.horizontalBarChart); // Certifique-se de ter um HorizontalBarChart no seu layout XML
        List<BarEntry> entries = new ArrayList<>();

        int index = 0;
        for (Faltas faltas : faltasList) {
            int quantidade = faltas.getTotalFaltasMateria();
            String nomeMateria = faltas.getNomeMateriaFaltas(); // Obter o nome da matéria

            informacoesFaltas.append("Matéria ").append(nomeMateria).append(": ").append(quantidade).append(" faltas\n");
            entries.add(new BarEntry(index++, quantidade)); // Usar o índice como y e a quantidade como x
        }

        if (dbHelper.calcularQuantidadeTotalFaltas(faltasList) == 0) {
            // Se a quantidade total de faltas for 0, exibir uma mensagem
            // Substitua "seuTextView" pelo ID do seu TextView ou outra forma de exibição de mensagem desejada
            TextView seuTextView = findViewById(R.id.TextParabens);
            seuTextView.setText("Parabéns, você não possui nenhuma falta!");
            horizontalBarChart.setVisibility(View.GONE); // Ocultar o gráfico
        } else {
            TextView seuTextView = findViewById(R.id.TextParabens); // Substitua pelo ID do seu TextView
            seuTextView.setText("");

            // Caso contrário, configurar o gráfico de barras normalmente
            BarDataSet dataSet = new BarDataSet(entries, "Faltas por Matéria");
            dataSet.setColors(ColorTemplate.COLORFUL_COLORS); // Cores automáticas
            BarData data = new BarData(dataSet);
            horizontalBarChart.setData(data);

            // Calcular a quantidade total de faltas
            int quantidadeTotalFaltas = dbHelper.calcularQuantidadeTotalFaltas(faltasList);

            // Exibir a quantidade total de faltas para todas as matérias no agrupamento
            // Adicionar logs para depuração
            Log.d("TAG", "Informações de faltas: " + informacoesFaltas.toString());
            Log.d("TAG", "Quantidade total de faltas: " + quantidadeTotalFaltas);
            horizontalBarChart.getDescription().setEnabled(false);

            // Atualizar a interface do usuário (exemplo: exibir em um TextView)
            horizontalBarChart.setVisibility(View.VISIBLE); // Certificar-se de que o gráfico é visível
            horizontalBarChart.invalidate();
        }
    }



    public void exibirGraficoBarrasVerticais(List<Faltas> faltasList) {
        StringBuilder informacoesFaltas = new StringBuilder();
        BarChart barChart = findViewById(R.id.barChart); // Certifique-se de ter um BarChart no seu layout XML
        List<BarEntry> entries = new ArrayList<>();

        int index = 0;
        for (Faltas faltas : faltasList) {
            int quantidade = faltas.getTotalFaltasMateria();
            String nomeMateria = faltas.getNomeMateriaFaltas(); // Obter o nome da matéria

            informacoesFaltas.append("Matéria ").append(nomeMateria).append(": ").append(quantidade).append(" faltas\n");
            entries.add(new BarEntry(index++, quantidade)); // Usar o índice como x e a quantidade como y
        }

        if (dbHelper.calcularQuantidadeTotalFaltas(faltasList) == 0) {
            // Se a quantidade total de faltas for 0, exibir uma mensagem
            // Substitua "seuTextView" pelo ID do seu TextView ou outra forma de exibição de mensagem desejada
            TextView seuTextView = findViewById(R.id.TextParabens); // Substitua pelo ID do seu TextView
            seuTextView.setText("Parabéns, você não possui nenhuma falta!");
            barChart.setVisibility(View.GONE); // Ocultar o gráfico
        } else {
            TextView seuTextView = findViewById(R.id.TextParabens); // Substitua pelo ID do seu TextView
            seuTextView.setText("");

            // Caso contrário, configurar o gráfico de barras normalmente
            BarDataSet dataSet = new BarDataSet(entries, "Faltas por Matéria");
            dataSet.setColors(ColorTemplate.COLORFUL_COLORS); // Cores automáticas
            BarData data = new BarData(dataSet);
            barChart.setData(data);

            // Calcular a quantidade total de faltas
            int quantidadeTotalFaltas = dbHelper.calcularQuantidadeTotalFaltas(faltasList);

            // Exibir a quantidade total de faltas para todas as matérias no agrupamento
            // Adicionar logs para depuração
            Log.d("TAG", "Informações de faltas: " + informacoesFaltas.toString());
            Log.d("TAG", "Quantidade total de faltas: " + quantidadeTotalFaltas);
            barChart.getDescription().setEnabled(false);

            // Atualizar a interface do usuário (exemplo: exibir em um TextView)
            barChart.setVisibility(View.VISIBLE); // Certificar-se de que o gráfico é visível
            barChart.invalidate();
        }
    }

    public void exibirGraficoPizza(List<Faltas> faltasList) {
        StringBuilder informacoesFaltas = new StringBuilder();

        PieChart pieChart = findViewById(R.id.pieChart);
        List<PieEntry> entries = new ArrayList<>();

        for (Faltas faltas : faltasList) {
            int quantidade = faltas.getTotalFaltasMateria();
            String nomeMateria = faltas.getNomeMateriaFaltas(); // Obter o nome da matéria

            informacoesFaltas.append("Matéria ").append(nomeMateria).append(": ").append(quantidade).append(" faltas\n");
            entries.add(new PieEntry(quantidade, nomeMateria)); // Usar o nome da matéria
        }

        if (dbHelper.calcularQuantidadeTotalFaltas(faltasList) == 0) {
            // Se a quantidade total de faltas for 0, exibir uma mensagem
            // Substitua "seuTextView" pelo ID do seu TextView ou outra forma de exibição de mensagem desejada
            TextView seuTextView = findViewById(R.id.TextParabens); // Substitua pelo ID do seu TextView
            seuTextView.setText("Parabéns, você não possui nenhuma falta!");
            pieChart.setVisibility(View.GONE); // Ocultar o gráfico
        } else {
            TextView seuTextView = findViewById(R.id.TextParabens); // Substitua pelo ID do seu TextView
            seuTextView.setText("");

            // Caso contrário, configurar o gráfico de pizza normalmente
            PieDataSet dataSet = new PieDataSet(entries, "Faltas por Matéria");
            dataSet.setColors(ColorTemplate.COLORFUL_COLORS); // Cores automáticas
            PieData data = new PieData(dataSet);
            pieChart.setData(data);

            // Calcular a quantidade total de faltas
            int quantidadeTotalFaltas = dbHelper.calcularQuantidadeTotalFaltas(faltasList);

            // Exibir a quantidade total de faltas para todas as matérias no agrupamento
            // Adicionar logs para depuração
            Log.d("TAG", "Informações de faltas: " + informacoesFaltas.toString());
            Log.d("TAG", "Quantidade total de faltas: " + quantidadeTotalFaltas);
            pieChart.getDescription().setEnabled(false);

            // Atualizar a interface do usuário (exemplo: exibir em um TextView)
            pieChart.setVisibility(View.VISIBLE); // Certificar-se de que o gráfico é visível
            pieChart.invalidate();
        }
    }




    private String[] obterAgrupamentosDoBancoDeDados() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase bancoDados = dbHelper.getReadableDatabase();

        String[] projection = {"nome_agrupamento"};  // Coluna diretamente

        Cursor cursor = bancoDados.query(
                "agrupamento",  // Nome da tabela
                projection,
                null,
                null,
                null,
                null,
                null
        );

        List<String> agrupamentosList = new ArrayList<>();
        while (cursor.moveToNext()) {
            String nome = cursor.getString(cursor.getColumnIndexOrThrow("nome_agrupamento"));
            agrupamentosList.add(nome);
        }
        cursor.close();


        return agrupamentosList.toArray(new String[0]);
    }

    private void updateChartIfReady() {
        if (selectedAgrupamento != null && selectedGraph != null) {
            List<Faltas> faltasList = dbHelper.obterFaltasPorAgrupamentoEMateria(selectedAgrupamento);
            exibirInformacoesFaltas(faltasList, selectedGraph);
        }

    }


}
