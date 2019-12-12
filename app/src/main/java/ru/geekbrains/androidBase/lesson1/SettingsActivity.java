package ru.geekbrains.androidBase.lesson1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity{

    Button backButton;
    Switch darkThemeSwitch;

    AppSettingsSingleton appSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initViews();
        appSettings = AppSettingsSingleton.getInstance();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBack();
            }
        });

        darkThemeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                appSettings.setDarkThemeSwitchState(isChecked);
            }
        });
    }

    private void initViews(){
        backButton = findViewById(R.id.backButton);
        darkThemeSwitch = findViewById(R.id.darkThemeSwitch);
    }

    @Override
    protected void onResume() {
        super.onResume();

        darkThemeSwitch.setChecked(appSettings.isDarkThemeSwitchChecked());
    }

    public void onBack(){
        finish();
    }
}
