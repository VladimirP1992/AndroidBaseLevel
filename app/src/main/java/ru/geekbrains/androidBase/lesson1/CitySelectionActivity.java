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

    TextInputLayout cityNameField;
    Switch windSwitch;
    Switch pressureSwitch;
    Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_selection);

        initViews();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBack();
            }
        });

        //Lesson 3 - exercise 4
        //create singleton
        final AppSettingsSingleton presenter = AppSettingsSingleton.getInstance();

        //get saved information from singleton
        cityNameField.getEditText().setText(presenter.getCityFieldText());
        windSwitch.setChecked(presenter.isWindSwitchChecked());
        pressureSwitch.setChecked(presenter.isPressureSwitchChecked());

        //Save changes
        cityNameField.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                presenter.setCityFieldText(cityNameField.getEditText().getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        windSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                presenter.setWindSwitchState(isChecked);
            }
        });

        pressureSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                presenter.setPressureSwitchState(isChecked);
            }
        });
    }

    private void initViews(){
        backButton = findViewById(R.id.backButton);
        cityNameField = findViewById(R.id.cityTextInputLayout);
        windSwitch = findViewById(R.id.windSwitch);
        pressureSwitch = findViewById(R.id.pressureSwitch);
    }

    @Override
    protected void onResume() {
        super.onResume();

        AppSettingsSingleton appSettingsSingleton = AppSettingsSingleton.getInstance();
        cityNameField.getEditText().setText(appSettingsSingleton.getCityFieldText());
        windSwitch.setChecked(appSettingsSingleton.isWindSwitchChecked());
        pressureSwitch.setChecked(appSettingsSingleton.isPressureSwitchChecked());

    }

    @Override
    protected void onPause() {
        savePreferences();
        super.onPause();
    }

    private void savePreferences() {
        SharedPreferences appPreferences = getPreferences(MODE_PRIVATE);
        AppSettingsSingleton appSettingsSingleton = AppSettingsSingleton.getInstance();

        appPreferences.edit().putString("cityName", appSettingsSingleton.getCityFieldText()).apply();
        appPreferences.edit().putBoolean("windSwitchState", appSettingsSingleton.isWindSwitchChecked()).apply();
        appPreferences.edit().putBoolean("pressureSwitchState", appSettingsSingleton.isPressureSwitchChecked()).apply();
    }

    public void onBack(){
        //Lesson4 - exercise 1
        CitySelectionInfoParcel additionalWeatherInfo = new CitySelectionInfoParcel(cityNameField.getEditText().getText().toString(), windSwitch.isChecked(), pressureSwitch.isChecked());

        Intent intentResult = new Intent();
        intentResult.putExtra(ADDITIONAL_INFO, additionalWeatherInfo);
        setResult(RESULT_OK, intentResult);

        finish();
    }
}
