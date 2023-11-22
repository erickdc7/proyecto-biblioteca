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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SettingsFragment extends Fragment {

    private TextView textViewUstedDebe;
    private TextView textViewNombreLibro;
    private TextView textViewIdPrestamo;

    private Button botonRecogido;
    private List<String> labels = new ArrayList<>();  // Variable miembro para almacenar los nombres de los libros

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

        updateChart();

        loadDataFromDatabase();

        botonRecogido.setOnClickListener(v -> abrirGoogleMaps());

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

        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setEnabled(false);

        BarDataSet barDataSet = new BarDataSet(new ArrayList<>(), "Libros Prestados");
        barDataSet.setBarBorderWidth(1f);
        barDataSet.setBarBorderColor(ContextCompat.getColor(requireContext(), R.color.colorAccent));

        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);

        // Asigna el formateador de valores personalizado al eje X
        XAxisValueFormatter xAxisValueFormatter = new XAxisValueFormatter(labels);
        xAxis.setValueFormatter(xAxisValueFormatter);

        YAxisValueFormatter yAxisValueFormatter = new YAxisValueFormatter();
        leftAxis.setValueFormatter(yAxisValueFormatter);

        barChart.getLegend().setEnabled(false);
    }

    private void updateChart() {
        List<BarEntry> entries = getChartData();

        BarDataSet barDataSet = new BarDataSet(entries, "Libros Prestados");

        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);

        barChart.invalidate();
    }

    private List<BarEntry> getChartData() {
        List<BarEntry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();  // Lista para almacenar los nombres de los libros

        SQLiteDatabase database = dbHelper.getReadableDatabase();

        Cursor cursor = database.query(
                DBHelper.TABLE_PRESTAMOS,
                new String[]{DBHelper.COLUMN_LIBRO, DBHelper.COLUMN_CANTIDAD_PRESTAMOS},
                null,
                null,
                DBHelper.COLUMN_LIBRO,
                null,
                null
        );

        HashMap<String, Integer> librosPrestados = new HashMap<>();

        if (cursor != null) {
            int libroIndex = cursor.getColumnIndex(DBHelper.COLUMN_LIBRO);
            int cantidadIndex = cursor.getColumnIndex(DBHelper.COLUMN_CANTIDAD_PRESTAMOS);

            if (libroIndex != -1 && cantidadIndex != -1) {
                while (cursor.moveToNext()) {
                    String libro = cursor.getString(libroIndex);
                    int cantidadPrestamos = cursor.getInt(cantidadIndex);

                    if (librosPrestados.containsKey(libro)) {
                        // Si el libro ya está en el HashMap, actualiza la cantidad acumulada
                        librosPrestados.put(libro, librosPrestados.get(libro) + cantidadPrestamos);
                    } else {
                        // Si el libro no está en el HashMap, agrégalo
                        librosPrestados.put(libro, cantidadPrestamos);
                    }
                }
            }

            cursor.close();
        }

        database.close();

        // Convierte el HashMap en entradas para el gráfico
        int index = 0;
        for (String libro : librosPrestados.keySet()) {
            labels.add(libro);  // Agrega el nombre del libro a la lista de etiquetas
            entries.add(new BarEntry(index, librosPrestados.get(libro)));
            index++;
        }

        // Asigna el formateador de etiquetas personalizado al eje X
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

        return entries;
    }

    private static class XAxisValueFormatter extends IndexAxisValueFormatter {

        private final List<String> labels;

        public XAxisValueFormatter(List<String> labels) {
            this.labels = labels;
        }

        @Override
        public String getFormattedValue(float value) {
            int index = (int) value;
            if (index >= 0 && index < labels.size()) {
                return labels.get(index);
            }
            return "";
        }
    }

    private static class YAxisValueFormatter extends ValueFormatter {
        @Override
        public String getFormattedValue(float value) {
            return String.valueOf((int) value);
        }
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
                    updateUI("", "");
                }
            } else {
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

                // Obtén el libro que se está devolviendo desde SharedPreferences
                String libroDevuelto = sharedPreferences.getString(getString(R.string.last_borrowed_book), "");

                // Verifica si el libro devuelto es el mismo que se muestra en la interfaz de usuario
                if (!libroDevuelto.isEmpty() && libroDevuelto.equals(ultimoLibroPrestado)) {
                    // Muestra un mensaje indicando que no se debe ningún libro
                    Toast.makeText(requireContext(), "No debe ningún libro", Toast.LENGTH_SHORT).show();

                    // Limpia las preferencias ya que se ha devuelto el libro
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove(getString(R.string.last_borrowed_book));
                    editor.remove(getString(R.string.last_borrowed_id));
                    editor.apply();

                    // Actualiza la interfaz para reflejar que no hay préstamos recientes
                    textViewUstedDebe.setVisibility(View.GONE);
                    textViewNombreLibro.setText("No ha realizado préstamos recientes");
                    textViewIdPrestamo.setText("");

                }
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
