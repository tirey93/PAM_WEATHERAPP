package com.example.pam_weatherapp.model;

public class ForecastResponse {

    public String cod;
    public int message;
    public int cnt;
    public ForecastItem[] list;


    public static class ForecastItem {
        public long dt;
        public Main main;
        public Weather[] weather;
        public Clouds clouds;
        public Wind wind;
        public int visibility;
        public double pop;
        public Sys sys;
        public String dt_txt;
    }

    public static class Main {
        public double temp;
        public double feels_like;
        public double temp_min;
        public double temp_max;
        public int pressure;
        public int sea_level;
        public int grnd_level;
        public int humidity;
        public double temp_kf;
    }

    public static class Weather {
        public int id;
        public String main;
        public String description;
        public String icon;
    }

    public static class Clouds {
        public int all;
    }

    public static class Wind {
        public double speed;
        public int deg;
        public double gust;
    }

    public static class Sys {
        public String pod;
    }
}
