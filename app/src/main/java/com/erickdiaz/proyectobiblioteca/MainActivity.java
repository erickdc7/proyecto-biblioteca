package com.erickdiaz.proyectobiblioteca;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    EditText campo1,campo2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        campo1=(EditText)findViewById(R.id.editUsuario);
        campo2=(EditText)findViewById(R.id.editNombreLibro);
        TextView editFechaPrestamo = findViewById(R.id.editFechaPrestamo);

        // Formatea la fecha actual
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String fechaActual = dateFormat.format(new Date());

        // Establece la fecha en el TextView
        editFechaPrestamo.setText(fechaActual);
    }
    private void mostrarfecha(View v){
        SimpleDateFormat dateFormat=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String fecha=dateFormat.format(new Date());

        TextView editFechaPrestamo=findViewById(R.id.editFechaPrestamo);
        editFechaPrestamo.setText(fecha);
    }
    public void agregar(View v){
        if (validar()){
            Toast.makeText(this,"Datos agregados",Toast.LENGTH_SHORT).show();
        }
    }
    public boolean validar(){
        String c1=campo1.getText().toString();
        String c2=campo2.getText().toString();
        if(c1.isEmpty()){
            campo1.setError("Este campo es obligatorio");
            return false;
        }
        if(c2.isEmpty()){
            campo2.setError("Este campo es obligatorio");
            return false;
        }
        return true;
    }
    public void irMenu(View v){
        Intent men=new Intent(this,MenuActivity.class);
        startActivity(men);
    }
    public void irLogin(View v){
        Intent men=new Intent(this,LoginActivity.class);
        startActivity(men);
    }
    public void iraRegistro(View v){
        Intent men=new Intent(this,UsuarioActivity.class);
        startActivity(men);
    }

}