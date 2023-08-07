package com.example.inventarioapp;

import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;

public class TopBar {

    private MaterialToolbar topAppBar;

    public void setupTopBar(AppCompatActivity activity) {
        topAppBar = activity.findViewById(R.id.topAppBar);
        activity.setSupportActionBar(topAppBar);
    }

    public void setNavigationIcon(Drawable icon, View.OnClickListener listener) {
        topAppBar.setNavigationIcon(icon);
        topAppBar.setNavigationOnClickListener(listener);
    }
}
