package com.example.weatherforecastapp.util;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Locale;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DateFormatUtil {
    public static String formatDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-M-yyyy HH:mm:ss",
                Locale.getDefault());
        return simpleDateFormat.format(date);
    }

    public static Date isoDateStringToDate(String isoDateString) {
        Instant instant = Instant.parse(isoDateString);
        return Date.from(instant);
    }

    public static String dateToIsoString(Date date) {
        return date.toInstant().toString();
    }
}
