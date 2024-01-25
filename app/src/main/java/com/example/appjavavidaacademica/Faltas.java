package com.example.appjavavidaacademica;

public class Faltas {

    private String data;

    private int id_materia;
    private int quantidade;
    private String motivo;

    private int totalFaltasMateria;

    private String nome_materia;

    private String nomeAgrupamento;


    // Construtor
    public Faltas(String data, int id_materia, int quantidade, String motivo) {
        this.data = data;
        this.id_materia = id_materia;
        this.quantidade = quantidade;
        this.motivo = motivo;
    }

    public Faltas(String nome_materia,int id_materia, int totalFaltasMateria) {
        this.nome_materia = nome_materia;
        this.id_materia = id_materia;
        this.totalFaltasMateria = totalFaltasMateria;
    }

    public Faltas(String data, int id_materia, int quantidade, String motivo,String nome_materia, String nomeAgrupamento) {
        this.data = data;
        this.id_materia = id_materia;
        this.quantidade = quantidade;
        this.motivo = motivo;
        this.nome_materia = nome_materia;
        this.nomeAgrupamento = nomeAgrupamento;
    }


    public int getIdMateriaFaltas() {
        return id_materia;
    }

    // Getters
    public String getData() {
        return data;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public String getMotivo() {
        return motivo;
    }

    public int getTotalFaltasMateria() {
        return totalFaltasMateria;
    }

    public String getNomeMateriaFaltas() {
        return nome_materia;
    }

    public String getNomeAgrupamento(){
        return nomeAgrupamento;
    }



}


