package com.example.appjavavidaacademica;

public class Lembrete {

    private String data;
    private String descr;
    private String nomeMateria;
    private Boolean notif;
    private String categoria;

    private int idLembrete;


    public Lembrete(int idLembrete,String data, String descr, String nomeMateria, Boolean notif,String categoria) {
        this.idLembrete = idLembrete;
        this.data = data;
        this.descr = descr;
        this.nomeMateria = nomeMateria;
        this.notif = notif;
        this.categoria=categoria;
    }

    public Lembrete(int idLembrete,String data, String descr, String nomeMateria,String categoria) {
        this.idLembrete = idLembrete;
        this.data = data;
        this.descr = descr;
        this.nomeMateria = nomeMateria;
        this.categoria=categoria;
    }

    public String getNomeMateria() {
        return nomeMateria;
    }

    // Getters
    public String getCategoria() {
        return categoria;
    }

    public String getDescricao() {
        return descr;
    }

    public String getData() {
        return data;
    }


}
