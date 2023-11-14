package com.erickdiaz.proyectobiblioteca;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class AboutFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about, container, false);

        // Enlazar el botón
        Button btnVisitanos = rootView.findViewById(R.id.BtonVisitanos);

        // Agregar un listener al botón
        btnVisitanos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirGoogleMaps();
            }
        });

        return rootView;
    }

    private void abrirGoogleMaps() {
        try {
            String query = "279 Av Arequipa 265 Lima 15046";
            String encodedQuery = Uri.encode(query);
            String uriString = "geo:0,0?q=" + encodedQuery;
            Uri uri = Uri.parse(uriString);

            Intent mapIntent = new Intent(Intent.ACTION_VIEW, uri);
            mapIntent.setPackage("com.google.android.apps.maps");

            if (mapIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
                startActivity(mapIntent);
            } else {
                abrirGoogleMapsEnNavegadorWeb();
            }
        } catch (Exception e) {
            e.printStackTrace();
            mostrarMensajeError();
        }
    }

    private void abrirGoogleMapsEnNavegadorWeb() {
        try {
            String url = "https://www.google.com/maps/search/?api=1&query=279+Av+Arequipa+265+Lima+15046";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            mostrarMensajeError();
        }
    }

    private void mostrarMensajeError() {
        Toast.makeText(requireContext(), "Error al abrir Google Maps", Toast.LENGTH_SHORT).show();
    }
}
