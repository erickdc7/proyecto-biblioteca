package com.erickdiaz.proyectobiblioteca;
import java.util.List;

public class Book {
    private int id;
    private String title;
    private String author;

    public Book(int coverResource, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
    }
    public class BookResponse {
        private int estado;
        private List<Book> libros;
        // Otros campos y métodos getter y setter.
    }
    public int getCoverResource() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }
}

