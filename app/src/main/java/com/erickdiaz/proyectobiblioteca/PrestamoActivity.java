package com.erickdiaz.proyectobiblioteca;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import android.util.Log;

public class PrestamoActivity extends AppCompatActivity {
    private Spinner spinnerLibros;
    private EditText editTextFechaPrestamo;
    private EditText editTextFechaDevolucion;
    private List<Book> libros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.solicitudprestamo);

        spinnerLibros = findViewById(R.id.spinnerLibros);
        editTextFechaPrestamo = findViewById(R.id.editTextText12);
        editTextFechaDevolucion = findViewById(R.id.editTextText13);

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
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Book> books = response.body();

                    // Verificar si la lista de libros no es nula antes de obtener su tamaño
                    if (books != null) {
                        Log.d("Retrofit", "Libros obtenidos: " + books.size());

                        // Crea un adaptador para el Spinner
                        ArrayAdapter<Book> adapter = new ArrayAdapter<>(PrestamoActivity.this, android.R.layout.simple_spinner_item, books);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerLibros.setAdapter(adapter);
                        // Configura el adaptador para el Spinner
                        spinnerLibros.setAdapter(adapter);
                    } else {
                        // Manejar el caso en el que la lista de libros es nula
                    }
                } else {
                    Toast.makeText(PrestamoActivity.this, "Error en la respuesta", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                Log.e("Retrofit", "Error en la solicitud: " + t.getMessage());
                t.printStackTrace();
                // Handle errors
                Toast.makeText(PrestamoActivity.this, "Error en la solicitud", Toast.LENGTH_SHORT).show();

            }
        });

        Button buttonSolicitarPrestamo = findViewById(R.id.button5);
        buttonSolicitarPrestamo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Realiza la lógica de solicitud de préstamo aquí
                String libroSeleccionado = spinnerLibros.getSelectedItem().toString();
                String fechaPrestamo = editTextFechaPrestamo.getText().toString();
                String fechaDevolucion = editTextFechaDevolucion.getText().toString();

                // Realiza la lógica para enviar la solicitud de préstamo al servidor

                // Por ahora, muestra un mensaje de éxito
                Toast.makeText(PrestamoActivity.this, "Solicitud de préstamo enviada.", Toast.LENGTH_SHORT).show();
            }
        });

        Button buttonRegresarMenu = findViewById(R.id.button6);
        buttonRegresarMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Regresar al menú principal
                finish();
            }
        });
    }
}
