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


        listaLibros = new ArrayList<>();
        listaLibros.add(new Libro("Don Quijote de la Mancha", "Miguel de Cervantes"));
        listaLibros.add(new Libro("Cien años de soledad", "Gabriel García Márquez"));
        listaLibros.add(new Libro("1984", "George Orwell"));
        listaLibros.add(new Libro("Matar un ruiseñor", "Harper Lee"));
        listaLibros.add(new Libro("El Gran Gatsby", "F. Scott Fitzgerald"));
        listaLibros.add(new Libro("Los juegos del hambre", "Suzanne Collins"));
        listaLibros.add(new Libro("El Señor de los Anillos", "J.R.R. Tolkien"));
        listaLibros.add(new Libro("Harry Potter y la piedra filosofal", "J.K. Rowling"));

        // Configura el RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        libroAdapter = new LibroAdapter(listaLibros);
        recyclerView.setAdapter(libroAdapter);
    }

}
