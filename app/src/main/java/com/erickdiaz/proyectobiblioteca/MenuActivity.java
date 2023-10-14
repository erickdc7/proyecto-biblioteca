package com.erickdiaz.proyectobiblioteca;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {
    private RequestQueue requestQueue;
    private RecyclerView recyclerView;
    private BookAdapter bookAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        requestQueue = Volley.newRequestQueue(this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Realizar una solicitud GET a la API
        String apiUrl = "https://proleptic-coil.000webhostapp.com/Obtener_libros.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, apiUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int estado = response.getInt("estado");
                            if (estado == 1) {
                                JSONArray librosArray = response.getJSONArray("libros");

                                List<Book> books = new ArrayList<>();

                                for (int i = 0; i < librosArray.length(); i++) {
                                    JSONObject bookJson = librosArray.getJSONObject(i);
                                    int id = bookJson.getInt("id");
                                    String title = bookJson.getString("titulo");
                                    String author = bookJson.getString("autor");
                                    String category = bookJson.getString("categoria");
                                    int availability = bookJson.getInt("disponibilidad");

                                    Book book = new Book(id, title, author, category, availability);
                                    books.add(book);
                                }

                                bookAdapter = new BookAdapter(books);
                                recyclerView.setAdapter(bookAdapter);
                            } else {
                                // Manejar el caso en que el estado no sea 1 (puede ser un error)
                                Toast.makeText(MenuActivity.this, "Error al obtener los libros.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar errores, como problemas de red o respuesta incorrecta
                        Toast.makeText(MenuActivity.this, "Error de red: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Agregar la solicitud a la cola
        requestQueue.add(jsonObjectRequest);
    }

    public void irLogin(View v) {
        Intent men = new Intent(this, LoginActivity.class);
        startActivity(men);
    }

    public void iraRegistro(View v) {
        Intent reg = new Intent(this, UsuarioActivity.class);
        startActivity(reg);
    }
}
