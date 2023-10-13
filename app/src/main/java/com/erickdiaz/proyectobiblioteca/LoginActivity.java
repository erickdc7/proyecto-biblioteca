package com.erickdiaz.proyectobiblioteca;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
    }

    public void irMenuS(View v){
        Intent men=new Intent(this,MenuActivity.class);
        startActivity(men);
    }

    public void irMenuP(View v){
        Intent men=new Intent(this,MainActivity.class);
        startActivity(men);
    }
    public void RegistrarUsu(View v){
        Intent men=new Intent(this,UsuarioActivity.class);
        startActivity(men);
    }
}