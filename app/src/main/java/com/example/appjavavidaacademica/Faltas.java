package com.example.appjavavidaacademica;

import java.util.Date;

public class Faltas {

    private Date data;

    private int id_materia;
    private int quantidade;
    private String motivo;

    private int totalFaltasMateria;

    private String nome_materia;


    // Construtor
    public Faltas(Date data, int id_materia, int quantidade, String motivo) {
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


    public int getIdMateriaFaltas() {
        return id_materia;
    }

    // Getters
    public Date getData() {
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

}


