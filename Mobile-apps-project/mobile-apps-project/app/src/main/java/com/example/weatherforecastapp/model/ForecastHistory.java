package com.example.weatherforecastapp.model;

import static com.example.weatherforecastapp.util.DateFormatUtil.formatDate;

import androidx.annotation.NonNull;

import java.util.Date;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class ForecastHistory {
    private String id;
    private String cityName;
    private ForecastType forecastType;
    private Date searchDate;
    @NonNull
    @Override
    public String toString() {
        return String.format("City: %s, Forecast Type: %s, Date: %s",
                cityName, forecastType.getLabel(), formatDate(searchDate));
    }
}
