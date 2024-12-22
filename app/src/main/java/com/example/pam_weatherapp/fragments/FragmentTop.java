package com.example.pam_weatherapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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


public class FragmentTop extends Fragment {

    private final WeatherService weatherService;
    private final ForecastService forecastService;
    private final CacheService cacheService;

    public FragmentTop() {
        weatherService = WeatherService.getInstance();
        forecastService = ForecastService.getInstance();
        cacheService = CacheService.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top, container, false);

        Button button= view.findViewById(R.id.button2);
        if(button != null){
            button.setOnClickListener(v ->{
                try {
                    WeatherResponse w = weatherService.getWeather();
                    ForecastResponse f = forecastService.getForecast();

                    int a = 5;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }

        Button button3= view.findViewById(R.id.button3);
        if(button3 != null){
            button3.setOnClickListener(v ->{
                try {


                    Config c4 = cacheService.loadConfig();

                    int a = 5;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }

        return view;
    }
}