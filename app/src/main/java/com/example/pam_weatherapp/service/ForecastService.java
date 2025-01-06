package com.example.pam_weatherapp.service;

import com.example.pam_weatherapp.MyApp;
import com.example.pam_weatherapp.model.Config;
import com.example.pam_weatherapp.model.ForecastResponse;
import com.example.pam_weatherapp.model.WeatherResponse;
import com.google.gson.Gson;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class ForecastService {
    private static CacheService cacheService;

    private static ForecastService instance = null;
    private ForecastService() {}

    public static ForecastService getInstance() {
        if (instance == null) {
            synchronized(ForecastService.class) {
                instance = new ForecastService();
                cacheService = CacheService.getInstance();
            }
        }
        return instance;
    }

    public ForecastResponse getForecast() {
        Config config = cacheService.loadConfig();
        String urlString = "https://api.openweathermap.org/data/2.5/forecast?appid=" + MyApp.appId +"&units=" + config.currentUnit +"&q=" + config.currentCity;;
        try {
            URL url = new URL(urlString);
            URLConnection conn = null;
            conn = url.openConnection();
            InputStream is = conn.getInputStream();

            Scanner s = new Scanner(is).useDelimiter("\\A");
            String result = s.hasNext() ? s.next() : "";

            Gson gson = new Gson();
            return gson.fromJson(result, ForecastResponse.class);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
