package com.example.pam_weatherapp.model;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Config {
    private static final List<String> units = Arrays.asList("metric", "imperial");

    public String currentCity;
    public String currentUnit;
    public List<String> favouriteCities;

    public static Config getDefault(){
        Config defaultConfig = new Config();
        defaultConfig.currentCity = "Warszawa";
        defaultConfig.currentUnit = "metric";
        defaultConfig.favouriteCities = new ArrayList<>(Arrays.asList("Warszawa", "Łódź", "Piotrków Trybunalski"));

        return defaultConfig;
    }

    public String nextUnit(){
        int index = units.indexOf(currentUnit);
        if(index + 1 >= units.size()){
            index = 0;
        } else {
            index++;
        }
        return units.get(index);
    }

    public boolean isFav(){
        return favouriteCities.contains(currentCity);
    }
}