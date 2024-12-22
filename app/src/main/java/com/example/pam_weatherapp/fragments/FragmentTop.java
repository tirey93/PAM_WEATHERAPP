package com.example.pam_weatherapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
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

    public void update(){update(null);}
    public void update(Config config) {
        TextView resultTextView = view.findViewById(R.id.resultTextView);
        try {
            CompletableFuture.supplyAsync(() -> weatherService.getWeather(config), Executors.newSingleThreadExecutor())
                .whenComplete((weatherResponse, throwable) -> {
                    if (throwable != null) {
                        resultTextView.setText("Error fetching data: " + throwable.getMessage());
                    } else if (weatherResponse != null) {
                        resultTextView.post(()-> resultTextView.setText("City:" + weatherResponse.name + " temp: " + weatherResponse.main.temp));
                    }
                });

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}