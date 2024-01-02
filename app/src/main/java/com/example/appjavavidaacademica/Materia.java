package com.example.appjavavidaacademica;

public class Materia {
    private int id_materia;
    private String nome_materia;
    private String dia_semana;
    private int id_agrupamento;

    private String quantAulas;


    // Construtor com os parâmetros
    public Materia(String nome_materia, String dia_semana, int id_agrupamento, int id_materia, String quantAulas) {
        this.nome_materia = nome_materia;
        this.dia_semana = dia_semana;
        this.id_agrupamento = id_agrupamento;
        this.id_materia = id_materia;
        this.quantAulas = quantAulas;

    }

    // Getters e setters
    public int getId() {
        return id_materia;
    }

    public void setId(int id_materia) {
        this.id_materia = id_materia;
    }

    public String getNomeMateria() { return nome_materia; }


    public String getDataMateria() {
        return dia_semana;
    }


    public int getIdAgrupamento() {
        return id_agrupamento;
    }

    public String getQuantAulas() {
        return quantAulas;
    }

}
