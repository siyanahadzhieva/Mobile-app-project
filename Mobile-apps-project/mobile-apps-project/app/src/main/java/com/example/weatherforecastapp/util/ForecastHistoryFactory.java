package com.example.weatherforecastapp.util;

import com.example.weatherforecastapp.model.ForecastHistory;
import com.example.weatherforecastapp.model.ForecastType;

import java.util.Date;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ForecastHistoryFactory {
    public static ForecastHistory createForecastHistory(String cityName, ForecastType forecastType) {
        return ForecastHistory
                .builder()
                .id(UUID.randomUUID().toString())
                .cityName(cityName)
                .forecastType(forecastType)
                .searchDate(new Date())
                .build();
    }
}
