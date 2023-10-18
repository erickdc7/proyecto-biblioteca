package com.erickdiaz.proyectobiblioteca;

import android.content.Intent;
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

import java.text.SimpleDateFormat;
import java.util.Date;


public class DevolucionFragment extends Fragment {

    EditText campo1,campo2,campo3;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_devolucion, container, false);

        campo1=view.findViewById(R.id.editUsuario);
        campo2=view.findViewById(R.id.editNombreLibro);
        campo3=view.findViewById(R.id.editFechaDevolucion);
        TextView editFechaDevolucion = view.findViewById(R.id.editFechaDevolucion);
        Button Botonconfirm = view.findViewById(R.id.button6);
        TextView BotonRegresar = view.findViewById(R.id.editTextText12);
        // Formatea la fecha actual
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String fechaActual = dateFormat.format(new Date());

        // Establece la fecha en el TextView
        editFechaDevolucion.setText(fechaActual);

        Botonconfirm.setOnClickListener(new View.OnClickListener() {
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



    public boolean validar(){
        String c1=campo1.getText().toString();
        String c2=campo2.getText().toString();
        String c3=campo3.getText().toString();
        if(c1.isEmpty()){
            campo1.setError("Este campo es obligatorio");
            return false;
        }
        if(c2.isEmpty()){
            campo2.setError("Este campo es obligatorio");
            return false;
        }
        if(c3.isEmpty()){
            campo2.setError("Este campo es obligatorio");
            return false;
        }
        return true;
    }

    public void irMenuPrincipal(View v){
        Intent intent =new Intent(getActivity(),MenuLateralActivity.class);
        startActivity(intent);
    }

}
