package com.example.pam_weatherapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pam_weatherapp.MyApp;
import com.example.pam_weatherapp.R;
import com.example.pam_weatherapp.model.Config;
import com.example.pam_weatherapp.model.ForecastResponse;
import com.example.pam_weatherapp.model.WeatherResponse;
import com.example.pam_weatherapp.service.CacheService;
import com.example.pam_weatherapp.service.ForecastService;
import com.example.pam_weatherapp.service.WeatherService;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;


public class FragmentTop extends Fragment {

    private final WeatherService weatherService;
    private final ForecastService forecastService;
    private final CacheService cacheService;

    private View view = null;

    public FragmentTop() {
        weatherService = WeatherService.getInstance();
        forecastService = ForecastService.getInstance();
        cacheService = CacheService.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_top, container, false);

        update();
        return view;
    }

    public void update(){
        updateFav();
        updateData();
    }

    private void updateData() {
        try {
            WeatherResponse weatherCache = cacheService.loadWeather();
            if (weatherCache != null)
                setControls(weatherCache);

            CompletableFuture.supplyAsync(weatherService::getWeather, Executors.newSingleThreadExecutor())
                .whenComplete((weatherResponse, throwable) -> {
                    if (throwable != null && weatherCache == null) {
                        showToast("Data not available", Toast.LENGTH_LONG);
                    } else if (weatherResponse != null) {
                        setControls(weatherResponse);
                        showToast("Data load from web", Toast.LENGTH_SHORT);
                    } else {
                        showToast("Data loaded from cache", Toast.LENGTH_LONG);
                    }
                });

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setControls(WeatherResponse finalWeatherResponse){
        TextView resultTextView = view.findViewById(R.id.resultTextView);
        resultTextView.post(()-> resultTextView.setText("City:" + finalWeatherResponse.name + " temp: " + finalWeatherResponse.main.temp));

    }

    private void showToast(String text, int length) {
        getActivity().runOnUiThread(() ->{
            final Toast toast = Toast.makeText(getActivity(), text, length);
            toast.show();
        });

    }

    private void updateFav() {
        Config config = cacheService.loadConfig();
        MaterialSwitch fav = view.findViewById(R.id.fav);
        if(config == null){
            fav.setChecked(false);
        }else {
            fav.setChecked(config.isFav());
        }

        fav.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked)-> {
            cacheService.wrapUpdate(c -> {
                if(isChecked) {
                    c.favouriteCities.add(c.currentCity);
                }
                else {
                    c.favouriteCities.removeIf((x) -> x.equals(c.currentCity));
                }
            });
        });
    }
}