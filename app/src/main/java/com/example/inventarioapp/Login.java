package com.example.inventarioapp;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.biometric.BiometricManager;
import androidx.core.content.ContextCompat;
import androidx.biometric.BiometricPrompt;


public class Login extends AppCompatActivity {
    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String KEY_FIRST_RUN = "firstRun";

    EditText txtMail, txtPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        txtMail = findViewById(R.id.inCorreo);
        txtPass = findViewById(R.id.inContrasena);

        String huella = settings.getString("huella", "");

        if(huella != ""){
            showBiometricPrompt();
            checkBiometricSupport();
        }

    }

    private void showBiometricPrompt() {
        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Autenticación con huella digital")
                .setSubtitle("Coloca tu huella en el lector")
                .setNegativeButtonText("Cancelar")
                .build();

        BiometricPrompt biometricPrompt = new BiometricPrompt(this,
                ContextCompat.getMainExecutor(this), new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean(KEY_FIRST_RUN, false);
                String huella = settings.getString("huella", "");

                editor.putString("id_usuario", huella);
                editor.apply();

                if(huella != ""){

                    Intent intent = new Intent(Login.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                }
            }

            @Override
            public void onAuthenticationFailed() {
                // La autenticación falló. Puedes mostrar un mensaje de error aquí.
            }

            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                // Ocurrió un error en la autenticación. Puedes manejarlo aquí.
            }
        });

        biometricPrompt.authenticate(promptInfo);
    }


    private void checkBiometricSupport() {
        BiometricManager biometricManager = BiometricManager.from(this);

        switch (biometricManager.canAuthenticate()) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                //Toast.makeText(this, "Autenticación exitosa", Toast.LENGTH_SHORT).show();

                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                // El dispositivo no tiene un sensor de huellas digitales.
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                // El sensor de huellas digitales no está disponible actualmente.
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                // No se han registrado huellas digitales en el dispositivo.
                break;
        }


    }

    private void validarUsuario(String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.isEmpty()){
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String id = jsonObject.getString("id_usuario");

                        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putBoolean(KEY_FIRST_RUN, false);
                        editor.putString("id_usuario", id);

                        editor.apply();

                        String huella = settings.getString("huella", "");

                        if(huella != ""){
                            Intent intent = new Intent(Login.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                            return;
                        }

                        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);

                        // Personalizamos el layout del modal con un LayoutInflater
                        LayoutInflater inflater = getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.dialog_confirmation, null);
                        builder.setView(dialogView);

                        // Configuramos los botones de "Aceptar" y "Cancelar"
                        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Acciones a realizar cuando se pulsa el botón "Aceptar"
                                editor.putString("huella", id);
                                editor.apply();

                                Toast.makeText(getApplicationContext(), "Huella Activada", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(Login.this, MainActivity.class);
                                startActivity(intent);
                                finish();

                            }
                        });

                        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Login.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });

                        // Mostramos el modal
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();




                    }catch (Exception e){
                        Toast.makeText(Login.this, "Usuario0 invalido", Toast.LENGTH_SHORT).show();
                        System.out.println("ERROR: " + e);
                    }

                }
                else
                    Toast.makeText(Login.this, "Usuario invalido", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Login.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("usuario", txtMail.getText().toString());
                params.put("password", txtPass.getText().toString());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void Login(View view){

        if(txtMail.getText().toString().isEmpty()) {
            mostrarError("No se ha ingresado el correo");
            return;
        }
        if(txtPass.getText().toString().isEmpty()) {
            mostrarError("No se ha ingresado la contraseña");
            return;
        }
        validarUsuario("https://ceruminous-helmsman.000webhostapp.com/validar_usuario.php");
    }

    private void mostrarError(String msg){
        View view = findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    public void Register(View view){
       // Intent intent = new Intent(Login.this, Register.class);
        //startActivity(intent);
    }

    public void Recovery(View view){
        Intent intent = new Intent(Login.this, RecoveryPass.class);
        startActivity(intent);
    }
}