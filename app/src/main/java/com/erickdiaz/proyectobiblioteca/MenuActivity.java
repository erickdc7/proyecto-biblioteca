package com.erickdiaz.proyectobiblioteca;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BookAdapter bookAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Aquí debes crear una lista ficticia de libros o cargarla desde una fuente de datos.
        List<Book> books = createSampleBookList();

        bookAdapter = new BookAdapter(books);
        recyclerView.setAdapter(bookAdapter);
    }

    // Método para crear una lista ficticia de libros
    private List<Book> createSampleBookList() {
        List<Book> books = new ArrayList<>();
        // Agrega libros ficticios a la lista aquí.
        books.add(new Book(R.drawable.portada_1984, "1984", "George Orwell"));
        books.add(new Book(R.drawable.portada_matar_ruisenor, "Matar un ruiseñor", "Harper Lee"));
        books.add(new Book(R.drawable.portada_cien_anios_soledad, "Cien años de soledad", "Gabriel García Márquez"));
        books.add(new Book(R.drawable.portada_gran_gatsby, "El Gran Gatsby", "F. Scott Fitzgerald"));
        books.add(new Book(R.drawable.portada_don_quijote, "Don Quijote de la Mancha", "Miguel de Cervantes Saavedra"));
        books.add(new Book(R.drawable.portada_crimen_castigo, "Crimen y castigo", "Fyodor Dostoevsky"));
        books.add(new Book(R.drawable.portada_mujer_punto_cero, "Mujer en punto cero", "Nawal El Saadawi"));
        books.add(new Book(R.drawable.portada_odisea, "La Odisea", "Homero"));
        books.add(new Book(R.drawable.portada_tiempo_perdido, "En busca del tiempo perdido", "Marcel Proust"));
        books.add(new Book(R.drawable.portada_matar_elefante, "Matar a un elefante", "George Orwell"));
        // Agrega más libros si es necesario.
        return books;
    }

    public void irLogin(View v){
        Intent men = new Intent(this, LoginActivity.class);
        startActivity(men);
    }

    public void iraRegistro(View v){
        Intent reg = new Intent(this, UsuarioActivity.class);
        startActivity(reg);
    }
}
