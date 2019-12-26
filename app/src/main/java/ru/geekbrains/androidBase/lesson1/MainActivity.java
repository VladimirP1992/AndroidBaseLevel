package ru.geekbrains.androidBase.lesson1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ru.geekbrains.androidBase.lesson1.model.WeatherModel;
import ru.geekbrains.androidBase.lesson1.model.WeatherProvider;
import ru.geekbrains.androidBase.lesson1.model.WeatherProviderListener;

public class MainActivity extends AppCompatActivity implements ConstantNames, WeatherProviderListener {

    private static final String TAG = "Lesson3";

    Button citySelectButton;
    RecyclerView weekWeatherList;
    TextView cityNameTextView;
    TextView temperatureTextView;

    AppSettingsSingleton appSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        initViews();
        appSettings = AppSettingsSingleton.getInstance();

        citySelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCitySelectActivity();
            }
        });

        createWeekWeatherList();
        loadPreferences();

        Log.d(TAG,"onCreate()");

    }

    private void initViews(){
        citySelectButton = findViewById(R.id.citySelectButton);
        weekWeatherList = findViewById(R.id.weekWeatherList);
        cityNameTextView = findViewById(R.id.cityNameTextView);
        temperatureTextView = findViewById(R.id.temperatureTextView);
    }

    private void loadPreferences() {
        SharedPreferences appPreferences = getSharedPreferences(APP_SHARED_PREFERENCES_NAME, MODE_PRIVATE);

        appSettings.setCityFieldText(appPreferences.getString(CITY_NAME, ""));
        appSettings.setWindSwitchState(appPreferences.getBoolean(WIND_SWITCH_STATE, true));
        appSettings.setPressureSwitchState(appPreferences.getBoolean(PRESSURE_SWITCH_STATE, true));
        appSettings.setDarkThemeSwitchState(appPreferences.getBoolean(DARK_THEME_SWITCH_STATE, true));
    }

    private void createWeekWeatherList() {
        weekWeatherList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        weekWeatherList.setLayoutManager(linearLayoutManager);

        String[] weekDays = getResources().getStringArray(R.array.weekDaysNames);
        weekWeatherList.setAdapter(new WeekWeatherAdapter(weekDays));

        DividerItemDecoration itemDecoration = new DividerItemDecoration(this,  LinearLayoutManager.VERTICAL);
        itemDecoration.setDrawable(getDrawable(R.drawable.separator));
        weekWeatherList.addItemDecoration(itemDecoration);
    }

    public void openCitySelectActivity(){
        Intent intent;
        intent = new Intent(this, CitySelectionActivity.class);
        startActivity(intent);
    }

    public void openSettingsActivity(){
        Intent intent;
        intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void openBrowserActivity(){
        String cityText = removeCountryFromCityText(cityNameTextView.getText().toString());

        String url = "https://yandex.ru/pogoda/" + cityText;
        Uri uri = Uri.parse(url);

        Intent browser = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(browser);
    }

    public void openDeveloperActivity(){
        Intent intent;
        intent = new Intent(this, DeveloperActivity.class);
        startActivity(intent);
    }
    //Костыль для формата ГОРОД,СТРАНА
    public String removeCountryFromCityText(String rawCityText){
        StringBuilder cityText = new StringBuilder(rawCityText);
        int position = cityText.lastIndexOf(",");
        if(position != -1)
            cityText.delete(position, cityText.length());
        return cityText.toString();
    }

    @Override
    protected void onStart() {
        super.onStart();
        startWeatherUpdateService();

        Log.d(TAG,"onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();

        WeatherProvider.getInstance(this).addListener(this);

        //moved from onActivityResult method (but here is Singleton initialisation)
        if(appSettings.getCityFieldText().isEmpty())
            cityNameTextView.setText(getResources().getString(R.string.no_city_selected));
        else
            cityNameTextView.setText(appSettings.getCityFieldText());

    }

    //Android advanced level - lesson 4 (3) Service
    private void startWeatherUpdateService(){
        startService(new Intent(MainActivity.this, WeatherUpdateService.class));
    }

    @Override
    protected void onPause() {
        WeatherProvider.getInstance(this).removeListener(this);

        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int itemId = item.getItemId();
        if(itemId == R.id.action_settings)
        {
            openSettingsActivity();
        } else if(itemId == R.id.action_yandex_pogoda){
            openBrowserActivity();
        } else if(itemId == R.id.action_about_developer){
            openDeveloperActivity();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateWeather(WeatherModel model) {
        double temperature = WeatherProvider.kelvinToCelsius(model.getMain().getTemp());
        temperatureTextView.setText(String.format("%.1f %s", temperature, getResources().getString(R.string.celsius)));

        String cityName = String.format("%s,%s", model.getName(), model.getSys().getCountry());
        cityNameTextView.setText(cityName);
    }
}
