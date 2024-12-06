package com.example.pam_weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.pam_weatherapp.fragments.FragmentBottom;
import com.example.pam_weatherapp.fragments.FragmentMiddle;
import com.example.pam_weatherapp.fragments.FragmentTop;

public class MainActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.default_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.authors) {
            return true;
        } else if (itemId == R.id.exit) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        FragmentTop fragmentTop = new FragmentTop();
        FragmentMiddle fragmentMiddle = new FragmentMiddle();
        FragmentBottom fragmentBottom = new FragmentBottom();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(R.id.layoutTop, fragmentTop);
        ft.add(R.id.layoutMiddle, fragmentMiddle);
        ft.add(R.id.layoutBottom, fragmentBottom);
        ft.commit();
    }
}