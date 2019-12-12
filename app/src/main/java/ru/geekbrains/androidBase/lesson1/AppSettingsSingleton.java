package ru.geekbrains.androidBase.lesson1;

public class AppSettingsSingleton {
    private static AppSettingsSingleton instance = null;

    private static final Object syncObj = new Object();

    //City name text
    private String cityFieldText;
    //Switches state
    private boolean windSwitchState;
    private boolean pressureSwitchState;
    private boolean darkThemeSwitchState;

    private AppSettingsSingleton(){
        cityFieldText = "";
        windSwitchState = true;
        pressureSwitchState = true;
        darkThemeSwitchState = true;
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

    public boolean isDarkThemeSwitchChecked(){
        return darkThemeSwitchState;
    }

    public void setDarkThemeSwitchState(boolean darkThemeSwitchState){
        this.darkThemeSwitchState = darkThemeSwitchState;
    }

    public static AppSettingsSingleton getInstance(){
        synchronized (syncObj) {
            if (instance == null) {
                instance = new AppSettingsSingleton();
            }
            return instance;
        }
    }


}
