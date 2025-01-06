package com.example.pam_weatherapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pam_weatherapp.R;
import com.example.pam_weatherapp.model.WeatherResponse;
import com.example.pam_weatherapp.service.CacheService;


public class FragmentMiddle extends Fragment {

    private final CacheService cacheService;

    private View view = null;


    public FragmentMiddle() {
        cacheService = CacheService.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_middle, container, false);

        return view;
    }

    public void update(){
        updateData();
    }

    private void updateData() {
        WeatherResponse weatherCache = cacheService.loadWeather();
        if (weatherCache != null)
            setControls(weatherCache);
    }
    public void setControls(WeatherResponse finalWeatherResponse){
        TextView resultTextView = view.findViewById(R.id.textView);
        resultTextView.post(()-> resultTextView.setText("Wind: " + finalWeatherResponse.wind.speed));
    }
}