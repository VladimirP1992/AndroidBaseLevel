package ru.geekbrains.androidBase.lesson1.model;

import android.os.Build;
import android.os.Handler;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

public class WeatherProvider {
    private Set<WeatherProviderListener> listenerSet;
    Handler handler = new Handler();
    private static WeatherProvider instance = null;
    private Timer timer;

    private WeatherProvider(){
        listenerSet = new HashSet<>();
        startRequests();
    }

    public static int kelvinToCelsius(double kelvinTemperature){
        return (int)(kelvinTemperature - 273.15);
    }

    public static WeatherProvider getInstance(){
        return instance = (instance == null) ? new WeatherProvider() : instance;
    }

    public void addListener(WeatherProviderListener listener){
        if(!listenerSet.contains(listener)){
            listenerSet.add(listener);
        }
    }

    public void removeListener(WeatherProviderListener listener){
        if(listenerSet.contains(listener)){
            listenerSet.remove(listener);
        }
    }

    private WeatherModel getWeather(String cityName){
        WeatherModel model = null;

        HttpURLConnection urlConnection = null;
        //Url sample https://api.openweathermap.org/data/2.5/weather?q=Moscow,RU&appid=33512f8887706ed78a064d2a5823381c
        String urlString = String.format("https://api.openweathermap.org/data/2.5/weather?q=%s,RU&appid=33512f8887706ed78a064d2a5823381c", cityName);

        try {
            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(10000);

            if(urlConnection.getResponseCode() >= 200 && urlConnection.getResponseCode() < 299) {
                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    final String result = in.lines().collect(Collectors.joining("\n"));
                    Gson gson = new Gson();
                    model = gson.fromJson(result, WeatherModel.class);
                }
            } else {
                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream()));
                //process the error somehow
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return model;
    }

    private void startRequests(){
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                final WeatherModel model = getWeather("Moscow"); //BAD - hardcoded
                if (model == null) return;

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        for (WeatherProviderListener listener: listenerSet){
                            listener.updateWeather(model);
                        }
                    }
                });
            }
        },1000, 10000);
    }

    void stop(){
        if(timer != null){
            timer.cancel();
        }
        listenerSet.clear();
    }
}
