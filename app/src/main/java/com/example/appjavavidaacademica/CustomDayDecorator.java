package com.example.appjavavidaacademica;

import android.graphics.Color;

import androidx.annotation.NonNull;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.List;

public class CustomDayDecorator implements DayViewDecorator {
    private final List<Lembrete> lembretes;

    public CustomDayDecorator(List<Lembrete> lembretes) {
        this.lembretes = lembretes;
    }

    @Override
    public boolean shouldDecorate(@NonNull CalendarDay day) {
        // Verificar se a data do dia corresponde a uma data de lembrete
        for (Lembrete lembrete : lembretes) {
            if (lembrete.getDataCalendarDay().equals(day.toString())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void decorate(@NonNull DayViewFacade view) {
        for (Lembrete lembrete : lembretes) {
            int color;

            // Se o lembrete é novo, aplicar cor com base na categoria
            color = getColorForCategory(lembrete.getCategoria());

            // Adicionar um ponto com a cor especificada ao dia
            view.addSpan(new DotSpan(5, color));
        }
    }

    private int getColorForCategory(String categoria) {
        switch (categoria) {
            case "TCC":
                return Color.RED;
            case "Reunião":
                return Color.GREEN;
            case "Avaliação":
                return Color.BLUE;
            default:
                return Color.BLACK; // Cor padrão
        }
    }
}
