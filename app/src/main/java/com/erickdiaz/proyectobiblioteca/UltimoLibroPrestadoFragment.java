package com.erickdiaz.proyectobiblioteca;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.content.SharedPreferences;
import android.content.Context;

import androidx.fragment.app.Fragment;

public class UltimoLibroPrestadoFragment extends Fragment {

    private TextView textViewMensaje;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_settings, container, false);

        textViewMensaje = vista.findViewById(R.id.textViewNombreLibro);

        // Recupera los detalles del último libro prestado desde SharedPreferences
        mostrarUltimoLibroPrestado();

        return vista;
    }

    private void mostrarUltimoLibroPrestado() {
        SharedPreferences preferencias = requireContext().getSharedPreferences("UltimoLibroPrestado", Context.MODE_PRIVATE);

        // Recupera los detalles desde SharedPreferences
        String titulo = preferencias.getString("titulo", "");
        String mensaje = "USTED DEBE: " + titulo;

        // Muestra el mensaje en el TextView
        textViewMensaje.setText(mensaje);
    }
}
