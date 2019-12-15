package ru.geekbrains.androidBase.lesson1;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

public class WeatherUpdateService extends IntentService {

    private static final String TAG = "WEATHER_SERVICE";

    public WeatherUpdateService() {
        super("weatherUpdate");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "Weather Update Service was started");

    }
}
