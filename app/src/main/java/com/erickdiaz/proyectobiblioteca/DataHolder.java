package com.erickdiaz.proyectobiblioteca;

public class DataHolder {
    private static DataHolder instance;
    private String fechaDevolucion;

    private DataHolder() {}

    public static DataHolder getInstance() {
        if (instance == null) {
            instance = new DataHolder();
        }
        return instance;
    }

    public String getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(String fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }
}