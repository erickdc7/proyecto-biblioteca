package com.erickdiaz.proyectobiblioteca;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MenuLateralActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menuprincipal);
    }

    public void irMenuLateral(View v) {
        Intent reg = new Intent(this, OpcionesActivity.class);
        startActivity(reg);
    }

    public void irLogin(View v) {
        Intent reg = new Intent(this, LoginActivity.class);
        startActivity(reg);
    }
}