package com.example.weatherforecastapp.util;

import static com.example.weatherforecastapp.util.DateFormatUtil.dateToIsoString;
import static com.example.weatherforecastapp.util.DateFormatUtil.isoDateStringToDate;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.weatherforecastapp.model.ForecastHistory;
import com.example.weatherforecastapp.model.ForecastType;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "FORECASTDB";
    private static final int DATABASE_VERSION = 1;
    private static final String CREATE_FORECAST_HISTORY_SQL = "CREATE TABLE FORECAST_HISTORY(" +
            "ID TEXT PRIMARY KEY, " +
            "CITY_NAME TEXT," +
            "FORECAST_TYPE TEXT," +
            "DATE TEXT)";
    private static final String DROP_FORECAST_HISTORY_TABLE_SQL = "DROP TABLE FORECAST_HISTORY";
    private static final String CREATE_FORECAST_HISTORY_RECORD_SQL = "INSERT INTO FORECAST_HISTORY(ID, CITY_NAME, FORECAST_TYPE, DATE) " +
            "VALUES(?, ?, ?, ?)";
    private static final String SELECT_FORECASTS_SQL = "SELECT ID, CITY_NAME, FORECAST_TYPE, DATE FROM FORECAST_HISTORY ORDER BY DATE DESC";
    private static final String DELETE_FORECAST_BY_ID_SQL = "DELETE FROM FORECAST_HISTORY WHERE ID = ?";

    public DatabaseHelper(Context context) {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_FORECAST_HISTORY_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_FORECAST_HISTORY_TABLE_SQL);
        db.execSQL(CREATE_FORECAST_HISTORY_SQL);
    }

    public void saveForecast(ForecastHistory forecastHistory) {
        try(SQLiteDatabase db = getWritableDatabase()) {
            Object[] values = {
                    forecastHistory.getId(),
                    forecastHistory.getCityName(),
                    forecastHistory.getForecastType().toString(),
                    dateToIsoString(forecastHistory.getSearchDate())
            };
            db.execSQL(CREATE_FORECAST_HISTORY_RECORD_SQL, values);
        }
    }

    public List<ForecastHistory> loadAllForecastHistory() {
        List<ForecastHistory> forecastHistories = new ArrayList<>();
        try(SQLiteDatabase sqLiteDatabase = getWritableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery(SELECT_FORECASTS_SQL, null)) {
            while (cursor.moveToNext()) {
                forecastHistories.add(extractForecastHistoryFromCursor(cursor));
            }
        }
        return forecastHistories;
    }

    private ForecastHistory extractForecastHistoryFromCursor(Cursor cursor) {
        return ForecastHistory
                .builder()
                .id(cursor.getString(0))
                .cityName(cursor.getString(1))
                .forecastType(ForecastType.valueOf(cursor.getString(2)))
                .searchDate(isoDateStringToDate(cursor.getString(3)))
                .build();
    }

    public void deleteForecast(String forecastId) {
        try(SQLiteDatabase writableDatabase = getWritableDatabase()) {
            Object[] queryArgs = new Object[] {
                    forecastId
            };
            writableDatabase.execSQL(DELETE_FORECAST_BY_ID_SQL, queryArgs);
        }
    }
}