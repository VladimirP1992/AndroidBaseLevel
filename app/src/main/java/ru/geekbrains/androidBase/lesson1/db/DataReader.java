package ru.geekbrains.androidBase.lesson1.db;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.io.Closeable;
import java.io.IOException;

public class DataReader implements Closeable {
    private final SQLiteDatabase database;

    private Cursor cursorWeatherTable;
    private String[] allColumnWeatherTable =
            {
                    DataHelper.ID,
                    DataHelper.ID_CITY,
                    DataHelper.DATE,
                    DataHelper.TEMPERATURE,
                    DataHelper.WIND,
                    DataHelper.PRESSURE
            };
    //Used locally in method
    private Cursor cursorCityTable;
    private String[] allColumnCityTable =
            {
                    DataHelper.ID,
                    DataHelper.CITY_NAME,
            };

    public DataReader(SQLiteDatabase database) {
        this.database = database;
    }

    public void open(){
        query();
        cursorWeatherTable.moveToFirst();
    }

    private void query() {
        cursorWeatherTable = database.query(DataHelper.WEATHER_INFO_TABLE_NAME, allColumnWeatherTable,
                null,
                null,
                null,
                null,
                null);
    }

    private Cursor cityQuery(String selection){
        Cursor cursor = database.query(DataHelper.CITIES_TABLE_NAME, allColumnCityTable,
                selection,
                null,
                null,
                null,
                null);

        return cursor;
    }

    private String getCityName(long idCity){
        cursorCityTable = cityQuery(DataHelper.ID + "=" + idCity);

        cursorCityTable.moveToFirst();
        String cityName = cursorCityTable.getString(1);
        cursorCityTable.close();
        return cityName;
    }

    public boolean isCityExists(String cityName){
        Cursor cursor = cityQuery(DataHelper.CITY_NAME + "='" + cityName + "'");
        boolean result = cursor.moveToFirst();
        cursor.close();

        return result;
    }

    public long getIdCity(String cityName) throws SQLException {
        Cursor cursor = cityQuery(DataHelper.CITY_NAME + "='" + cityName + "'");
        if(cursor.moveToFirst()){
            return cursor.getLong(0);
        }
        throw new SQLException("City was not found!");
    }

    public void refresh(){
        int positionWeather = cursorWeatherTable.getPosition();
        query();
        cursorWeatherTable.moveToPosition(positionWeather);
    }

    @Override
    public void close() throws IOException {
        cursorWeatherTable.close();
    }

    private DbRecord cursorToDbRecord(){
        DbRecord record = new DbRecord();

        record.setId(cursorWeatherTable.getLong(0));
        long idCity = cursorWeatherTable.getLong(1);
        record.setIdCity(idCity);
        record.setCity(getCityName(idCity));
        record.setDate(cursorWeatherTable.getString(2));
        record.setTemperature(cursorWeatherTable.getInt(3));
        record.setWind(cursorWeatherTable.getInt(4));
        record.setPressure(cursorWeatherTable.getInt(5));

        return record;
    }

    public DbRecord getPosition(int position){
        cursorWeatherTable.moveToPosition(position);
        return cursorToDbRecord();
    }

    public int getCount(){
        return cursorWeatherTable.getCount();
    }
}
