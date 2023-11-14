package com.erickdiaz.proyectobiblioteca;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

public class SettingsFragment extends Fragment {

    private TextView textViewUstedDebe;
    private TextView textViewNombreLibro;
    private TextView textViewIdPrestamo;

    private Button botonRecogido;

    private SharedPreferences sharedPreferences;
    private DBHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Utiliza el inflador proporcionado para inflar el diseño de este fragmento
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        textViewUstedDebe = rootView.findViewById(R.id.textViewUstedDebe);
        textViewNombreLibro = rootView.findViewById(R.id.textViewNombreLibro);
        textViewIdPrestamo = rootView.findViewById(R.id.textViewIdPrestamo);
        botonRecogido = rootView.findViewById(R.id.botonRecogido);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        dbHelper = new DBHelper(requireContext());

        // Carga los datos desde la base de datos SQLite
        loadDataFromDatabase();
        botonRecogido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirGoogleMaps();
            }
        });
        return rootView;  // Devuelve la vista creada
    }

    private void loadDataFromDatabase() {
        // Obtiene el último libro prestado y su ID desde SharedPreferences
        String ultimoLibroPrestado = sharedPreferences.getString(getString(R.string.last_borrowed_book), "");
        String idPrestamo = sharedPreferences.getString(getString(R.string.last_borrowed_id), "");

        if (!ultimoLibroPrestado.isEmpty() && !idPrestamo.isEmpty()) {
            // Si hay datos en SharedPreferences, actualiza la interfaz de usuario
            updateUI(ultimoLibroPrestado, idPrestamo);
        } else {
            // Si no hay datos en SharedPreferences, intenta obtener el último libro prestado desde la base de datos
            obtenerUltimoLibroPrestadoDesdeBaseDeDatos();
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

                    // Actualiza la interfaz de usuario con los datos de la base de datos
                    updateUI(ultimoLibroPrestado, idPrestamo);
                } else {
                    // Si las columnas no están presentes, muestra un mensaje
                    updateUI("", "");
                }
            } else {
                // Si no hay datos en la base de datos, muestra un mensaje
                updateUI("", "");
            }
        }
    }


    private void updateUI(String ultimoLibroPrestado, String idPrestamo) {
        if (textViewUstedDebe != null) {
            if (ultimoLibroPrestado.isEmpty() || idPrestamo.isEmpty()) {
                textViewUstedDebe.setVisibility(View.GONE);
                textViewNombreLibro.setText("No ha realizado préstamos recientes");
            } else {
                textViewUstedDebe.setVisibility(View.VISIBLE);
                textViewNombreLibro.setText("Libro a Recoger: " + ultimoLibroPrestado);
                textViewIdPrestamo.setText("ID de préstamo: " + idPrestamo);
            }
        }
    }
    private void abrirGoogleMaps() {
        // Puedes cambiar las coordenadas a la ubicación que desees
        String ubicacion = "https://www.google.com/maps/search/279,+Av.+Arequipa+265,+Lima+15046/@-12.0685064,-77.0388615,17z/data=!3m1!4b1?entry=ttu";

        // Crear un intent para abrir Google Maps
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ubicacion));
        intent.setPackage("com.google.android.apps.maps");

        // Verificar si hay aplicaciones que pueden manejar el intent
        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivity(intent);
        } else {
            // Si no hay aplicaciones que pueden manejar el intent, puedes mostrar un mensaje al usuario
            // o tomar alguna otra acción según tus necesidades
            // Por ejemplo, puedes abrir la versión web de Google Maps en un navegador
            abrirGoogleMapsEnNavegadorWeb();
        }
    }

    private void abrirGoogleMapsEnNavegadorWeb() {
        String url = "https://www.google.com/maps/search/279,+Av.+Arequipa+265,+Lima+15046/@-12.0685064,-77.0388615,17z/data=!3m1!4b1?entry=ttu";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}
