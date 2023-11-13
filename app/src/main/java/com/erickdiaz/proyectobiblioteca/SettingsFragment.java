package com.erickdiaz.proyectobiblioteca;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

public class SettingsFragment extends Fragment {

    private TextView textViewUstedDebe;
    private TextView textViewNombreLibro;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        textViewUstedDebe = rootView.findViewById(R.id.textViewUstedDebe);
        textViewNombreLibro = rootView.findViewById(R.id.textViewNombreLibro);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());

        // Obtener el último libro prestado desde SharedPreferences
        String ultimoLibroPrestado = sharedPreferences.getString(getString(R.string.last_borrowed_book), "");

        // Actualizar los TextViews
        actualizarVistas(ultimoLibroPrestado);

        return rootView;
    }

    private void actualizarVistas(String ultimoLibroPrestado) {
        if (ultimoLibroPrestado.isEmpty()) {
            textViewUstedDebe.setVisibility(View.GONE);
            textViewNombreLibro.setText("No ha realizado préstamos recientes");
        } else {
            textViewUstedDebe.setVisibility(View.VISIBLE);
            textViewNombreLibro.setText(ultimoLibroPrestado);
        }
    }
}
