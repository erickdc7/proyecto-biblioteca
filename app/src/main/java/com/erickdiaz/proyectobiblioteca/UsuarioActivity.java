package com.erickdiaz.proyectobiblioteca;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.erickdiaz.proyectobiblioteca.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UsuarioActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText dniEditText;
    private EditText direccionEditText;
    private EditText telefonoEditText;
    private EditText correoEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrousuario);

        usernameEditText = findViewById(R.id.editTextUsername);
        dniEditText = findViewById(R.id.DNI);
        direccionEditText = findViewById(R.id.Direccion);
        telefonoEditText = findViewById(R.id.Telefono);
        correoEditText = findViewById(R.id.Correo);
        passwordEditText = findViewById(R.id.editTextPassword);
    }

    public void registrarUsuario(View v) {
        final String username = usernameEditText.getText().toString();
        final String dni = dniEditText.getText().toString();
        final String direccion = direccionEditText.getText().toString();
        final String telefono = telefonoEditText.getText().toString();
        final String correo = correoEditText.getText().toString();
        final String password = passwordEditText.getText().toString();

        String url = "https://proleptic-coil.000webhostapp.com/loginusuario.php";

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            String message = jsonResponse.getString("message");

                            if (success) {
                                // Registro de usuario exitoso, puedes mostrar un mensaje o redirigir a otra actividad
                                Intent intent = new Intent(UsuarioActivity.this, LoginActivity.class);
                                startActivity(intent);
                            } else {
                                // Mostrar mensaje de error
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("dni", dni);
                params.put("direccion", direccion);
                params.put("telefono", telefono);
                params.put("correo", correo);
                params.put("password", password);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}
