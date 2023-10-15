package com.erickdiaz.proyectobiblioteca;

import retrofit2.Call;
import retrofit2.http.GET;
import java.util.List;

public interface BookService {
    @GET("https://proleptic-coil.000webhostapp.com/Obtener_libros.php")
    Call<List<Book>> getBooks();
}

