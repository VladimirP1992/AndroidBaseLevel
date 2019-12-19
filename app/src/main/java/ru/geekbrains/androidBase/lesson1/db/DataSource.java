package ru.geekbrains.androidBase.lesson1.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.Closeable;
import java.io.IOException;
import java.sql.SQLException;

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
    }

    public DbRecord add(String city, String date, int temperature, int wind, int pressure){

        //Todo: CHECK IF CITY ALREADY EXISTS (IN LOW REGISTER) - insert/update

        DbRecord record = new DbRecord();

        ContentValues values = new ContentValues();
        values.put(DataHelper.CITY_NAME, city);

        long idCity = 1; //Todo: database.insert(DataHelper.CITIES_TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(DataHelper.ID_CITY, idCity);
        values.put(DataHelper.DATE, date);
        values.put(DataHelper.TEMPERATURE, temperature);
        values.put(DataHelper.WIND, wind);
        values.put(DataHelper.PRESSURE, pressure);
        long idRecord = database.insert(DataHelper.WEATHER_INFO_TABLE_NAME, null, values);

        record.setId(idRecord);
        record.setIdCity(idCity);
        record.setCity(city);
        record.setDate(date);
        record.setTemperature(temperature);
        record.setWind(wind);
        record.setPressure(pressure);

        return record;
    }

    public void edit(DbRecord record, long idCity, String date, int temperature, int wind, int pressure){
        ContentValues values = new ContentValues();
        values.put(DataHelper.ID_CITY, idCity);
        values.put(DataHelper.DATE, date);
        values.put(DataHelper.TEMPERATURE, temperature);
        values.put(DataHelper.WIND, wind);
        values.put(DataHelper.PRESSURE, pressure);
        database.update(DataHelper.WEATHER_INFO_TABLE_NAME, values, DataHelper.ID + "=" + record.getId(), null);
    }

    public void delete(){
        //Todo: Do i need delete?
    }

    public DataReader getReader() {
        return reader;
    }
}
