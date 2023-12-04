package com.example.weatherforecastapp.model;

import lombok.Data;

@Data
public class Forecast {
    private int temperature;
    private String icon;
    private String weatherDescription;
}
