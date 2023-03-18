package com.senla.weatheranalyzer.util;

import lombok.experimental.UtilityClass;

import java.time.*;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class CommonUtil {

    public static long getMillisecondsOfStartDay(String date) {
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        LocalDateTime startOfDate = LocalTime.MIN.atDate(localDate);
        ZonedDateTime zoneDateTime = startOfDate.atZone(ZoneId.of("Europe/Minsk"));

        return zoneDateTime.toInstant().toEpochMilli()/1000;
    }

    public static long getMillisecondsOfEndDay(String date) {
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        LocalDateTime startOfDate = LocalTime.MAX.atDate(localDate);
        ZonedDateTime zoneDateTime = startOfDate.atZone(ZoneId.of("Europe/Minsk"));

        return zoneDateTime.toInstant().toEpochMilli()/1000;
    }

    public static long getMillisecondsEndOfDayFromLocalDateTime(LocalDateTime localDateTime) {
        LocalDateTime endOfDate = localDateTime.toLocalDate().atTime(LocalTime.MAX);
        ZonedDateTime zoneDateTime = endOfDate.atZone(ZoneId.of("Europe/Minsk"));

        return  zoneDateTime.toInstant().toEpochMilli() / 1000;
    }

}
