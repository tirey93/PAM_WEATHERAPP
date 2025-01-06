package com.example.pam_weatherapp.service;

import com.example.pam_weatherapp.MyApp;
import com.example.pam_weatherapp.model.Config;
import com.example.pam_weatherapp.model.WeatherResponse;
import com.google.gson.Gson;

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

    public WeatherResponse getWeather() {
        Config config = cacheService.loadConfig();
        String urlString = "https://api.openweathermap.org/data/2.5/weather?appid=" + MyApp.appId +"&units=" + config.currentUnit +"&q=" + config.currentCity;
        try {
            URL url = new URL(urlString);
            URLConnection conn = null;
            conn = url.openConnection();

            try (Socket soc = new Socket()) {
                soc.connect(new InetSocketAddress("www.openweathermap.org", 80), 100);
            }

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
