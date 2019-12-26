package ru.geekbrains.androidBase.lesson1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.sql.SQLException;

import ru.geekbrains.androidBase.lesson1.db.DataSource;
import ru.geekbrains.androidBase.lesson1.db.DbRecordAdapter;
import ru.geekbrains.androidBase.lesson1.model.WeatherProvider;

public class CitySelectionActivity extends AppCompatActivity implements ConstantNames{

    private TextInputLayout cityTextInputLayout;
    private Switch windSwitch;
    private Switch pressureSwitch;
    private Button backButton;
    private RecyclerView selectedCitiesList;

    private static final int PERMISSION_REQUEST_CODE = 10;
    Handler handler;
    private Button findPlaceButton;
    private TextView latitudeValue;
    private TextView longitudeValue;


    private AppSettingsSingleton appSettings;
    private SharedPreferences appPreferences;

    private DbRecordAdapter adapter;
    private DataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_selection);

        appSettings = AppSettingsSingleton.getInstance();
        appPreferences = getSharedPreferences(APP_SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        handler = new Handler();

        initViews();
        initListeners();
        createCityList();

        getLocation();
    }

    private void initViews(){
        backButton = findViewById(R.id.backButton);
        cityTextInputLayout = findViewById(R.id.cityTextInputLayout);
        windSwitch = findViewById(R.id.windSwitch);
        pressureSwitch = findViewById(R.id.pressureSwitch);
        selectedCitiesList = findViewById(R.id.previouslySelectedCitiesList);

        findPlaceButton = findViewById(R.id.findPlaceButton);
        latitudeValue = findViewById(R.id.latitudeValue);
        longitudeValue = findViewById(R.id.longitudeValue);
    }
    private void initListeners(){
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBack();
            }
        });

        //Save changes
        cityTextInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                appSettings.setCityFieldText(cityTextInputLayout.getEditText().getText().toString());
                appPreferences.edit().putString(CITY_NAME, cityTextInputLayout.getEditText().getText().toString()).apply();
            }
        });

        windSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                appSettings.setWindSwitchState(isChecked);
                appPreferences.edit().putBoolean(WIND_SWITCH_STATE, isChecked).apply();
            }
        });

        pressureSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                appSettings.setPressureSwitchState(isChecked);
                appPreferences.edit().putBoolean(PRESSURE_SWITCH_STATE, isChecked).apply();
            }
        });

        findPlaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    double latitude = Double.valueOf(latitudeValue.getText().toString());
                    double longitude = Double.valueOf(longitudeValue.getText().toString());
                    setCityNameTextByCoordinates(latitude, longitude);
                }
                catch (NumberFormatException e){
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.cant_find_city_by_coordinate), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }
        });
    }

    private void setCityNameTextByCoordinates(final double latitude, final double longitude){
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String cityName = WeatherProvider.getInstance(getApplicationContext()).getCityName(latitude, longitude);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        cityTextInputLayout.getEditText().setText(cityName);
                    }
                });
            }
        }).start();
    }

    private void getLocation(){
        // check permission before get location (ask for permission if it necessary)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            requestLocation();
        } else {
            requestLocationPermissions();
        }
    }

    private void requestLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        String provider = locationManager.getBestProvider(criteria, true);

        if (provider != null) {
            LocationListener locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    String latitude = String.format("%.2f", location.getLatitude());
                    String longitude = String.format("%.2f", location.getLongitude());

                    latitudeValue.setText(latitude);
                    longitudeValue.setText(longitude);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) { }
                @Override
                public void onProviderEnabled(String provider) { }
                @Override
                public void onProviderDisabled(String provider) { }
            };

            locationManager.requestLocationUpdates(provider, 10000, 10, locationListener);
        }
    }

    private void requestLocationPermissions() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length == 2 &&
                    (grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                requestLocation();
            }
        }
    }

    private void createCityList(){
        dataSource = new DataSource(this);
        try {
            dataSource.open();
            AppSettingsSingleton.getInstance().setDataSource(dataSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        selectedCitiesList.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        selectedCitiesList.setLayoutManager(layoutManager);
        adapter = new DbRecordAdapter(dataSource.getReader());
        //Todo: maybe some onClickListeners
        selectedCitiesList.setAdapter(adapter);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this,  LinearLayoutManager.VERTICAL);
        itemDecoration.setDrawable(getDrawable(R.drawable.separator));
        selectedCitiesList.addItemDecoration(itemDecoration);
    }

    private void refreshData(){
        dataSource.getReader().refresh();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();

        cityTextInputLayout.getEditText().setText(appSettings.getCityFieldText());
        windSwitch.setChecked(appSettings.isWindSwitchChecked());
        pressureSwitch.setChecked(appSettings.isPressureSwitchChecked());

        refreshData();
    }

    public void onBack(){
        finish();
    }
}
