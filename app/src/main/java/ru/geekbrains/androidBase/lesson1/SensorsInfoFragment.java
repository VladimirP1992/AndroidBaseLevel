package ru.geekbrains.androidBase.lesson1;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class SensorsInfoFragment extends Fragment {
    private TextView temperatureSensorValue;
    private TextView humiditySensorValue;
    private SensorManager sensorManager;
    private SensorEventListener temperatureSensorListener;
    private SensorEventListener humiditySensorListener;

    public SensorsInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sensors_info, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        Sensor temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        Sensor humiditySensor = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);

        temperatureSensorValue = getActivity().findViewById(R.id.sensorTemperatureValue);
        humiditySensorValue = getActivity().findViewById(R.id.sensorHumidityValue);

        temperatureSensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                temperatureSensorValue.setText(String.format("%f", event.values[0]));
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
        humiditySensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                humiditySensorValue.setText(String.format("%f", event.values[0]));
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        sensorManager.registerListener(temperatureSensorListener, temperatureSensor, 1000);
        sensorManager.registerListener(humiditySensorListener, humiditySensor, 1000);
    }

    @Override
    public void onPause() {
        sensorManager.unregisterListener(temperatureSensorListener);
        sensorManager.unregisterListener(humiditySensorListener);
        super.onPause();
    }
}
