package ru.geekbrains.androidBase.lesson1.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DataHelper extends SQLiteOpenHelper {

    //Database info
    private static final String DB_NAME = "selected_cities.db";
    private static final int DB_VERSION = 1;

    //Table names
    private static final String CITIES_TABLE_NAME = "cities";
    private static final String WEATHER_INFO_TABLE_NAME = "weather_info";

    //Field names
    private static final String ID = "id";
    private static final String ID_CITY = "id_city";
    private static final String DATE = "date";
    private static final String TEMPERATURE = "temp";
    private static final String WIND = "wind";
    private static final String PRESSURE = "press";
    private static final String CITY_NAME = "city";

    public DataHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder query = new StringBuilder
                ("CREATE TABLE ").append(CITIES_TABLE_NAME).append(" (").
                append(ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT,").
                append(CITY_NAME).append(" TEXT);");

        db.execSQL(query.toString());

        query = new StringBuilder
                ("CREATE TABLE ").append(WEATHER_INFO_TABLE_NAME).append(" (").
                append(ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT,").
                append(ID_CITY).append(" INTEGER,").
                append(DATE).append(" TEXT,").
                append(TEMPERATURE).append(" INTEGER,").
                append(WIND).append(" INTEGER,").
                append(PRESSURE).append(" INTEGER);");

        db.execSQL(query.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion == 1 && newVersion == 2){
            //Maybe it will happen
        }
    }
}
