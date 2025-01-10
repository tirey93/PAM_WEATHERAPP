package com.example.pam_weatherapp.service;

import com.example.pam_weatherapp.MyApp;
import com.example.pam_weatherapp.model.Config;
import com.example.pam_weatherapp.model.ForecastResponse;
import com.example.pam_weatherapp.model.WeatherResponse;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.function.Consumer;

public class CacheService {

    private static final String configName = "config.json";
    private static final String weatherName = "weather.json";
    private static final String forecastName = "forecast.json";

    private static CacheService instance = null;;
    private static final Gson gson = new Gson();

    private CacheService() {}

    public static CacheService getInstance() {
        if (instance == null) {
            synchronized(CacheService.class) {
                instance = new CacheService();
            }
        }
        return instance;
    }

    public void wrapUpdate(Consumer<Config> consumer) {
        Config config = loadConfig();
        consumer.accept(config);

        saveConfig(config);
    }

    public Config loadConfig(){
        File file = new File(MyApp.getContext().getCacheDir(), configName);
        if(!file.exists()){
            saveConfig(Config.getDefault());
        }

        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            while (line != null) {
                stringBuilder.append(line).append('\n');
                line = reader.readLine();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        String out = stringBuilder.toString();
        return gson.fromJson(out, Config.class);
    }

    public void saveWeather(WeatherResponse weatherResponse){
        File cacheFile = new File(MyApp.getContext().getCacheDir(), weatherName);

        String jsonData = gson.toJson(weatherResponse);

        try (FileWriter writer = new FileWriter(cacheFile)) {
            writer.write(jsonData);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public WeatherResponse loadWeather(){
        File file = new File(MyApp.getContext().getCacheDir(), weatherName);
        if(!file.exists()){
            return null;
        }

        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            while (line != null) {
                stringBuilder.append(line).append('\n');
                line = reader.readLine();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        String out = stringBuilder.toString();
        return gson.fromJson(out, WeatherResponse.class);
    }

    public void saveForecast(ForecastResponse forecastResponse){
        File cacheFile = new File(MyApp.getContext().getCacheDir(), forecastName);

        String jsonData = gson.toJson(forecastResponse);

        try (FileWriter writer = new FileWriter(cacheFile)) {
            writer.write(jsonData);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ForecastResponse loadForecast(){
        File file = new File(MyApp.getContext().getCacheDir(), forecastName);
        if(!file.exists()){
            return null;
        }

        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            while (line != null) {
                stringBuilder.append(line).append('\n');
                line = reader.readLine();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        String out = stringBuilder.toString();
        return gson.fromJson(out, ForecastResponse.class);
    }

    private void saveConfig(Config config){
        File cacheFile = new File(MyApp.getContext().getCacheDir(), configName);

        String jsonData = gson.toJson(config);

        try (FileWriter writer = new FileWriter(cacheFile)) {
            writer.write(jsonData);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
