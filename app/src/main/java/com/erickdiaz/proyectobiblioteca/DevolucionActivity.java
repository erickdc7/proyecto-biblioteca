package com.erickdiaz.proyectobiblioteca;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DevolucionActivity extends AppCompatActivity {

    EditText campo2, campo3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.solicituddevolucion);
        campo2 = (EditText) findViewById(R.id.editNombreLibro);
        campo3 = (EditText) findViewById(R.id.editFechaDevolucion);
        TextView editFechaDevolucion = findViewById(R.id.editFechaDevolucion);

        // Formatea la fecha actual
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String fechaActual = dateFormat.format(new Date());

        // Establece la fecha en el TextView
        editFechaDevolucion.setText(fechaActual);
    }

    private void mostrarfecha(View v) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String fecha = dateFormat.format(new Date());

        TextView editFechaPrestamo = findViewById(R.id.editFechaDevolucion);
        editFechaPrestamo.setText(fecha);
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

    public void irMenu(View v) {
        Intent intent = new Intent(this, OpcionesActivity.class);
        startActivity(intent);
    }

}
