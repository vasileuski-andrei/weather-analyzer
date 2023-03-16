package com.senla.weatheranalyzer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeatherInfoDto {

    private String name;
    private String region;
    private String country;
    private String timezoneId;
    private String localtime_epoch;
    private String localtime;

    private double tempC;
    private double tempF;
    private double windMph;
    private double pressureMb;
    private double pressureIn;
    private double humidity;
    private double cloud;

}
