package com.example.weatherforecastapp.util;

import com.example.weatherforecastapp.model.Forecast;
import com.example.weatherforecastapp.model.MultiDayForecast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ForecastFactory {

    private static final int UNIX_TIME_STAMP_MULTIPLIER = 1000;

    public static Forecast createForecastFromApiResponse(JSONObject jsonObject) throws JSONException {
        Forecast forecast = new Forecast();
        forecast.setTemperature(extractTemperature(jsonObject));
        forecast.setIcon(extractIcon(jsonObject));
        forecast.setWeatherDescription(extractWeatherDescription(jsonObject));
        return forecast;
    }

    private static String extractWeatherDescription(JSONObject jsonObject) throws JSONException {
        return jsonObject
                .getJSONArray("weather")
                .getJSONObject(0)
                .getString("description");
    }

    public static List<MultiDayForecast> createForecastsFromApiResponse(JSONObject jsonObject) throws JSONException {
        List<MultiDayForecast> forecasts = new ArrayList<>();
        JSONArray jsonArray = jsonObject.getJSONArray("list");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject currentJsonObject = jsonArray.getJSONObject(i);
            forecasts.add(createMultiDayForecastFromApiResponse(currentJsonObject));
        }
        return forecasts;
    }

    private static MultiDayForecast createMultiDayForecastFromApiResponse(JSONObject currentJsonObject) throws JSONException {
        MultiDayForecast multiDayForecast = new MultiDayForecast();
        multiDayForecast.setTemperature(extractTemperature(currentJsonObject));
        multiDayForecast.setIcon(extractIcon(currentJsonObject));
        multiDayForecast.setDate(extractDate(currentJsonObject));
        multiDayForecast.setWeatherDescription(extractWeatherDescription(currentJsonObject));
        return multiDayForecast;
    }

    private static Date extractDate(JSONObject jsonObject) throws JSONException {
        long timestamp = jsonObject.getLong("dt");
        return new Date(timestamp * UNIX_TIME_STAMP_MULTIPLIER);
    }

    private static String extractIcon(JSONObject jsonObject) throws JSONException {
        return jsonObject
                .getJSONArray("weather")
                .getJSONObject(0)
                .getString("icon");
    }

    private static int extractTemperature(JSONObject jsonObject) throws JSONException {
        double temperature =
                jsonObject.getJSONObject("main")
                .getDouble("temp");
        return (int) Math.round(temperature);
    }
}
