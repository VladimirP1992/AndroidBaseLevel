package ru.geekbrains.androidBase.lesson1.model;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

import retrofit2.http.Query;
import ru.geekbrains.androidBase.lesson1.AppSettingsSingleton;
import ru.geekbrains.androidBase.lesson1.db.DataSource;

public class WeatherProvider {
    private Set<WeatherProviderListener> listenerSet;
    private Handler handler = new Handler();
    private static WeatherProvider instance = null;
    private Timer timer;

    private Retrofit retrofit;
    private OpenWeather weatherApi;

    private AppSettingsSingleton settingsSingleton;
    private DataSource dataSource;
    private Context context;

    private WeatherProvider(Context context) {
        listenerSet = new HashSet<>();
        retrofit = new Retrofit.Builder().baseUrl("https://api.openweathermap.org").
                addConverterFactory(GsonConverterFactory.create()).build();
        weatherApi = retrofit.create(OpenWeather.class);
        settingsSingleton = AppSettingsSingleton.getInstance();
        this.context = context;

        startRequests();
    }

    public static Double kelvinToCelsius(double kelvinTemperature) {
        return (kelvinTemperature - 273.15);
    }

    public static Double hectopascalTOMmOfMercury(double hectopascalPressure) {
        return (hectopascalPressure * 0.750063755419211);
    }

    public static WeatherProvider getInstance(Context context) {
        return instance = (instance == null) ? new WeatherProvider(context) : instance;
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

    interface OpenWeather {
        @GET("data/2.5/weather")
        Call<WeatherModel> getWeather(@Query("q") String q, @Query("appid") String appId);
        @GET("data/2.5/weather")
        Call<WeatherModel> getWeather(@Query("lat") double latitude, @Query("lon") double longitude, @Query("appid") String appId);
    }

    //Get WeatherModel by city name
    private WeatherModel getWeather(String cityName) throws Exception {

        Call<WeatherModel> call = weatherApi.getWeather(cityName, "33512f8887706ed78a064d2a5823381c");

        Response<WeatherModel> response = call.execute();

        if (response.isSuccessful()) {
            dataSource = AppSettingsSingleton.getInstance().getDataSource();
            return response.body();
        }
        throw new Exception(response.errorBody().string(), null);
    }

    //Get WeatherModel by coordinates
    private WeatherModel getWeather(double latitude, double longitude) throws Exception {

        Call<WeatherModel> call = weatherApi.getWeather(latitude, longitude, "33512f8887706ed78a064d2a5823381c");

        Response<WeatherModel> response = call.execute();

        if (response.isSuccessful()) {
            return response.body();
        }
        throw new Exception(response.errorBody().string(), null);
    }
    //Get city name by coordinates
    public String getCityName(double latitude, double longitude){
        String cityName = "";

        try {
            final WeatherModel model = getWeather(latitude, longitude);
            if (model != null) {
                cityName = String.format("%s,%s", model.getName(), model.getSys().getCountry());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cityName;
    }

    private void startRequests() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    String cityName = settingsSingleton.getCityFieldText();
                    final WeatherModel model = getWeather(cityName);

                    if (model == null) {
                        return;
                    }
                    //get correct city name from model
                    cityName = String.format("%s,%s", model.getName(), model.getSys().getCountry());
                    settingsSingleton.setCityFieldText(cityName);
                    updateDatabase(model, cityName);

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            for (WeatherProviderListener listener : listenerSet) {
                                listener.updateWeather(model);
                            }
                        }
                    });
                } catch (Exception e) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "Can't get weather for entered city!", Toast.LENGTH_LONG).show();
                        }
                    });

                    e.printStackTrace();
                }
            }
        }, 500, 8000);
    }

    private void updateDatabase(WeatherModel model, String cityName) {
        if (dataSource != null) {
            double temperature = kelvinToCelsius(model.getMain().getTemp());
            double wind = model.getWind().getSpeed();
            double pressure = hectopascalTOMmOfMercury(model.getMain().getPressure());
            dataSource.updateHistory(cityName, temperature, wind, pressure);
        }
    }

    void stop() {
        if (timer != null) {
            timer.cancel();
        }
        listenerSet.clear();
    }
}
