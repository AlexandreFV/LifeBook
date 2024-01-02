package com.example.appjavavidaacademica;

public class Agrupamento {

    private int id_agrupamento;
    private String nomeAgrupamento;
    private String categoria;
    private String corFundoHex;

    private String corTextoHex;

    private String corBotoesHex;

    private int iconEscolhido;


    // Construtor
    public Agrupamento(String nomeAgrupamento, String categoria, String corFundoHex, int id_agrupamento, String corTextoHex, String  corBotoesHex, int iconEscolhido) {
        this.id_agrupamento = id_agrupamento;
        this.nomeAgrupamento = nomeAgrupamento;
        this.categoria = categoria;
        this.corFundoHex = corFundoHex;
        this.corTextoHex = corTextoHex;
        this.corBotoesHex = corBotoesHex;
        this.iconEscolhido = iconEscolhido;
    }

    public int getId() {
        return id_agrupamento;
    }

    // Getters
    public String getNomeAgrupamento() {
        return nomeAgrupamento;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getCorFundoHex() {
        return corFundoHex;
    }

    public String getCorTextoHex() {
        return corTextoHex;
    }

    public String getCorButtonHex() {
        return corBotoesHex;
    }

    public int getIconEscolhido() {
        return iconEscolhido;
    }

}
