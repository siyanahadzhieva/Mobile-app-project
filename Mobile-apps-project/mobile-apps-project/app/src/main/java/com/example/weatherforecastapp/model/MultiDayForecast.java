package com.example.weatherforecastapp.model;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class MultiDayForecast extends Forecast {
    private Date date;
}
