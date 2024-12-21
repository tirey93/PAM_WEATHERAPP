package com.example.pam_weatherapp.service;

import com.example.pam_weatherapp.model.ForecastResponse;
import com.example.pam_weatherapp.model.WeatherResponse;
import com.google.gson.Gson;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class WeatherService {
    private static WeatherService instance = null;

    private WeatherService() {}

    public static WeatherService getInstance() {
        if (instance == null) {
            synchronized(WeatherService.class) {
                instance = new WeatherService();
            }
        }
        return instance;
    }

    public WeatherResponse getWeather() {
        String urlString = "https://api.openweathermap.org/data/2.5/weather?lat=44.34&lon=10.99&units=metric&appid=ad6d56e3ad097ef279175e9fadb7c7df";
        try {
            URL url = new URL(urlString);
            URLConnection conn = null;
            conn = url.openConnection();
            InputStream is = conn.getInputStream();

            Scanner s = new Scanner(is).useDelimiter("\\A");
            String result = s.hasNext() ? s.next() : "";

            Gson gson = new Gson();
            return gson.fromJson(result, WeatherResponse.class);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
