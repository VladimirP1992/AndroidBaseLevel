package ru.geekbrains.androidBase.lesson1.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.Closeable;
import java.io.IOException;

public class DataReader implements Closeable {
    private final SQLiteDatabase database;
    private Cursor cursor;
    String[] allColumn =
            {
                    DataHelper.ID,
                    DataHelper.ID_CITY,
                    //DataHelper.CITY_NAME,
                    DataHelper.DATE,
                    DataHelper.TEMPERATURE,
                    DataHelper.WIND,
                    DataHelper.PRESSURE
            };

    public DataReader(SQLiteDatabase database) {
        this.database = database;
    }

    public void open(){
        query();
        cursor.moveToFirst();
    }

    private void query() {
        cursor = database.query(DataHelper.WEATHER_INFO_TABLE_NAME, allColumn,
                null,
                null,
                null,
                null,
                null);
    }

    public void refresh(){
        int position = cursor.getPosition();
        query();
        cursor.moveToPosition(position);
    }

    @Override
    public void close() throws IOException {
        cursor.close();
    }

    private DbRecord cursorToDbRecord(){
        DbRecord record = new DbRecord();

        record.setId(cursor.getLong(0));
        record.setIdCity(cursor.getLong(1));
        //Todo: record.setCity();
        record.setDate(cursor.getString(2));
        record.setTemperature(cursor.getInt(3));
        record.setWind(cursor.getInt(4));
        record.setPressure(cursor.getInt(5));

        return record;
    }

    public DbRecord getPosition(int position){
        cursor.moveToPosition(position);
        return cursorToDbRecord();
    }

    public int getCount(){
        return cursor.getCount();
    }
}
