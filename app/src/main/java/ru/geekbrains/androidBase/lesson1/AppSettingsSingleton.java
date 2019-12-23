package ru.geekbrains.androidBase.lesson1;

import ru.geekbrains.androidBase.lesson1.db.DataSource;

public class AppSettingsSingleton {
    private static AppSettingsSingleton instance = null;

    //City name text
    private String cityFieldText;
    //Switch states
    private boolean windSwitchState;
    private boolean pressureSwitchState;
    private boolean darkThemeSwitchState;

    //Database source
    private DataSource dataSource;

    private AppSettingsSingleton(){
        cityFieldText = "";
        windSwitchState = true;
        pressureSwitchState = true;
        darkThemeSwitchState = true;
        dataSource = null;
    }

    public synchronized String getCityFieldText() {
        return cityFieldText;
    }

    public synchronized void setCityFieldText(String cityFieldText) {
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

    public synchronized DataSource getDataSource() {
        return dataSource;
    }

    public synchronized void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static AppSettingsSingleton getInstance(){
        return instance = (instance == null) ? new AppSettingsSingleton() : instance;
    }


}
