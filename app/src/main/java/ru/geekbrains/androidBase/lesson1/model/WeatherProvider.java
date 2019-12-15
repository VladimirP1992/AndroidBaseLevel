package ru.geekbrains.androidBase.lesson1.model;

import android.os.Build;
import android.os.Handler;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class WeatherProvider {
    private Set<WeatherProviderListener> listenerSet;
    Handler handler = new Handler();
    private static WeatherProvider instance = null;
    private Timer timer;
    private static String cityName = "Moscow"; //Default value;
    private static Object synchObject = new Object();

    private Retrofit retrofit;
    private OpenWeather weatherApi;

    public static void setCity(String cityName) {
        synchronized (synchObject) {
            WeatherProvider.cityName = cityName;
        }
    }

    private WeatherProvider() {
        listenerSet = new HashSet<>();
        retrofit = new Retrofit.Builder().baseUrl("https://api.openweathermap.org").
                addConverterFactory(GsonConverterFactory.create()).build();
        weatherApi = retrofit.create(OpenWeather.class);

        startRequests();
    }

    public static int kelvinToCelsius(double kelvinTemperature) {
        return (int) (kelvinTemperature - 273.15);
    }

    public static WeatherProvider getInstance() {
        return instance = (instance == null) ? new WeatherProvider() : instance;
    }

    public void addListener(WeatherProviderListener listener) {
        if (!listenerSet.contains(listener)) {
            listenerSet.add(listener);
        }
    }

    public void removeListener(WeatherProviderListener listener) {
        if (listenerSet.contains(listener)) {
            listenerSet.remove(listener);
        }
    }
    interface OpenWeather{
        @GET("data/2.5/weather")
        Call<WeatherModel> getWeather(@Query("q") String q, @Query("appid") String appId);
    }

    private WeatherModel getWeather(String cityName) throws Exception {

        Call<WeatherModel> call = weatherApi.getWeather(cityName + ",RU","33512f8887706ed78a064d2a5823381c");

        Response<WeatherModel> response = call.execute();

        if(response.isSuccessful())
            return response.body();
        else
            throw new Exception(response.errorBody().string(), null);
    }

    private void startRequests() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                synchronized (synchObject) {
                    try {
                        final WeatherModel model = getWeather(WeatherProvider.cityName);    //BAD - hardcoded

                        if (model == null) return;

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                for (WeatherProviderListener listener : listenerSet) {
                                    listener.updateWeather(model);
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, 1000, 10000);
    }

    void stop() {
        if (timer != null) {
            timer.cancel();
        }
        listenerSet.clear();
    }
}
