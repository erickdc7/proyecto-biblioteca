package com.erickdiaz.proyectobiblioteca;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import android.util.Log;

public class MenuActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BookAdapter bookAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Carga los libros desde la API
        loadBooksFromApi();
    }

    private void loadBooksFromApi() {
        String apiUrl = "https://proyecto-biblioteca-utp.000webhostapp.com/Obtener_libros.php";
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, apiUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                List<Book> books = parseBooksFromJson(response);
                Log.d("BookAdapter", "Number of books: " + books.size()); // Agregar este registro
                bookAdapter = new BookAdapter(books);
                recyclerView.setAdapter(bookAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Manejar errores de solicitud
            }
        });

        queue.add(jsonArrayRequest);
    }

    private List<Book> parseBooksFromJson(JSONArray jsonArray) {
        List<Book> books = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject bookObject = jsonArray.getJSONObject(i);
                int id = bookObject.getInt("id");
                String title = bookObject.getString("titulo");
                String author = bookObject.getString("autor");
                String category = bookObject.getString("categoria");
                int availability = bookObject.getInt("disponibilidad");

                books.add(new Book(id, title, author, category, availability));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return books;
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
