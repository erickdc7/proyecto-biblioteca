package com.erickdiaz.proyectobiblioteca;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class SettingsFragment extends Fragment {

    private TextView textViewUstedDebe;
    private TextView textViewNombreLibro;
    private TextView textViewIdPrestamo;

    private Button botonRecogido;

    private BarChart barChart;

    private SharedPreferences sharedPreferences;
    private DBHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        textViewUstedDebe = rootView.findViewById(R.id.textViewUstedDebe);
        textViewNombreLibro = rootView.findViewById(R.id.textViewNombreLibro);
        textViewIdPrestamo = rootView.findViewById(R.id.textViewIdPrestamo);
        botonRecogido = rootView.findViewById(R.id.botonRecogido);
        barChart = rootView.findViewById(R.id.barChart);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        dbHelper = new DBHelper(requireContext());

        setupBarChart();

        loadDataFromDatabase();
        botonRecogido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirGoogleMaps();
            }
        });
        return rootView;
    }

    private void loadDataFromDatabase() {
        String ultimoLibroPrestado = sharedPreferences.getString(getString(R.string.last_borrowed_book), "");
        String idPrestamo = sharedPreferences.getString(getString(R.string.last_borrowed_id), "");

        if (!ultimoLibroPrestado.isEmpty() && !idPrestamo.isEmpty()) {
            updateUI(ultimoLibroPrestado, idPrestamo);
        } else {
            obtenerUltimoLibroPrestadoDesdeBaseDeDatos();
        }

        updateChart();
    }

    private void setupBarChart() {
        barChart.getDescription().setEnabled(false);
        barChart.setPinchZoom(false);
        barChart.setDrawBarShadow(false);
        barChart.setDrawGridBackground(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setLabelCount(5, true);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int) value);
            }
        });
        leftAxis.setAxisMaximum(getMaxValueFromData() + 1);

        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setLabelCount(5, false);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinimum(0f);
        rightAxis.setEnabled(false);

        BarDataSet barDataSet = new BarDataSet(new ArrayList<>(), "Libros Prestados");
        barDataSet.setBarBorderWidth(1f);
        barDataSet.setBarBorderColor(ContextCompat.getColor(requireContext(), R.color.colorAccent));

        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);

        barChart.getLegend().setEnabled(false);
    }

    private float getMaxValueFromData() {
        DBHelper dbHelper = new DBHelper(requireContext());
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        Cursor cursor = database.query(
                DBHelper.TABLE_PRESTAMOS,
                new String[]{DBHelper.COLUMN_CANTIDAD_PRESTAMOS},
                null,
                null,
                null,
                null,
                null
        );

        float maxValue = 0;

        if (cursor != null) {
            int cantidadIndex = cursor.getColumnIndex(DBHelper.COLUMN_CANTIDAD_PRESTAMOS);

            if (cantidadIndex != -1) {
                while (cursor.moveToNext()) {
                    int cantidadPrestamos = cursor.getInt(cantidadIndex);
                    if (cantidadPrestamos > maxValue) {
                        maxValue = cantidadPrestamos;
                    }
                }
            } else {
                // Handle the case where the column doesn't exist in the cursor
            }

            cursor.close();
        }

        database.close();
        dbHelper.close();

        return maxValue;
    }

    private void updateChart() {
        List<BarEntry> entries = getChartData();

        BarDataSet barDataSet = new BarDataSet(entries, "Libros Prestados");

        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(getLabels()));

        YAxis yAxisRight = barChart.getAxisRight();
        yAxisRight.setEnabled(false);

        barChart.invalidate();
    }

    private List<BarEntry> getChartData() {
        List<BarEntry> entries = new ArrayList<>();

        DBHelper dbHelper = new DBHelper(requireContext());
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        Cursor cursor = database.query(
                DBHelper.TABLE_PRESTAMOS,
                new String[]{DBHelper.COLUMN_LIBRO, DBHelper.COLUMN_CANTIDAD_PRESTAMOS},
                null,
                null,
                null,
                null,
                null
        );

        if (cursor != null) {
            int libroIndex = cursor.getColumnIndex(DBHelper.COLUMN_LIBRO);
            int cantidadIndex = cursor.getColumnIndex(DBHelper.COLUMN_CANTIDAD_PRESTAMOS);

            if (libroIndex != -1 && cantidadIndex != -1) {
                int index = 0;

                while (cursor.moveToNext()) {
                    String libro = cursor.getString(libroIndex);
                    int cantidadPrestamos = cursor.getInt(cantidadIndex);
                    entries.add(new BarEntry(index, cantidadPrestamos));
                    index++;
                }
            } else {
                // Handle the case where the columns don't exist in the cursor
            }

            cursor.close();
        }

        database.close();
        dbHelper.close();

        return entries;
    }

    private List<String> getLabels() {
        List<String> labels = new ArrayList<>();

        DBHelper dbHelper = new DBHelper(requireContext());
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        Cursor cursor = database.query(
                DBHelper.TABLE_PRESTAMOS,
                new String[]{DBHelper.COLUMN_LIBRO},
                null,
                null,
                null,
                null,
                null
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int libroIndex = cursor.getColumnIndex(DBHelper.COLUMN_LIBRO);
                if (libroIndex != -1) {
                    String libro = cursor.getString(libroIndex);
                    labels.add(libro);
                }
            }

            cursor.close();
        }

        database.close();
        dbHelper.close();

        return labels;
    }

    private void obtenerUltimoLibroPrestadoDesdeBaseDeDatos() {
        try (Cursor cursor = dbHelper.getReadableDatabase().query(
                DBHelper.TABLE_PRESTAMOS,
                new String[]{DBHelper.COLUMN_LIBRO, DBHelper.COLUMN_ID},
                null,
                null,
                null,
                null,
                null,
                null
        )) {
            if (cursor != null && cursor.moveToLast()) {
                int libroColumnIndex = cursor.getColumnIndex(DBHelper.COLUMN_LIBRO);
                int idColumnIndex = cursor.getColumnIndex(DBHelper.COLUMN_ID);

                if (libroColumnIndex != -1 && idColumnIndex != -1) {
                    String ultimoLibroPrestado = cursor.getString(libroColumnIndex);
                    String idPrestamo = cursor.getString(idColumnIndex);

                    updateUI(ultimoLibroPrestado, idPrestamo);
                    updateChart();
                } else {
                    // Handle the case where the columns don't exist in the cursor
                    updateUI("", "");
                }
            } else {
                // Handle the case where there's no data in the cursor
                updateUI("", "");
            }
        }
    }

    private void updateUI(String ultimoLibroPrestado, String idPrestamo) {
        if (textViewUstedDebe != null) {
            if (ultimoLibroPrestado.isEmpty()) {
                textViewUstedDebe.setVisibility(View.GONE);
                textViewNombreLibro.setText("No ha realizado préstamos recientes");
            } else {
                textViewUstedDebe.setVisibility(View.VISIBLE);
                textViewNombreLibro.setText("Usted debe: " + ultimoLibroPrestado);
                textViewIdPrestamo.setText("ID de préstamo: " + idPrestamo);
            }
        }
    }

    private void abrirGoogleMaps() {
        String ubicacion = "https://www.google.com/maps/search/279,+Av.+Arequipa+265,+Lima+15046/@-12.0685064,-77.0388615,17z/data=!3m1!4b1?entry=ttu";

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ubicacion));
        intent.setPackage("com.google.android.apps.maps");

        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivity(intent);
        } else {
            abrirGoogleMapsEnNavegadorWeb();
        }
    }

    private void abrirGoogleMapsEnNavegadorWeb() {
        String url = "https://www.google.com/maps/search/279,+Av.+Arequipa+265,+Lima+15046/@-12.0685064,-77.0388615,17z/data=!3m1!4b1?entry=ttu";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}
