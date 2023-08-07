package com.example.inventarioapp;

import android.view.MenuItem;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;

public class Drawer {

    private ActionBarDrawerToggle drawerToggle;
    private MainActivity activity;

    private Herramientas herramientas;
    private Ajuste ajuste;
    private DrawerLayout drawerLayout;

    public void setupDrawer(MainActivity activity, DrawerLayout drawerLayout) {
        this.activity = activity;
        this.drawerLayout = drawerLayout;

        drawerToggle = new ActionBarDrawerToggle(
                activity,
                drawerLayout,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }



    public void setupDrawer(Herramientas herramientas, DrawerLayout drawerLayout) {
        this.herramientas = herramientas;
        this.drawerLayout = drawerLayout;

        drawerToggle = new ActionBarDrawerToggle(
                herramientas,
                drawerLayout,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        return drawerToggle.onOptionsItemSelected(item);
    }
    public void setupDrawer(Ajuste ajuste, DrawerLayout drawerLayout) {
        this.ajuste = ajuste;
        this.drawerLayout = drawerLayout;

        drawerToggle = new ActionBarDrawerToggle(
                ajuste,
                drawerLayout,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }
}
