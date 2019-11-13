package ru.geekbrains.androidBase.lesson1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements ConstantNames{

    private static final String TAG = "Lesson3";
    private static final int REQUEST_CODE = 13579;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button citySelectButton = findViewById(R.id.citySelectButton);
        citySelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCitySelectActivity();
            }
        });

        Button settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettingsActivity();
            }
        });

        //Lesson 4 - exercise 2
        Button browserButton = findViewById(R.id.browserButton);
        browserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBrowserActivity();
            }
        });


        Toast.makeText(getApplicationContext(), "onCreate()", Toast.LENGTH_SHORT).show();
        Log.d(TAG,"onCreate()");

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
        //Lesson 4 - exercise 1
        if(requestCode != REQUEST_CODE) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
        if(resultCode == RESULT_OK){
            TextView cityNameTextView = findViewById(R.id.cityNameTextView);
            LinearLayout windInfo = findViewById(R.id.windInfoLinearLayout);
            LinearLayout pressureInfo = findViewById(R.id.pressureInfoLinearLayout);

            CitySelectionInfoParcel additionalInfo = (CitySelectionInfoParcel) data.getExtras().getSerializable(ADDITIONAL_INFO);
            cityNameTextView.setText(additionalInfo.getCityName());
            if(additionalInfo.isWindChecked()){
                windInfo.setVisibility(View.VISIBLE);
            }else {
                windInfo.setVisibility(View.GONE);
            }
            if(additionalInfo.isPressureChecked()){
                pressureInfo.setVisibility(View.VISIBLE);
            }
            else {
                pressureInfo.setVisibility(View.GONE);
            }
        }
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
        Toast.makeText(getApplicationContext(), "onResume()", Toast.LENGTH_SHORT).show();
        Log.d(TAG,"onResume()");
    }

    @Override
    protected void onPause() {
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

}
