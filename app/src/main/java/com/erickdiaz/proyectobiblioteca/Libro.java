package com.erickdiaz.proyectobiblioteca;
public class Libro {
    private String titulo;
    private String autor;
    private int imagenPortada; // Agrega una propiedad para la imagen de portada

    public Libro(String titulo, String autor, int imagenPortada) {
        this.titulo = titulo;
        this.autor = autor;
        this.imagenPortada = imagenPortada;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public int getImagenPortada() {
        return imagenPortada;
    }
}


