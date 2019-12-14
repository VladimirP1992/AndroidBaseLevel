package ru.geekbrains.androidBase.lesson1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.google.android.material.textfield.TextInputLayout;

public class CitySelectionActivity extends AppCompatActivity implements ConstantNames{

    TextInputLayout cityTextInputLayout;
    Switch windSwitch;
    Switch pressureSwitch;
    Button backButton;

    AppSettingsSingleton appSettings;
    SharedPreferences appPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_selection);

        initViews();
        appSettings = AppSettingsSingleton.getInstance();
        appPreferences = getSharedPreferences(APP_SHARED_PREFERENCES_NAME, MODE_PRIVATE);

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
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                appSettings.setCityFieldText(cityTextInputLayout.getEditText().getText().toString());
                appPreferences.edit().putString(CITY_NAME, cityTextInputLayout.getEditText().getText().toString()).apply();
            }

            @Override
            public void afterTextChanged(Editable s) { }
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
    }

    private void initViews(){
        backButton = findViewById(R.id.backButton);
        cityTextInputLayout = findViewById(R.id.cityTextInputLayout);
        windSwitch = findViewById(R.id.windSwitch);
        pressureSwitch = findViewById(R.id.pressureSwitch);
    }

    @Override
    protected void onResume() {
        super.onResume();

        cityTextInputLayout.getEditText().setText(appSettings.getCityFieldText());
        windSwitch.setChecked(appSettings.isWindSwitchChecked());
        pressureSwitch.setChecked(appSettings.isPressureSwitchChecked());

    }

    public void onBack(){
        //Lesson4 - exercise 1
        CitySelectionInfoParcel additionalWeatherInfo = new CitySelectionInfoParcel(cityTextInputLayout.getEditText().getText().toString(), windSwitch.isChecked(), pressureSwitch.isChecked());

        Intent intentResult = new Intent();
        intentResult.putExtra(ADDITIONAL_INFO, additionalWeatherInfo);
        setResult(RESULT_OK, intentResult);

        finish();
    }
}
