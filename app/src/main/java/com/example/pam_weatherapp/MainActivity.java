package com.example.pam_weatherapp;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.pam_weatherapp.fragments.FragmentBottom;
import com.example.pam_weatherapp.fragments.FragmentMiddle;
import com.example.pam_weatherapp.fragments.FragmentTop;
import com.example.pam_weatherapp.model.Config;
import com.example.pam_weatherapp.service.CacheService;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private final CacheService cacheService;
    private final FragmentTop fragmentTop = new FragmentTop();
    private final FragmentMiddle fragmentMiddle = new FragmentMiddle();
    private final FragmentBottom fragmentBottom = new FragmentBottom();

    public MainActivity() {
        cacheService = CacheService.getInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.default_menu, menu);
        MenuCompat.setGroupDividerEnabled(menu, true);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();

        Config config = cacheService.loadConfig();
        menu.add(R.id.units, 0, 0, "Unit: " + config.currentUnit);
        menu.add(R.id.searching, 1, 0, "Search");
        int i = 2;
        for (String city : config.favouriteCities) {
            menu.add(R.id.items, i, 0, city);
            i++;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        int groupId = item.getGroupId();
        if (groupId == R.id.items) {
            cacheService.wrapUpdate(config -> {
                config.currentCity = Objects.requireNonNull(item.getTitle()).toString();
            });
            updateFragments();
            return true;
        } else if ( groupId == R.id.searching){
            onButtonShowPopupWindowClick(new LinearLayout(this));
            return true;
        } else if ( groupId == R.id.units){
            cacheService.wrapUpdate(config -> {
                config.currentUnit = config.nextUnit();
                item.setTitle("Unit: " + config.currentUnit);
            });
            updateFragments();
            return true;
        }

        return true;
    }

    private void updateFragments() {
        fragmentTop.update();
    }

    public void onButtonShowPopupWindowClick(View view) {
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window, null);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        EditText newCity = popupView.findViewById(R.id.editCity);
        if(newCity != null){
            newCity.setOnEditorActionListener((TextView v, int actionId, KeyEvent event) ->{
                if (actionId == EditorInfo.IME_ACTION_DONE || event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER){
                    cacheService.wrapUpdate(config -> {
                        config.currentCity = Objects.requireNonNull(newCity.getText()).toString();
                    });
                    updateFragments();
                    popupWindow.dismiss();
                    return true;
                }
                return  false;
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);



        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(R.id.layoutTop, fragmentTop);
        ft.add(R.id.layoutMiddle, fragmentMiddle);
        ft.add(R.id.layoutBottom, fragmentBottom);
        ft.commit();
    }
}