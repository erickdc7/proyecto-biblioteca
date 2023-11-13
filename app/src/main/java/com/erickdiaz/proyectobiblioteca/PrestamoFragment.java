package com.erickdiaz.proyectobiblioteca;

import android.content.ContentValues;
import android.content.Context;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Date;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.solicitudprestamo, container, false);
        context = rootView.getContext();
        dbHelper = new DBHelper(context);

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
                        // Obtén los títulos de los libros desde la respuesta del servicio web
                        List<String> bookTitles = new ArrayList<>();
                        for (Book book : books) {
                            String title = book.getTitle();
                            if (title != null) {
                                bookTitles.add(title);
                            }
                        }

                        // Crea un adaptador personalizado para el Spinner
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, bookTitles);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerLibros.setAdapter(adapter);
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

        // Resto del código...

        return rootView;
    }

    private void solicitarPrestamo() {
        String libro = (spinnerLibros.getSelectedItem() != null) ? spinnerLibros.getSelectedItem().toString() : "";
        String fechaPrestamo = fechaActual;
        RadioButton selectedRadioButton = rootView.findViewById(radioGroupDias.getCheckedRadioButtonId());
        String duracionPrestamo = getDuracionFromRadioButton(selectedRadioButton);

        if (libro.isEmpty() || fechaPrestamo.isEmpty() || duracionPrestamo.isEmpty()) {
            Toast.makeText(context, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Insertar datos en la base de datos SQLite
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DBHelper.COLUMN_LIBRO, libro);
            values.put(DBHelper.COLUMN_FECHA_PRESTAMO, fechaPrestamo);
            values.put(DBHelper.COLUMN_DURACION, duracionPrestamo);

            long newRowId = db.insert(DBHelper.TABLE_PRESTAMOS, null, values);

            if (newRowId != -1) {
                Toast.makeText(context, "Préstamo solicitado con éxito", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Error al solicitar el préstamo", Toast.LENGTH_SHORT).show();
            }
        } catch (SQLException e) {
            Toast.makeText(context, "Error al acceder a la base de datos", Toast.LENGTH_SHORT).show();
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    private String getDuracionFromRadioButton(RadioButton radioButton) {
        if (radioButton == null) {
            return "";
        }

        return radioButton.getText().toString();
    }
}
