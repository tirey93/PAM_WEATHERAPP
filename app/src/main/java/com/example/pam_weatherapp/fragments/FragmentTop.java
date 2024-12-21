package com.example.pam_weatherapp.fragments;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pam_weatherapp.R;
import com.example.pam_weatherapp.model.ForecastResponse;
import com.example.pam_weatherapp.model.Person;
import com.example.pam_weatherapp.model.WeatherResponse;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;


public class FragmentTop extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top, container, false);

        Button button= view.findViewById(R.id.button2);
        if(button != null){
            button.setOnClickListener(v ->{
                String urlString = "https://api.openweathermap.org/data/2.5/forecast?id=524901&appid=ad6d56e3ad097ef279175e9fadb7c7df";
                try {
                    URL url = new URL(urlString);
                    URLConnection conn = null;
                    conn = url.openConnection();
                    InputStream is = conn.getInputStream();

                    Scanner s = new Scanner(is).useDelimiter("\\A");
                    String result = s.hasNext() ? s.next() : "";

                    Gson gson = new Gson();
                    ForecastResponse weatherResponse = gson.fromJson(result, ForecastResponse.class);



                    int a = 5;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }

        return view;
    }
}