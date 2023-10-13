package com.erickdiaz.proyectobiblioteca;

public class Book {
    private int coverResource;
    private String title;
    private String author;

    public Book(int coverResource, String title, String author) {
        this.coverResource = coverResource;
        this.title = title;
        this.author = author;
    }

    public int getCoverResource() {
        return coverResource;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }
}

