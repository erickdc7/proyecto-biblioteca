package com.erickdiaz.proyectobiblioteca;

import retrofit2.Call;
import retrofit2.http.GET;
import java.util.List;

public interface BookService {
    @GET("Obtener_libros.php")
    Call<List<Book>> getBooks();
}

