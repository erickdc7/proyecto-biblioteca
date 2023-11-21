package com.erickdiaz.proyectobiblioteca;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import java.text.SimpleDateFormat;
import java.util.Date;


public class DevolucionFragment extends Fragment {
    private View view;
    EditText campo2, campo3 ;
    private EditText editNombreLibro;
    private Context context;
    private SharedPreferences sharedPreferences;
    private DBHelper dbHelper;

    private String fechaActual;

    private EditText editFechaPrestamo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_devolucion, container, false);

        campo2 = view.findViewById(R.id.editNombreLibro);

        editNombreLibro = view.findViewById(R.id.editNombreLibro);

        String fechaDevolucion = DataHolder.getInstance().getFechaDevolucion();
// Usa la fechaDevolucion como sea necesario
        // Obtén la referencia a tu EditText
        EditText editFechaDevolucion = view.findViewById(R.id.editFechaDevolucion);
        editFechaDevolucion.setText(fechaDevolucion);
// Establece la fecha de devolución en el EditText
        Button Botonconfirm = view.findViewById(R.id.Botonconfirm);
        TextView BotonRegresar = view.findViewById(R.id.BotonRegresar);
        editFechaPrestamo = view.findViewById(R.id.editFechaPrestamo);
        // Formatea la fecha actual
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        fechaActual = dateFormat.format(new Date());
        editFechaPrestamo.setText(fechaActual);
        String fechaActual = dateFormat.format(new Date());
        context = view.getContext();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        dbHelper = new DBHelper(requireContext());

        obtenerUltimoLibroPrestadoDesdeBaseDeDatos();
        // Establece la fecha en el TextView


        Botonconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mostrar un mensaje de solicitud exitosa (puedes usar un Toast)
                Toast.makeText(context, "Devolucion Exitosa", Toast.LENGTH_SHORT).show();

                // Redirigir al usuario a la pantalla de inicio (HomeFragment)
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new HomeFragment())
                        .commit();
            }
        });

        BotonRegresar.setOnClickListener(new View.OnClickListener() {
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

        return view;
    }

    public boolean validar() {
        String c2 = campo2.getText().toString();
        String c3 = campo3.getText().toString();
        if (c2.isEmpty()) {
            campo2.setError("Este campo es obligatorio");
            return false;
        }
        if (c3.isEmpty()) {
            campo2.setError("Este campo es obligatorio");
            return false;
        }
        return true;
    }

    public void irMenuPrincipal(View v) {
        Intent intent = new Intent(getActivity(), MenuLateralActivity.class);
        startActivity(intent);
    }


    private void obtenerUltimoLibroPrestadoDesdeBaseDeDatos() {
        try (Cursor cursor = dbHelper.getReadableDatabase().query(
                DBHelper.TABLE_PRESTAMOS,
                new String[]{DBHelper.COLUMN_LIBRO, DBHelper.COLUMN_ID},
                null,
                null,
                null,
                null,
                null,
                null
        )) {
            if (cursor != null && cursor.moveToLast()) {
                int libroColumnIndex = cursor.getColumnIndex(DBHelper.COLUMN_LIBRO);
                int idColumnIndex = cursor.getColumnIndex(DBHelper.COLUMN_ID);

                if (libroColumnIndex != -1 && idColumnIndex != -1) {
                    String ultimoLibroPrestado = cursor.getString(libroColumnIndex);
                    String idPrestamo = cursor.getString(idColumnIndex);

                    // Guardar el último libro prestado en SharedPreferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(getString(R.string.last_borrowed_book), ultimoLibroPrestado);
                    editor.putString(getString(R.string.last_borrowed_id), idPrestamo);
                    editor.apply();

                    // Actualizar la interfaz de usuario
                    if (editNombreLibro != null) {
                        editNombreLibro.setText(ultimoLibroPrestado);
                    }
                }
            }
        }
    }


}

