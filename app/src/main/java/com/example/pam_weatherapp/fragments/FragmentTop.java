package com.example.pam_weatherapp.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pam_weatherapp.R;
import com.example.pam_weatherapp.model.Config;
import com.example.pam_weatherapp.model.WeatherResponse;
import com.example.pam_weatherapp.service.CacheService;
import com.example.pam_weatherapp.service.ForecastService;
import com.example.pam_weatherapp.service.WeatherService;
import com.google.android.material.materialswitch.MaterialSwitch;


public class FragmentTop extends Fragment {

    private final WeatherService weatherService;
    private final ForecastService forecastService;
    private final CacheService cacheService;

    private View view = null;

    public FragmentTop() {
        weatherService = WeatherService.getInstance();
        forecastService = ForecastService.getInstance();
        cacheService = CacheService.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_top, container, false);

        Config config = cacheService.loadConfig();

        update(config);
        return view;
    }

    public void update(Config config){
        updateFav(config);
        updateData(config);
    }

    private void updateData(Config config) {
        WeatherResponse weatherCache = weatherService.getWeather(config);
        if (weatherCache != null) {
            setControls(weatherCache);
        }
    }

    public void setControls(WeatherResponse finalWeatherResponse){
        TextView resultTextView = view.findViewById(R.id.resultTextView);
        ImageView img = view.findViewById(R.id.imageView);

        resultTextView.post(()-> resultTextView.setText("City:" + finalWeatherResponse.name + " temp: " + finalWeatherResponse.main.temp));

        Bitmap bitmap = weatherService.getBitmapWeatherIcon(finalWeatherResponse.weather[0].icon);
        img.setImageBitmap(bitmap);
    }

    private void updateFav(Config config) {
        MaterialSwitch fav = view.findViewById(R.id.fav);
        if(config == null){
            fav.setChecked(false);
        }else {
            fav.setChecked(config.isFav());
        }

        fav.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked)-> {
            cacheService.wrapUpdate(c -> {
                if(isChecked) {
                    c.favouriteCities.add(c.currentCity);
                }
                else {
                    c.favouriteCities.removeIf((x) -> x.equals(c.currentCity));
                }
            });
        });
    }
}