package com.erickdiaz.proyectobiblioteca;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private RequestQueue requestQueue;
    private String url = "https://proleptic-coil.000webhostapp.com/loginusuario.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        requestQueue = Volley.newRequestQueue(this);
    }

    public void iniciarSesion(View v) {
        final String username = editTextUsername.getText().toString();
        final String password = editTextPassword.getText().toString();

        // Realizar una solicitud POST al servidor
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                // Autenticación exitosa, redirige al menú
                                Intent intent = new Intent(LoginActivity.this, OpcionesActivity.class);
                                startActivity(intent);
                            } else {
                                // Autenticación fallida, muestra un mensaje de error
                                String message = jsonResponse.getString("message");
                                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Error al analizar la respuesta JSON
                            Toast.makeText(LoginActivity.this, "Error al analizar la respuesta del servidor", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejo de errores en la solicitud al servidor
                        Toast.makeText(LoginActivity.this, "Error de red: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Enviar datos POST al servidor
                Map<String, String> params = new HashMap<>();
                params.put("loginUsername", username);
                params.put("loginPassword", password);
                return params;
            }
        };

        // Agrega la solicitud a la cola de solicitudes
        requestQueue.add(stringRequest);
    }

    public void SolicitarUsuario(View v) {
        // Número de teléfono y mensaje predefinido
        String phoneNumber = "51960252970";  // Agrega el código de país según tu necesidad
        String message = "Solicito mis Credenciales para ingresar al App UTP Biblioteca";

        // Forma la URI de WhatsApp
        String uri = "https://api.whatsapp.com/send?phone=" + phoneNumber + "&text=" + Uri.encode(message);

        // Crea un Intent con la acción ACTION_VIEW y la URI de WhatsApp
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(uri));

        // Verifica si la aplicación de WhatsApp está instalada en el dispositivo
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Abre la aplicación de WhatsApp
            startActivity(intent);
        } else {
            // Muestra un mensaje si la aplicación de WhatsApp no está instalada
            Toast.makeText(this, "WhatsApp no está instalado en este dispositivo", Toast.LENGTH_SHORT).show();
        }
    }
}