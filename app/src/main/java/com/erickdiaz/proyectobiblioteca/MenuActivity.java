package com.erickdiaz.proyectobiblioteca;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {
    private RequestQueue requestQueue;
    private RecyclerView recyclerView;
    private BookAdapter bookAdapter;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        requestQueue = Volley.newRequestQueue(this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Realizar una solicitud GET a la API para cargar la lista de libros, similar a tu código anterior
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
                                    String coverImageUrl = bookJson.getString("coverImageUrl");

                                    Book book = new Book(id, title, author, category, availability, coverImageUrl);
                                    books.add(book);
                                }

                                bookAdapter = new BookAdapter(MenuActivity.this, books);
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


    // Agrega un método para abrir el menú de opciones desde el encabezado

    public void abrirMenu(View v) {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.openDrawer(GravityCompat.START);
    }

}