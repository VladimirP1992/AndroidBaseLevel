package ru.geekbrains.androidBase.lesson1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import ru.geekbrains.androidBase.lesson1.model.WeatherModel;
import ru.geekbrains.androidBase.lesson1.model.WeatherProvider;
import ru.geekbrains.androidBase.lesson1.model.WeatherProviderListener;

public class MainActivity extends AppCompatActivity implements ConstantNames, WeatherProviderListener {

    private static final String TAG = "Lesson3";
    private static final int REQUEST_CODE = 13579;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        Button citySelectButton = findViewById(R.id.citySelectButton);
        citySelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCitySelectActivity();
            }
        });

        //Lesson 6 - exercise 1, 2
        createWeekWeatherList();


        Toast.makeText(getApplicationContext(), "onCreate()", Toast.LENGTH_SHORT).show();
        Log.d(TAG,"onCreate()");

    }

    private void createWeekWeatherList() {
        ////Lesson 6 - exercise 1
        RecyclerView weekWeatherList = findViewById(R.id.weekWeatherList);
        weekWeatherList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        weekWeatherList.setLayoutManager(linearLayoutManager);

        String[] weekDays = getResources().getStringArray(R.array.weekDaysNames);
        //Here may be stringArray localisation
        weekWeatherList.setAdapter(new WeekWeatherAdapter(weekDays));

        //Lesson 6 - exercise 2
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this,  LinearLayoutManager.VERTICAL);
        itemDecoration.setDrawable(getDrawable(R.drawable.separator));
        weekWeatherList.addItemDecoration(itemDecoration);
    }

    public void openCitySelectActivity(){
        Intent intent;
        intent = new Intent(this, CitySelectionActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    public void openSettingsActivity(){
        Intent intent;
        intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void openBrowserActivity(){
        final TextView cityNameTextView = findViewById(R.id.cityNameTextView);
        String url = "https://yandex.ru/pogoda/" + cityNameTextView.getText().toString();
        Uri uri = Uri.parse(url);

        Intent browser = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(browser);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //This logic was moved to this.onResume() and AdditionalInfoFragment.onResume() methods
        super.onActivityResult(requestCode, resultCode, data);
    }

    //==========Life cycle outputs===========================================================================
    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(getApplicationContext(), "onStart()", Toast.LENGTH_SHORT).show();
        Log.d(TAG,"onStart()");
    }

    @Override
    protected void onRestoreInstanceState(Bundle saveInstanceState){
        super.onRestoreInstanceState(saveInstanceState);
        Toast.makeText(getApplicationContext(), "onRestoreInstanceState()", Toast.LENGTH_SHORT).show();
        Log.d(TAG,"onRestoreInstanceState()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Android advanced level - lesson 3 Async Task
        heavyProcedure();

        WeatherProvider.getInstance().addListener(this);

        //moved from onActivityResult method (but here is Singleton initialisation)
        TextView cityNameTextView = findViewById(R.id.cityNameTextView);
        if(CitySelectionSingleton.getInstance().getCityFieldText().isEmpty())
            cityNameTextView.setText(getResources().getString(R.string.no_city_selected));
        else
            cityNameTextView.setText(CitySelectionSingleton.getInstance().getCityFieldText());

        Toast.makeText(getApplicationContext(), "onResume()", Toast.LENGTH_SHORT).show();
        Log.d(TAG,"onResume()");
    }

    private void heavyProcedure() {
        AsyncTask<Integer,String,String> asyncTask = new AsyncTask<Integer, String, String>() {
            @Override
            protected String doInBackground(Integer... value) {
                long start = System.currentTimeMillis();
                Random random = new Random();
                for (int i = 0; i < value[0]; i++){
                    double variable = random.nextInt(5)+1;
                    variable = variable * Math.sin(variable*variable/variable)%variable + Math.pow(Math.abs(variable), Math.cos(variable))/variable*variable*0.5%variable - variable;
                }
                long finish = System.currentTimeMillis();
                String message = String.format("Heavy procedure have been completed at %d seconds", ((finish-start)/1000));

                return message;
            }

            @Override
            protected void onPostExecute(String message) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                super.onPostExecute(message);
            }
        };

        asyncTask.execute(100000000);
    }

    @Override
    protected void onPause() {
        WeatherProvider.getInstance().removeListener(this);
        super.onPause();
        Toast.makeText(getApplicationContext(), "onPause()", Toast.LENGTH_SHORT).show();
        Log.d(TAG,"onPause()");
    }

    @Override
    protected void onSaveInstanceState(Bundle saveInstanceState){
        super.onSaveInstanceState(saveInstanceState);
        Toast.makeText(getApplicationContext(), "onSaveInstanceState()", Toast.LENGTH_SHORT).show();
        Log.d(TAG,"onSaveInstanceState()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(getApplicationContext(), "onStop()", Toast.LENGTH_SHORT).show();
        Log.d(TAG,"onStop()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Toast.makeText(getApplicationContext(), "onRestart()", Toast.LENGTH_SHORT).show();
        Log.d(TAG,"onRestart()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(getApplicationContext(), "onDestroy()", Toast.LENGTH_SHORT).show();
        Log.d(TAG,"onDestroy()");
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
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateWeather(WeatherModel model) {
        ((TextView) findViewById(R.id.temperatureTextView)).setText(String.format("%d C",WeatherProvider.kelvinToCelsius(model.getMain().getTemp())));
    }
}
