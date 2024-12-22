package com.example.pam_weatherapp.service;

import com.example.pam_weatherapp.MyApp;
import com.example.pam_weatherapp.model.Config;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class CacheService {

    private static final String name = "config.json";

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

    public void saveConfig(Config config){
        File cacheFile = new File(MyApp.getContext().getCacheDir(), name);

        String jsonData = gson.toJson(config);

        try (FileWriter writer = new FileWriter(cacheFile)) {
            writer.write(jsonData);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Config loadConfig(){
        File file = new File(MyApp.getContext().getCacheDir(), name);
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
}
