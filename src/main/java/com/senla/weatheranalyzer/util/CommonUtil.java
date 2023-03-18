package com.senla.weatheranalyzer.util;

import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class CommonUtil {

    public static long convertTimeToMilliseconds(String date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime localDateTime = LocalDateTime.parse(date, dateTimeFormatter);
        ZonedDateTime zoneDateTime = localDateTime.atZone(ZoneId.of("Europe/Minsk"));
        long epochMilliseconds = zoneDateTime.toInstant().toEpochMilli()/1000;

        return epochMilliseconds;
    }
}
