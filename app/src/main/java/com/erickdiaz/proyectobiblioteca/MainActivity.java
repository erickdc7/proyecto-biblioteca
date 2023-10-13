package com.erickdiaz.proyectobiblioteca;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LibroAdapter libroAdapter;
    private ArrayList<Libro> listaLibros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializa la lista de libros
        listaLibros = new ArrayList<>();

        // Agrega libros a la lista
        listaLibros.add(new Libro("Don Quijote de la Mancha", "Miguel de Cervantes", R.drawable.portada1));
        listaLibros.add(new Libro("Cien años de soledad", "Gabriel García Márquez", R.drawable.portada1));
        listaLibros.add(new Libro("1984", "George Orwell", R.drawable.portada1));

        // Agrega más libros aquí

        // Configura el RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        libroAdapter = new LibroAdapter(listaLibros);
        recyclerView.setAdapter(libroAdapter);
    }
}
