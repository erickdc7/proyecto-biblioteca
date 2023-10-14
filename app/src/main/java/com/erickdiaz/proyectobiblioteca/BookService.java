package com.erickdiaz.proyectobiblioteca;

import retrofit2.Call;
import retrofit2.http.GET;
public interface BookService {
    @GET("Obtener_libros.php")
    Call<Book> getBooks();
}
