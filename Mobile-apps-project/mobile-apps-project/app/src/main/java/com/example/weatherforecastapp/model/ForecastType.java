package com.example.weatherforecastapp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ForecastType {
    TODAY("Today"), FIVE_DAY("Five day");

    private final String label;


}
