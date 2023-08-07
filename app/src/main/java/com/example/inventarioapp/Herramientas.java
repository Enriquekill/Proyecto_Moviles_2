package com.example.inventarioapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Herramientas extends AppCompatActivity {

    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String KEY_FIRST_RUN = "firstRun";

    NotasFragment Conversor = new NotasFragment();
    CalculateFragment Calculadora = new CalculateFragment();
    ConversorFragment Crono = new ConversorFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_herramientas);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        DrawerLayout drawerLayouts = findViewById(R.id.drawer_layout);
        Drawer drawerManager = new Drawer();
        drawerManager.setupDrawer(this, drawerLayouts);

        TopBar topBar = new TopBar();
        topBar.setupTopBar(this);
        topBar.setNavigationIcon(
                ContextCompat.getDrawable(this, R.drawable.baseline_menu_24),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
                        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                            drawerLayout.closeDrawer(GravityCompat.START);
                        } else {
                            drawerLayout.openDrawer(GravityCompat.START);
                        }
                    }
                }
        );

        BottomNavigationView navigation = findViewById(R.id.bottomNavigationView);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        loadFragment(Calculadora);
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.Calcula:
                    loadFragment(Calculadora);
                    return true;
                case R.id.convert:
                    loadFragment(Conversor);
                    return true;
                case R.id.crono:
                    loadFragment(Crono);
                    return true;

            }
            return false;
        }
    };

    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.commit();
    }

    public void logout() {
        // Código para cerrar sesión
    }

    public void doThis(MenuItem item) {
        Intent intent = new Intent(Herramientas.this, Login.class);
        startActivity(intent);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(KEY_FIRST_RUN, true);
        editor.apply();
    }

    public void doThis1(MenuItem item) {
        Intent intent = new Intent(Herramientas.this, MainActivity.class);
        startActivity(intent);
    }

    public void openHerramienta(MenuItem item) {
        // Código para abrir el fragmento de la Calculadora
        Intent intent = new Intent(Herramientas.this, Herramientas.class);
        startActivity(intent);

    }

    public void openAjuste(MenuItem item) {
        // Código para abrir el fragmento de la Calculadora
        Intent intent = new Intent(Herramientas.this, MainActivity.class);
        startActivity(intent);
    }
}


