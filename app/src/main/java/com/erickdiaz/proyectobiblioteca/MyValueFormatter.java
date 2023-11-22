package com.erickdiaz.proyectobiblioteca;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;


public class MyValueFormatter extends ValueFormatter {

    @Override
    public String getBarLabel(BarEntry barEntry) {
        return String.valueOf((int) barEntry.getY());
    }
}

