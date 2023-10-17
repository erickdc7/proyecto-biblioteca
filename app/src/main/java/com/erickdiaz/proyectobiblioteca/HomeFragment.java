package com.erickdiaz.proyectobiblioteca;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
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

public class HomeFragment extends Fragment {

    private RequestQueue requestQueue;
    private RecyclerView recyclerView;
    private BookAdapter bookAdapter;
    private DrawerLayout drawerLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        requestQueue = Volley.newRequestQueue(getContext());

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

                                bookAdapter = new BookAdapter(getContext(), books);
                                recyclerView.setAdapter(bookAdapter);
                            } else {
                                // Manejar el caso en que el estado no sea 1 (puede ser un error)
                                Toast.makeText(getContext(), "Error al obtener los libros.", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getContext(), "Error de red: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);

        return view;
    }

    // Agrega un método para abrir el menú de opciones desde el encabezado

    public void abrirMenu(View v) {
        drawerLayout = requireActivity().findViewById(R.id.drawer_layout);
        drawerLayout.openDrawer(GravityCompat.START);
    }
}
