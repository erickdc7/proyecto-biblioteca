package com.erickdiaz.proyectobiblioteca;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PrestamoFragment extends Fragment {

    private Spinner spinnerLibros;
    private EditText editTextFechaPrestamo;
    private RadioGroup radioGroupDias;
    private Context context;
    private DBHelper dbHelper;
    private String fechaActual;
    private View rootView;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.solicitudprestamo, container, false);
        context = rootView.getContext();
        dbHelper = new DBHelper(context);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        spinnerLibros = rootView.findViewById(R.id.spinnerLibros);
        editTextFechaPrestamo = rootView.findViewById(R.id.editTextText12);
        radioGroupDias = rootView.findViewById(R.id.radioGroupDias);

        // Obtener la fecha actual
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        fechaActual = dateFormat.format(new Date());
        editTextFechaPrestamo.setText(fechaActual);

        // Configura Retrofit para obtener la lista de libros
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://proleptic-coil.000webhostapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        BookService bookService = retrofit.create(BookService.class);

        // Realiza la solicitud para obtener la lista de libros
        Call<List<Book>> call = bookService.getBooks();
        call.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(@NonNull Call<List<Book>> call, @NonNull Response<List<Book>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Book> books = response.body();

                    if (books != null && !books.isEmpty()) {
                        List<String> bookTitles = new ArrayList<>();
                        for (Book book : books) {
                            String title = book.getTitle();
                            if (title != null) {
                                bookTitles.add(title);
                            }
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, bookTitles);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerLibros.setAdapter(adapter);

                        // Agregar listeners a los botones después de configurar el adapter
                        Button buttonSolicitarPrestamo = rootView.findViewById(R.id.buttonSolicitarPrestamo);
                        buttonSolicitarPrestamo.setOnClickListener(v -> {
                            solicitarPrestamo();

                            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.fragment_container, new HomeFragment());
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        });

                        Button buttonRegresarMenu = rootView.findViewById(R.id.button6);
                        buttonRegresarMenu.setOnClickListener(v -> requireActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, new HomeFragment())
                                .commit());
                    } else {
                        Toast.makeText(context, "La lista de títulos de libros está vacía o es nula", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Error en la respuesta", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Book>> call, @NonNull Throwable t) {
                Toast.makeText(context, "Error en la solicitud", Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

    private void solicitarPrestamo() {
        if (spinnerLibros.getSelectedItem() == null) {
            Toast.makeText(context, "No hay libros disponibles", Toast.LENGTH_SHORT).show();
            return;
        }

        String idSolicitud = UUID.randomUUID().toString();
        String libro = spinnerLibros.getSelectedItem().toString();
        String fechaPrestamo = fechaActual;

        RadioButton selectedRadioButton = rootView.findViewById(radioGroupDias.getCheckedRadioButtonId());
        String duracionPrestamo = (selectedRadioButton != null) ? selectedRadioButton.getText().toString() : "";

        if (libro.isEmpty() || fechaPrestamo.isEmpty() || duracionPrestamo.isEmpty()) {
            Toast.makeText(context, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;


        }

        // Insertar datos en la base de datos SQLite
        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(DBHelper.COLUMN_LIBRO, libro);
            values.put(DBHelper.COLUMN_FECHA_PRESTAMO, fechaPrestamo);
            values.put(DBHelper.COLUMN_DURACION, duracionPrestamo);

            // Insertar datos y obtener el ID del nuevo préstamo
            long newRowId = db.insert(DBHelper.TABLE_PRESTAMOS, null, values);

            if (newRowId != -1) {
                // Guarda el último libro prestado y su ID en SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(getString(R.string.last_borrowed_book), libro);
                editor.putString(getString(R.string.last_borrowed_id), String.valueOf(newRowId));
                editor.apply();

                // Muestra el cuadro de diálogo con la información del préstamo
                mostrarInfoPrestamo(libro, String.valueOf(newRowId));

                // Actualiza la vista en el fragmento de configuración
                actualizarVistasEnSettings(libro, String.valueOf(newRowId));
            } else {
                Toast.makeText(context, "Error al solicitar el préstamo", Toast.LENGTH_SHORT).show();
            }
        } catch (SQLException e) {
            Toast.makeText(context, "Error al acceder a la base de datos", Toast.LENGTH_SHORT).show();
        }
        String fechaDevolucion = calcularFechaDevolucion(fechaPrestamo, duracionPrestamo);
        DataHolder.getInstance().setFechaDevolucion(fechaDevolucion);
        Bundle bundle = new Bundle();
        bundle.putString("editFechaDevolucion", fechaDevolucion);

        // Crear una instancia del fragmento solicituddevolucion y asignarle el Bundle
        DevolucionFragment solicitudDevolucionFragment = new DevolucionFragment();
        solicitudDevolucionFragment.setArguments(bundle);

        // Reemplazar el fragmento actual con el fragmento solicituddevolucion
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, solicitudDevolucionFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    private void limpiarDatos() {
        // Elimina el último libro prestado y su ID de SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(getString(R.string.last_borrowed_book));
        editor.remove(getString(R.string.last_borrowed_id));
        editor.apply();

        // Actualiza la vista en el fragmento de configuración
        actualizarVistas("", "");
    }

    private void mostrarInfoPrestamo(String libro, String idPrestamo) {
        // Crear el cuadro de diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Información de Préstamo");
        builder.setMessage("Su libro: " + libro + " con ID de préstamo: " + idPrestamo + " está listo para recoger.");

        // Agregar el botón de recogido
        builder.setPositiveButton("Perfecto", (dialog, which) -> {
            // Eliminar el último libro prestado y su ID de SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(getString(R.string.last_borrowed_book));
            editor.remove(getString(R.string.last_borrowed_id));
            editor.apply();
            limpiarDatos();

            // Actualiza la vista en el fragmento de configuración
            actualizarVistas("", "");

            dialog.dismiss();
        });

        // Mostrar el cuadro de diálogo
        builder.create().show();
    }

    private void actualizarVistas(String ultimoLibroPrestado, String idPrestamo) {
        TextView textViewUstedDebe = rootView.findViewById(R.id.textViewUstedDebe);
        TextView textViewNombreLibro = rootView.findViewById(R.id.textViewNombreLibro);
        TextView textViewIdPrestamo = rootView.findViewById(R.id.textViewIdPrestamo);

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

    public void actualizarVistasPublic(String ultimoLibroPrestado, String idPrestamo) {
        actualizarVistas(ultimoLibroPrestado, idPrestamo);
    }

    private void actualizarVistasEnSettings(String ultimoLibroPrestado, String idPrestamo) {
        Fragment settingsFragment = getActivity().getSupportFragmentManager().findFragmentByTag(SettingsFragment.class.getSimpleName());
        if (settingsFragment != null && settingsFragment.isVisible()) {
            PrestamoFragment prestamoFragment = (PrestamoFragment) getParentFragment();
            if (prestamoFragment != null) {
                prestamoFragment.actualizarVistasPublic(ultimoLibroPrestado, idPrestamo);
            }
        }
    }


    private String calcularFechaDevolucion(String fechaPrestamo, String duracionPrestamo) {
        // Obtener la duración del préstamo en días
        int diasPrestamo = 0;
        switch (duracionPrestamo) {
            case "5 días":
                diasPrestamo = 5;
                break;
            case "10 días":
                diasPrestamo = 10;
                break;
            case "15 días":
                diasPrestamo = 15;
                break;
        }

        // Calcular la fecha de devolución
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date fechaPrestamoDate = sdf.parse(fechaPrestamo);
            if (fechaPrestamoDate != null) {
                long tiempoEnMilisegundos = fechaPrestamoDate.getTime();
                long tiempoDevolucion = tiempoEnMilisegundos + (diasPrestamo * 24 * 60 * 60 * 1000);
                Date fechaDevolucionDate = new Date(tiempoDevolucion);
                return sdf.format(fechaDevolucionDate);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // En caso de error, devolver una cadena vacía
        return "";
    }



}
