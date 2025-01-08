package com.example.pam_weatherapp.service;

import com.example.pam_weatherapp.MyApp;
import com.example.pam_weatherapp.model.Config;
import com.example.pam_weatherapp.model.WeatherResponse;
import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class WeatherService {
    private static CacheService cacheService;

    private static WeatherService instance = null;
    private WeatherService() {}

    public static WeatherService getInstance() {
        if (instance == null) {
            synchronized(WeatherService.class) {
                instance = new WeatherService();
                cacheService = CacheService.getInstance();
            }
        }
        return instance;
    }

    public boolean isCityExist(String city){
        String urlString = "https://api.openweathermap.org/data/2.5/weather?appid=" + MyApp.appId +"&q=" + city;
        try {
            URL url = new URL(urlString);
            URLConnection conn = null;
            conn = url.openConnection();

            InputStream is = conn.getInputStream();

            Scanner s = new Scanner(is).useDelimiter("\\A");
            String result = s.hasNext() ? s.next() : "";

            Gson gson = new Gson();
            WeatherResponse weather = gson.fromJson(result, WeatherResponse.class);
            cacheService.saveWeather(weather);
            return weather.cod == 200;

        } catch (FileNotFoundException e) {
            return false;
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public WeatherResponse getWeather() {
        Config config = cacheService.loadConfig();
        String urlString = "https://api.openweathermap.org/data/2.5/weather?appid=" + MyApp.appId +"&units=" + config.currentUnit +"&q=" + config.currentCity;
        try {
            URL url = new URL(urlString);
            URLConnection conn = null;
            conn = url.openConnection();

            try (Socket soc = new Socket()) {
                soc.connect(new InetSocketAddress("www.openweathermap.org", 80), 1000);
            }

            InputStream is = conn.getInputStream();

            Scanner s = new Scanner(is).useDelimiter("\\A");
            String result = s.hasNext() ? s.next() : "";

            Gson gson = new Gson();
            WeatherResponse weather = gson.fromJson(result, WeatherResponse.class);
            cacheService.saveWeather(weather);
            return weather;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
