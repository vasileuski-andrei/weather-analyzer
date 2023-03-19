package com.senla.weatheranalyzer.util;

import lombok.experimental.UtilityClass;

import java.time.*;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class DateTimeUtil {

    public static final String CITY = "Europe/Minsk";

    public static long getMillisecondsOfStartDay(String date) {
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        LocalDateTime startOfDate = LocalTime.MIN.atDate(localDate);

        return getMillisecondsFromLocalDateTime(startOfDate);
    }

    public static long getMillisecondsOfEndDay(String date) {
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        LocalDateTime endOfDate = LocalTime.MAX.atDate(localDate);

        return getMillisecondsFromLocalDateTime(endOfDate);
    }

    public static long getMillisecondsEndOfDayFromLocalDateTime(LocalDateTime localDateTime) {
        LocalDateTime endOfDate = localDateTime.toLocalDate().atTime(LocalTime.MAX);

        return getMillisecondsFromLocalDateTime(endOfDate);
    }

    public long getMillisecondsFromLocalDateTime(LocalDateTime localDateTime) {
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of(CITY));

        return zonedDateTime.toInstant().toEpochMilli() / 1000;
    }

}
