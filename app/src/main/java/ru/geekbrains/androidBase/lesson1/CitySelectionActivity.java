package ru.geekbrains.androidBase.lesson1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

public class CitySelectionActivity extends AppCompatActivity{

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
        final AutoCompleteTextView cityNameField = findViewById(R.id.cityAutoCompleteTextView);
        Switch windSwitch = findViewById(R.id.windSwitch);
        Switch pressureSwitch = findViewById(R.id.pressureSwitch);

        //get saved information from singleton
        cityNameField.setText(presenter.getCityFieldText());
        windSwitch.setChecked(presenter.isWindSwitchChecked());
        pressureSwitch.setChecked(presenter.isPressureSwitchChecked());

        //Save changes
//        cityNameField.setOnHoverListener(new View.OnHoverListener() {
//            @Override
//            public boolean onHover(View v, MotionEvent event) {
//                presenter.setCityFieldText(cityNameField.getText().toString());
//                return false;
//            }
//        });
        cityNameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                presenter.setCityFieldText(cityNameField.getText().toString());
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
        finish();
    }
}
