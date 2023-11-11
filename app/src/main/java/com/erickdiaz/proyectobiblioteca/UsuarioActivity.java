package com.erickdiaz.proyectobiblioteca;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UsuarioActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText dniEditText;
    private EditText codigoEditText;
    private EditText telefonoEditText;
    private EditText correoEditText;
    private EditText passwordEditText;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrousuario);

        // Inicializar el DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        usernameEditText = findViewById(R.id.editTextUsername);
        dniEditText = findViewById(R.id.DNI);
        codigoEditText = findViewById(R.id.Codigo);
        telefonoEditText = findViewById(R.id.Telefono);
        correoEditText = findViewById(R.id.Correo);
        passwordEditText = findViewById(R.id.editTextPassword);
    }

    public void registrarUsuario(View v) {
        final String username = usernameEditText.getText().toString();
        final String dni = dniEditText.getText().toString();
        final String codigo = codigoEditText.getText().toString();
        final String telefono = telefonoEditText.getText().toString();
        final String correo = correoEditText.getText().toString();
        final String password = passwordEditText.getText().toString();

        // Validación de entrada
        if (username.isEmpty() || dni.isEmpty() || codigo.isEmpty() || telefono.isEmpty() || correo.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        // Contraseña segura (por ejemplo, al menos 6 caracteres)
        if (password.length() < 6) {
            Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validaciones específicas
        if (dni.length() != 8 || !TextUtils.isDigitsOnly(dni)) {
            Toast.makeText(this, "El DNI debe tener 7 dígitos y ser numérico", Toast.LENGTH_SHORT).show();
            return;
        }

        if (codigo.length() > 0 && (codigo.charAt(0) != 'u' || codigo.length() > 9 || !TextUtils.isDigitsOnly(codigo.substring(1)))) {
            Toast.makeText(this, "El código debe empezar con 'u' y tener máximo 9 dígitos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (telefono.length() > 9 || !TextUtils.isDigitsOnly(telefono)) {
            Toast.makeText(this, "El teléfono debe tener máximo 9 dígitos y ser numérico", Toast.LENGTH_SHORT).show();
            return;
        }

        long result = databaseHelper.addUser(username, dni, codigo, telefono, correo, password);

        if (result != -1) {
            Toast.makeText(this, "Registro local exitoso", Toast.LENGTH_SHORT).show();

            // Redirige a la actividad de inicio de sesión
            Intent intent = new Intent(UsuarioActivity.this, LoginActivity.class);
            startActivity(intent);

            // Puedes redirigir a otra actividad o realizar otras acciones si es necesario
        } else {
            Toast.makeText(this, "Error al registrar localmente", Toast.LENGTH_SHORT).show();
        }
    }
}