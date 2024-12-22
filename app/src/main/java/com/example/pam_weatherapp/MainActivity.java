package com.example.pam_weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.MenuCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.pam_weatherapp.fragments.FragmentBottom;
import com.example.pam_weatherapp.fragments.FragmentMiddle;
import com.example.pam_weatherapp.fragments.FragmentTop;
import com.example.pam_weatherapp.model.Config;
import com.example.pam_weatherapp.model.WeatherResponse;
import com.example.pam_weatherapp.service.CacheService;
import com.example.pam_weatherapp.service.WeatherService;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private final CacheService cacheService;
    private final FragmentTop fragmentTop = new FragmentTop();
    private final FragmentMiddle fragmentMiddle = new FragmentMiddle();
    private final FragmentBottom fragmentBottom = new FragmentBottom();

    public MainActivity() {
        cacheService = CacheService.getInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.default_menu, menu);
        MenuCompat.setGroupDividerEnabled(menu, true);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();

        Config config = cacheService.loadConfig();
        menu.add(R.id.units, 0, 0, "Unit: " + config.currentUnit);
        menu.add(R.id.searching, 1, 0, "Search");
        int i = 2;
        for (String city : config.favouriteCities) {
            menu.add(R.id.items, i, 0, city);
            i++;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        int groupId = item.getGroupId();
        if (groupId == R.id.items) {
            Config config = cacheService.loadConfig();
            config.currentCity = Objects.requireNonNull(item.getTitle()).toString();
            fragmentTop.update(config);
            return true;
        } else if ( groupId == R.id.searching){




            return true;
        } else if ( groupId == R.id.units){
            Config config = cacheService.loadConfig();
            config.currentUnit = config.nextUnit();
            cacheService.saveConfig(config);
            item.setTitle("Unit: " + config.currentUnit);
            fragmentTop.update(config);
            return true;
        }

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);



        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(R.id.layoutTop, fragmentTop);
        ft.add(R.id.layoutMiddle, fragmentMiddle);
        ft.add(R.id.layoutBottom, fragmentBottom);
        ft.commit();
    }
}