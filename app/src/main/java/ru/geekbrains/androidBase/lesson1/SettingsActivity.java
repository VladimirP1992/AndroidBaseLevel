package ru.geekbrains.androidBase.lesson1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity implements ConstantNames{

    Button backButton;
    Switch darkThemeSwitch;

    AppSettingsSingleton appSettings;
    SharedPreferences appPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initViews();
        appSettings = AppSettingsSingleton.getInstance();
        appPreferences = getSharedPreferences(APP_SHARED_PREFERENCES_NAME, MODE_PRIVATE);

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
                appPreferences.edit().putBoolean(DARK_THEME_SWITCH_STATE, isChecked).apply();
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
