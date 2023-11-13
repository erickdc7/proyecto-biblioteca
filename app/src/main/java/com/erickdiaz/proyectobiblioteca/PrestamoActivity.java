package com.erickdiaz.proyectobiblioteca;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PrestamoActivity extends AppCompatActivity {

    private Spinner spinnerLibros;
    private EditText editTextFechaPrestamo;
    private RadioGroup radioGroupDias;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.solicitudprestamo);

        dbHelper = new DBHelper(this);

        spinnerLibros = findViewById(R.id.spinnerLibros);
        editTextFechaPrestamo = findViewById(R.id.editTextText12);
        radioGroupDias = findViewById(R.id.radioGroupDias);

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
                        StringSpinnerAdapter adapter = new StringSpinnerAdapter(PrestamoActivity.this, bookTitles);
                        spinnerLibros.setAdapter(adapter);
                    } else {
                        Toast.makeText(PrestamoActivity.this, "La lista de títulos de libros está vacía o es nula", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(PrestamoActivity.this, "Error en la respuesta", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Book>> call, @NonNull Throwable t) {
                Toast.makeText(PrestamoActivity.this, "Error en la solicitud", Toast.LENGTH_SHORT).show();
            }
        });

        // Agregar listeners a los botones
        Button buttonSolicitarPrestamo = findViewById(R.id.buttonSolicitarPrestamo);
        buttonSolicitarPrestamo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                solicitarPrestamo();
            }
        });

        Button buttonRegresarMenu = findViewById(R.id.button6);
        buttonRegresarMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Regresar al fragmento HomeFragment
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new HomeFragment())
                        .commit();
            }
        });
    }

    private void solicitarPrestamo() {
        String libro = spinnerLibros.getSelectedItem().toString();
        String fechaPrestamo = editTextFechaPrestamo.getText().toString();
        RadioButton selectedRadioButton = findViewById(radioGroupDias.getCheckedRadioButtonId());
        String duracionPrestamo = getDuracionFromRadioButton(selectedRadioButton);

        if (libro.isEmpty() || fechaPrestamo.isEmpty() || duracionPrestamo.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(this, "Préstamo solicitado con éxito", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error al solicitar el préstamo", Toast.LENGTH_SHORT).show();
            }
        } catch (SQLException e) {
            Toast.makeText(this, "Error al acceder a la base de datos", Toast.LENGTH_SHORT).show();
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

        // Obtenemos el texto del botón seleccionado
        return radioButton.getText().toString();
    }

}
