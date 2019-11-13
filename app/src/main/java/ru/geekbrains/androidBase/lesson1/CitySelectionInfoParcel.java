package ru.geekbrains.androidBase.lesson1;

import java.io.Serializable;

public class CitySelectionInfoParcel implements Serializable {
    private String cityName;
    private boolean windIsChecked;
    private boolean pressureIsChecked;

    public CitySelectionInfoParcel(String cityName, boolean windIsChecked, boolean pressureIsChecked) {
        this.cityName = cityName;
        this.windIsChecked = windIsChecked;
        this.pressureIsChecked = pressureIsChecked;
    }


    public String getCityName() {
        return cityName;
    }

    public boolean isWindChecked() {
        return windIsChecked;
    }

    public boolean isPressureChecked() {
        return pressureIsChecked;
    }
}
