package com.example.pam_weatherapp;

import android.app.Application;
import android.content.Context;

public class MyApp extends Application {
    private static MyApp instance;
    public static String appId = "ad6d56e3ad097ef279175e9fadb7c7df";

    public static MyApp getInstance() {
        return instance;
    }

    public static Context getContext(){
        return instance.getApplicationContext();
    }

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
    }
}
