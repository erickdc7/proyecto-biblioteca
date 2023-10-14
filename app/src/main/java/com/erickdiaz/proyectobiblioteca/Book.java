package com.erickdiaz.proyectobiblioteca;

public class Book {
    private int id;
    private String title;
    private String author;
    private String category;
    private int availability;

    public Book(int id, String title, String author, String category, int availability) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.category = category;
        this.availability = availability;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getCategory() {
        return category;
    }

    public int getAvailability() {
        return availability;
    }
}
