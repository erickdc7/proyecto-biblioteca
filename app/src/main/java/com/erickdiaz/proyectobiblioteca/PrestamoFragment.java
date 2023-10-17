package com.erickdiaz.proyectobiblioteca;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import android.content.Intent;
import android.content.Context;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
public class PrestamoFragment extends Fragment {

    private Spinner spinnerLibros;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.solicitudprestamo, container, false);
        context = rootView.getContext();
        Button buttonSolicitarPrestamo = rootView.findViewById(R.id.buttonSolicitarPrestamo);
        Button button6 = rootView.findViewById(R.id.button6);


        spinnerLibros = rootView.findViewById(R.id.spinnerLibros);

        // Configura Retrofit para obtener la lista de libros
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://proleptic-coil.000webhostapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        BookService bookService = retrofit.create(BookService.class);

        // Realiza la solicitud para obtener la lista de libros
        Call<List<Book>> call = bookService.getBooks();
        call.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(@NonNull Call<List<Book>> call, @NonNull Response<List<Book>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Book> books = response.body();

                    if (books != null && !books.isEmpty()) {
                        Log.d("Retrofit", "Títulos de libros obtenidos: " + books.size());

                        // Obtén los títulos de los libros desde la respuesta del servicio web
                        List<String> bookTitles = new ArrayList<>();
                        for (Book book : books) {
                            String title = book.getTitle();
                            if (title != null) {
                                bookTitles.add(title);
                            }
                        }

                        // Crea un adaptador personalizado para el Spinner
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, bookTitles);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerLibros.setAdapter(adapter);

                        for (int i = 0; i < adapter.getCount(); i++) {
                            Log.d("SpinnerItem", adapter.getItem(i));
                        }
                    } else {
                        Toast.makeText(context, "La lista de títulos de libros está vacía o es nula", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Error en la respuesta", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Book>> call, @NonNull Throwable t) {
                Log.e("Retrofit", "Error en la solicitud", t);
                Toast.makeText(context, "Error en la solicitud", Toast.LENGTH_SHORT).show();
            }
        });

        buttonSolicitarPrestamo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mostrar un mensaje de solicitud exitosa (puedes usar un Toast)
                Toast.makeText(context, "Solicitud Exitosa", Toast.LENGTH_SHORT).show();

                // Redirigir al usuario a la pantalla de inicio (HomeFragment)
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new HomeFragment())
                        .commit();
            }
        });
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Crea una instancia del fragmento que deseas mostrar (en este caso, fragment_home)
                Fragment fragmentHome = new HomeFragment();

                // Crea una transacción de fragmento
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                // Reemplaza el fragmento actual por fragment_home
                transaction.replace(R.id.fragment_container, fragmentHome);

                // Agrega la transacción a la pila de retroceso (para permitir volver)
                transaction.addToBackStack(null);

                // Realiza la transacción
                transaction.commit();
            }
        });



        return rootView;

    }



}
