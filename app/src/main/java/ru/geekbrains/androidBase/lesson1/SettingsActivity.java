package ru.geekbrains.androidBase.lesson1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBack();
            }
        });

        Switch darkThemeSwitch = findViewById(R.id.darkThemeSwitch);
        final AppSettingsSingleton presenter = AppSettingsSingleton.getInstance();

        darkThemeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                presenter.setDarkThemeSwitchState(isChecked);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        final Switch darkThemeSwitch = findViewById(R.id.darkThemeSwitch);
        darkThemeSwitch.setChecked(AppSettingsSingleton.getInstance().isDarkThemeSwitchChecked());
    }

    public void onBack(){
        finish();
    }
}
