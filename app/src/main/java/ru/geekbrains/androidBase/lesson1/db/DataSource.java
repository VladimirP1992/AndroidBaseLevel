package ru.geekbrains.androidBase.lesson1.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.Closeable;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ru.geekbrains.androidBase.lesson1.AppSettingsSingleton;

public class DataSource implements Closeable {

    private final DataHelper dbHelper;
    private SQLiteDatabase database = null;
    private DataReader reader = null;

    public DataSource(Context context){
        dbHelper = new DataHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
        if(database != null) {
            reader = new DataReader(database);
            reader.open();
        }
    }

    @Override
    public void close() throws IOException {
        if(database != null)
            database.close();
        if (reader != null)
            reader.close();
        AppSettingsSingleton.getInstance().setDataSource(null);
    }

    public void updateHistory(String city, double temperature, double wind, double pressure){
        //move city name to lower case
        String cityString = city.toLowerCase();
        //get nows date
        Date dateNow = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy.MM.dd");
        String dateString = formatForDateNow.format(dateNow);

        ContentValues values;

        //get city id
        long idCity;
        boolean isCityExists = reader.isCityExists(cityString);
        if(isCityExists){
            //just get idCity from city table
            idCity = reader.getIdCity(cityString);
        } else {
            //insert new city to city table
            values = new ContentValues();
            values.put(DataHelper.CITY_NAME, cityString);
            idCity = database.insert(DataHelper.CITIES_TABLE_NAME, null, values);
        }

        //prepare new row for weather table
        values = new ContentValues();
        values.put(DataHelper.ID_CITY, idCity);
        values.put(DataHelper.DATE, dateString);
        values.put(DataHelper.TEMPERATURE, temperature);
        values.put(DataHelper.WIND, wind);
        values.put(DataHelper.PRESSURE, pressure);

        if(isCityExists){
            database.update(DataHelper.WEATHER_INFO_TABLE_NAME, values, DataHelper.ID_CITY + "=" + idCity, null);
        } else {
            database.insert(DataHelper.WEATHER_INFO_TABLE_NAME, null, values);
        }
    }

    public DataReader getReader() {
        return reader;
    }
}
