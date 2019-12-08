package ru.geekbrains.androidBase.lesson1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.google.android.material.textfield.TextInputLayout;

public class CitySelectionActivity extends AppCompatActivity implements ConstantNames{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_selection);

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBack();
            }
        });

        //Lesson 3 - exercise 4
        //create singleton
        final CitySelectionSingleton presenter = CitySelectionSingleton.getInstance();

        //find all views that should be saved
        final TextInputLayout cityNameField = findViewById(R.id.cityTextInputLayout);
        final Switch windSwitch = findViewById(R.id.windSwitch);
        final Switch pressureSwitch = findViewById(R.id.pressureSwitch);

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

    public void onBack(){
        //Lesson4 - exercise 1
        final TextInputLayout cityNameField = findViewById(R.id.cityTextInputLayout);
        final Switch windSwitch = findViewById(R.id.windSwitch);
        final Switch pressureSwitch = findViewById(R.id.pressureSwitch);
        CitySelectionInfoParcel additionalWeatherInfo = new CitySelectionInfoParcel(cityNameField.getEditText().getText().toString(), windSwitch.isChecked(), pressureSwitch.isChecked());

        Intent intentResult = new Intent();
        intentResult.putExtra(ADDITIONAL_INFO, additionalWeatherInfo);
        setResult(RESULT_OK, intentResult);

        finish();
    }
}
