package com.erickdiaz.proyectobiblioteca;

public class Book {
    private int id;
    private String title;
    private String author;
    private String category;
    private int availability;
    private String coverImageUrl;

    public Book(int id, String title, String author, String category, int availability, String coverImageUrl) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.category = category;
        this.availability = availability;
        this.coverImageUrl = coverImageUrl;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getAvailability() {
        return availability;
    }

    public void setAvailability(int availability) {
        this.availability = availability;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }
}

