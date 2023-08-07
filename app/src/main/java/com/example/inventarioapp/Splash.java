package com.example.inventarioapp;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class Splash extends AppCompatActivity {

    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String KEY_FIRST_RUN = "firstRun";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        final boolean firstRun = settings.getBoolean(KEY_FIRST_RUN, true);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (firstRun) {
                    // La actividad se muestra por primera vez
                    // Realizar las acciones que deseas realizar solo la primera vez aquí
                    Intent intent = new Intent(Splash.this, Login.class);
                    startActivity(intent);
                    finish();
                    // Establecer la bandera "firstRun" a false
                    //SharedPreferences.Editor editor = settings.edit();
                    //editor.putBoolean(KEY_FIRST_RUN, false);
                    //editor.apply();
                } else {
                    // La actividad ya se ha mostrado antes
                    // Realizar las acciones que deseas realizar en las siguientes ejecuciones aquí
                    Intent intent = new Intent(Splash.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 2000);
    }
}
