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
import com.example.pam_weatherapp.model.Config;
import com.example.pam_weatherapp.model.ForecastResponse;
import com.example.pam_weatherapp.model.WeatherResponse;
import com.example.pam_weatherapp.service.CacheService;
import com.example.pam_weatherapp.service.ForecastService;
import com.example.pam_weatherapp.service.WeatherService;


public class FragmentBottom extends Fragment {
    private final ForecastService forecastService;
    private final CacheService cacheService;

    private View view = null;

    public FragmentBottom() {
        forecastService = ForecastService.getInstance();
        cacheService = CacheService.getInstance();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_bottom, container, false);

        Config config = cacheService.loadConfig();

        update(config);
        return view;
    }

    public void update(Config config){
        updateData(config);
    }

    private void updateData(Config config) {
        ForecastResponse forecastCache = null;
        try {
            forecastCache = forecastService.getForecast(config);
        } catch (Exception e) {
            forecastCache = cacheService.loadForecast();
        }
        if (forecastCache != null)
            setControls(forecastCache);
    }

    public void setControls(ForecastResponse forecastResponse){
        Config config = cacheService.loadConfig();
        String unit =  config.currentUnit.equals("metric") ? "°C" : "°F";
        TextView tvGeneralTemp1 = view.findViewById(R.id.generalTemp1);
        TextView tvGeneralTemp2 = view.findViewById(R.id.generalTemp2);
        TextView tvGeneralTemp3 = view.findViewById(R.id.generalTemp3);

        tvGeneralTemp1.post(()-> tvGeneralTemp1.setText("Temp: " + forecastResponse.list[8].main.temp + unit));
        tvGeneralTemp2.post(()-> tvGeneralTemp2.setText("Temp: " + forecastResponse.list[16].main.temp + unit));
        tvGeneralTemp3.post(()-> tvGeneralTemp3.setText("Temp: " + forecastResponse.list[24].main.temp + unit));
    }
}