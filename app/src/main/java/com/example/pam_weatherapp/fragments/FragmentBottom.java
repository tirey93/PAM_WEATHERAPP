package com.example.pam_weatherapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;


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
            setControls(forecastCache, config);
    }

    public void setControls(ForecastResponse forecastResponse, Config config){
        String unit =  config.currentUnit.equals("metric") ? "°C" : "°F";
        TextView tvGeneralTemp1 = view.findViewById(R.id.generalTemp1);
        TextView tvDate1 = view.findViewById(R.id.date1);
        TextView tvPressure1 = view.findViewById(R.id.pressure1);
        ImageView ivIcon1 = view.findViewById(R.id.weatherIcon1);

        TextView tvGeneralTemp2 = view.findViewById(R.id.generalTemp2);
        TextView tvDate2 = view.findViewById(R.id.date2);
        TextView tvPressure2 = view.findViewById(R.id.pressure2);
        ImageView ivIcon2 = view.findViewById(R.id.weatherIcon2);

        TextView tvGeneralTemp3 = view.findViewById(R.id.generalTemp3);
        TextView tvDate3 = view.findViewById(R.id.date3);
        TextView tvPressure3 = view.findViewById(R.id.pressure3);
        ImageView ivIcon3 = view.findViewById(R.id.weatherIcon3);

        tvGeneralTemp1.post(()-> tvGeneralTemp1.setText("Temp: " + forecastResponse.list[8].main.temp + unit));
        tvDate1.post(()-> tvDate1.setText(dtToStr(forecastResponse.list[8].dt)));
        tvPressure1.post(()-> tvPressure1.setText("Pressure: " + forecastResponse.list[8].main.pressure + "hPa"));
        ivIcon1.post(()-> ivIcon1.setImageBitmap(forecastService.getBitmapForecastIcon(forecastResponse.list[8].weather[0].icon)));


        tvGeneralTemp2.post(()-> tvGeneralTemp2.setText("Temp: " + forecastResponse.list[16].main.temp + unit));
        tvDate2.post(()-> tvDate2.setText(dtToStr(forecastResponse.list[16].dt)));
        tvPressure2.post(()-> tvPressure2.setText("Pressure: " + forecastResponse.list[16].main.pressure + "hPa"));
        ivIcon2.post(()-> ivIcon2.setImageBitmap(forecastService.getBitmapForecastIcon(forecastResponse.list[16].weather[0].icon)));


        tvGeneralTemp3.post(()-> tvGeneralTemp3.setText("Temp: " + forecastResponse.list[24].main.temp + unit));
        tvDate3.post(()-> tvDate3.setText(dtToStr(forecastResponse.list[24].dt)));
        tvPressure3.post(()-> tvPressure3.setText("Pressure: " + forecastResponse.list[24].main.pressure + "hPa"));
        ivIcon3.post(()-> ivIcon3.setImageBitmap(forecastService.getBitmapForecastIcon(forecastResponse.list[24].weather[0].icon)));
    }

    private String dtToStr(long dt){
        Date ld = new Date(dt * 1000);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(ld);
    }
}