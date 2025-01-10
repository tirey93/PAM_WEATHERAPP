package com.example.pam_weatherapp;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.pam_weatherapp.fragments.FragmentBottom;
import com.example.pam_weatherapp.fragments.FragmentMiddle;
import com.example.pam_weatherapp.fragments.FragmentTop;
import com.example.pam_weatherapp.model.Config;
import com.example.pam_weatherapp.model.ForecastResponse;
import com.example.pam_weatherapp.model.WeatherResponse;
import com.example.pam_weatherapp.service.CacheService;
import com.example.pam_weatherapp.service.ForecastService;
import com.example.pam_weatherapp.service.WeatherService;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private final CacheService cacheService;
    private final FragmentTop fragmentTop = new FragmentTop();
    private final FragmentMiddle fragmentMiddle = new FragmentMiddle();
    private final FragmentBottom fragmentBottom = new FragmentBottom();
    private final WeatherService weatherService;
    private final ForecastService forecastService;

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();

        Config config = cacheService.loadConfig();
        menu.add(R.id.refresh, 0, 0, "Refresh");
        menu.add(R.id.units, 1, 0, "Unit: " + config.currentUnit);
        menu.add(R.id.searching, 2, 0, "Search");
        int i = 3;
        for (String city : config.favouriteCities) {
            menu.add(R.id.items, i, 0, city);
            i++;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int groupId = item.getGroupId();
        if ( groupId == R.id.refresh) {
            loadWeather();
            return true;
        } else if (groupId == R.id.items) {
            cacheService.wrapUpdate(config -> {
                config.currentCity = Objects.requireNonNull(item.getTitle()).toString();
                updateFragments(config);
            });
            return true;
        } else if ( groupId == R.id.searching){
            onButtonShowPopupWindowClick(new LinearLayout(this));
            return true;
        } else if ( groupId == R.id.units){
            cacheService.wrapUpdate(config -> {
                config.currentUnit = config.nextUnit();
                item.setTitle("Unit: " + config.currentUnit);
                updateFragments(config);
            });
            return true;
        }

        return true;
    }

    public void onButtonShowPopupWindowClick(View view) {
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window, null);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        EditText newCity = popupView.findViewById(R.id.editCity);
        if(newCity != null){
            newCity.setOnEditorActionListener((TextView v, int actionId, KeyEvent event) ->{
                if (actionId == EditorInfo.IME_ACTION_DONE || event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER){
                    if(weatherService.isCityExist(String.valueOf(newCity.getText()))){
                        cacheService.wrapUpdate(config -> {
                            config.currentCity = String.valueOf(newCity.getText());
                            WeatherResponse weatherResponse = weatherService.getWeather(config);
                            config.currentCity = weatherResponse.name;
                            updateFragments(config);
                        });
                    } else {
                        showToast("City was not found", Toast.LENGTH_LONG);
                    }
                    popupWindow.dismiss();
                    return true;
                }
                return  false;
            });
        }
    }


    /*---------------------------------------*/
    /*------------ Main Activity -----------*/
    /*---------------------------------------*/
    public MainActivity() {
        cacheService = CacheService.getInstance();
        weatherService = WeatherService.getInstance();
        forecastService = ForecastService.getInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.default_menu, menu);
        MenuCompat.setGroupDividerEnabled(menu, true);
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

        loadWeather();
        loadForecast();
    }

    private void loadWeather() {
        Config config = cacheService.loadConfig();
        CompletableFuture.supplyAsync(() -> weatherService.getWeather(config), Executors.newSingleThreadExecutor())
            .whenComplete((weatherResponse, throwable) -> {
                WeatherResponse weatherCache = cacheService.loadWeather();
                if (throwable != null && weatherCache == null) {
                    showToast("Data not available", Toast.LENGTH_LONG);
                } else if (weatherResponse != null) {
//                    showToast("Data loaded from web", Toast.LENGTH_SHORT);
                    updateFragments(config);
                } else {
                    showToast("Data loaded from cache", Toast.LENGTH_LONG);
                }
            });
    }
    private void loadForecast() {
        Config config = cacheService.loadConfig();
        CompletableFuture.supplyAsync(() -> forecastService.getForecast(config), Executors.newSingleThreadExecutor())
            .whenComplete((forecastResponse, throwable) -> {
                ForecastResponse forecastCache = cacheService.loadForecast();
                if (throwable != null && forecastCache == null) {
//                    showToast("Data not available", Toast.LENGTH_LONG);
                } else if (forecastResponse != null) {
//                    showToast("Data loaded from web", Toast.LENGTH_SHORT);
                    fragmentBottom.update(config);
                } else {
//                    showToast("Data loaded from cache", Toast.LENGTH_LONG);
                }
            });
    }

    private void showToast(String text, int length) {
        runOnUiThread(() ->{
            final Toast toast = Toast.makeText(getApplicationContext(), text, length);
            toast.show();
        });
    }
    private void updateFragments(Config config) {
        fragmentTop.update(config);
        fragmentMiddle.update(config);
    }
}