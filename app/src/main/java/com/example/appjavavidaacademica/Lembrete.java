package com.example.appjavavidaacademica;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

public class Lembrete {

    private String data;
    private String descr;
    private String nomeMateria;
    private Boolean notif;
    private String categoria;

    private int idLembrete;
    private String dataCalendarDay;



    public Lembrete(int idLembrete,String data, String descr, String nomeMateria, Boolean notif,String categoria,String dataCalendarDay) {
        this.idLembrete = idLembrete;
        this.data = data;
        this.dataCalendarDay = dataCalendarDay;
        this.descr = descr;
        this.nomeMateria = nomeMateria;
        this.notif = notif;
        this.categoria=categoria;
    }

    public Lembrete(int idLembrete,String data, String descr, String nomeMateria,String categoria,String dataCalendarDay) {
        this.idLembrete = idLembrete;
        this.data = data;
        this.dataCalendarDay = dataCalendarDay;

        this.descr = descr;
        this.nomeMateria = nomeMateria;
        this.categoria=categoria;
    }

    public int getIdLembrete() {
        return idLembrete;
    }

    public String getDataCalendarDay() {
        return dataCalendarDay;
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

    public LocalDate getDataCalendarDayAsLocalDate() {
        // Formate a data no formato esperado (por exemplo, "yyyy-MM-dd")
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // Converta a String para LocalDate usando o formato especificado
        return LocalDate.parse(dataCalendarDay, formatter);
    }


}
