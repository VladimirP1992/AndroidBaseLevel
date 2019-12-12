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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initViews();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBack();
            }
        });

        final AppSettingsSingleton presenter = AppSettingsSingleton.getInstance();

        darkThemeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                presenter.setDarkThemeSwitchState(isChecked);
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

        darkThemeSwitch.setChecked(AppSettingsSingleton.getInstance().isDarkThemeSwitchChecked());
    }

    public void onBack(){
        finish();
    }


    @Override
    protected void onPause() {
        savePreferences();
        super.onPause();
    }

    private void savePreferences() {
        SharedPreferences appPreferences = getPreferences(MODE_PRIVATE);
        AppSettingsSingleton appSettingsSingleton = AppSettingsSingleton.getInstance();

        appPreferences.edit().putBoolean("darkThemeSwitchState", appSettingsSingleton.isDarkThemeSwitchChecked()).apply();
    }
}
