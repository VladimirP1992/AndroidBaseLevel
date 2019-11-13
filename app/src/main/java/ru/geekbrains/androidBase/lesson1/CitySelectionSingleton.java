package ru.geekbrains.androidBase.lesson1;

public class CitySelectionSingleton {
    private static CitySelectionSingleton instance = null;

    private static final Object syncObj = new Object();

    //City name text
    private String cityFieldText;
    //Switches state
    private boolean windSwitchState;
    private boolean pressureSwitchState;

    private CitySelectionSingleton(){
        cityFieldText = "";
        windSwitchState = true;
        pressureSwitchState = true;
    }

    public String getCityFieldText() {
        return cityFieldText;
    }

    public void setCityFieldText(String cityFieldText) {
        this.cityFieldText = cityFieldText;
    }

    public boolean isWindSwitchChecked() {
        return windSwitchState;
    }

    public void setWindSwitchState(boolean windSwitchState) {
        this.windSwitchState = windSwitchState;
    }

    public boolean isPressureSwitchChecked() {
        return pressureSwitchState;
    }

    public void setPressureSwitchState(boolean pressureSwitchState) {
        this.pressureSwitchState = pressureSwitchState;
    }

    public static CitySelectionSingleton getInstance(){
        synchronized (syncObj) {
            if (instance == null) {
                instance = new CitySelectionSingleton();
            }
            return instance;
        }
    }


}
