package com.example.pam_weatherapp.model;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Config {

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
}