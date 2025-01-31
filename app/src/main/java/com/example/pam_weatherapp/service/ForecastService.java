package com.example.pam_weatherapp.service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.pam_weatherapp.MyApp;
import com.example.pam_weatherapp.model.Config;
import com.example.pam_weatherapp.model.ForecastResponse;
import com.example.pam_weatherapp.model.WeatherResponse;
import com.google.gson.Gson;

import java.io.IOException;
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

    public Bitmap getBitmapForecastIcon(String icon) {
        try {
            URL url = new URL("https://openweathermap.org/img/wn/" + icon +"@4x.png");
            URLConnection connection = url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public ForecastResponse getForecast(Config config) {
        String urlString = "https://api.openweathermap.org/data/2.5/forecast?appid=" + MyApp.appId +"&units=" + config.currentUnit +"&q=" + config.currentCity;;
        try {
            URL url = new URL(urlString);
            URLConnection conn = null;
            conn = url.openConnection();
            InputStream is = conn.getInputStream();

            Scanner s = new Scanner(is).useDelimiter("\\A");
            String result = s.hasNext() ? s.next() : "";

            Gson gson = new Gson();
            ForecastResponse response = gson.fromJson(result, ForecastResponse.class);
            cacheService.saveForecast(response);
            return response;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
