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
import com.example.pam_weatherapp.model.WeatherResponse;
import com.example.pam_weatherapp.service.CacheService;
import com.example.pam_weatherapp.service.WeatherService;


public class FragmentMiddle extends Fragment {

    private final WeatherService weatherService;
    private final CacheService cacheService;

    private View view = null;


    public FragmentMiddle() {
        weatherService = WeatherService.getInstance();
        cacheService = CacheService.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_middle, container, false);

        Config config = cacheService.loadConfig();
        update(config);
        return view;
    }

    public void update(Config config){
        updateData(config);
    }

    private void updateData(Config config) {
        WeatherResponse weatherCache = weatherService.getWeather(config);
        if (weatherCache != null)
            setControls(weatherCache);
    }
    public void setControls(WeatherResponse finalWeatherResponse){
        TextView tvWind = view.findViewById(R.id.wind);
        TextView tvHumidity = view.findViewById(R.id.humidity);
        TextView tvVisibility = view.findViewById(R.id.visibility);
        TextView tvSeaLevel = view.findViewById(R.id.sea_level);
        TextView tvGust = view.findViewById(R.id.gust);
        TextView tvGrndLevel = view.findViewById(R.id.grnd_level);
        TextView tvTempMin = view.findViewById(R.id.temp_min);
        TextView tvTempMax = view.findViewById(R.id.temp_max);

        tvWind.post(()-> tvWind.setText("Wind: " + finalWeatherResponse.wind.speed));
        tvHumidity.post(()-> tvHumidity.setText("Humidity: " + finalWeatherResponse.main.humidity));
        tvVisibility.post(()-> tvVisibility.setText("Visibility: " + finalWeatherResponse.visibility));
        tvSeaLevel.post(()-> tvSeaLevel.setText("Sea level: " + finalWeatherResponse.main.sea_level));
        tvGust.post(()-> tvGust.setText("Gust: " + finalWeatherResponse.wind.gust));
        tvGrndLevel.post(()-> tvGrndLevel.setText("Ground level: " + finalWeatherResponse.main.grnd_level));
        tvTempMin.post(()-> tvTempMin.setText("Temp min: " + finalWeatherResponse.main.temp_min));
        tvTempMax.post(()-> tvTempMax.setText("Temp max: " + finalWeatherResponse.main.temp_max));
    }
}