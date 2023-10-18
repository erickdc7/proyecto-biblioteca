package com.erickdiaz.proyectobiblioteca;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.util.Log;

import java.text.SimpleDateFormat;

import android.view.View;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Intent;

public class PrestamoActivity extends AppCompatActivity {
    private Spinner spinnerLibros;
    private EditText editTextFechaPrestamo;
    private EditText editTextFechaDevolucion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.solicitudprestamo);
        TextView editTextFechaPrestamo = findViewById(R.id.editTextText12);

        spinnerLibros = findViewById(R.id.spinnerLibros);

        // Formatea la fecha actual
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String fechaActual = dateFormat.format(new Date());

        // Establece la fecha en el TextView
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
                        Log.d("Retrofit", "Títulos de libros obtenidos: " + books.size());

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

                        for (int i = 0; i < adapter.getCount(); i++) {
                            Log.d("SpinnerItem", adapter.getItem(i));
                        }
                    } else {
                        Toast.makeText(PrestamoActivity.this, "La lista de títulos de libros está vacía o es nula", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(PrestamoActivity.this, "Error en la respuesta", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Book>> call, @NonNull Throwable t) {
                Log.e("Retrofit", "Error en la solicitud", t);
                Toast.makeText(PrestamoActivity.this, "Error en la solicitud", Toast.LENGTH_SHORT).show();
            }
        });

        // Agregar listeners a los botones
        Button buttonSolicitarPrestamo = findViewById(R.id.buttonSolicitarPrestamo);

        buttonSolicitarPrestamo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mostrar un mensaje de solicitud exitosa (puedes usar un Toast)
                Toast.makeText(PrestamoActivity.this, "Solicitud Exitosa", Toast.LENGTH_SHORT).show();

                // Volver al fragmento HomeFragment
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new HomeFragment()) // Reemplaza R.id.fragment_container con el ID de tu contenedor de fragmentos
                        .commit();
            }
        });

        Button buttonRegresarMenu = findViewById(R.id.button6);
        buttonRegresarMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Regresar al fragmento HomeFragment
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new HomeFragment()) // Reemplaza R.id.fragment_container con el ID de tu contenedor de fragmentos
                        .commit();
            }
        });
    }
}