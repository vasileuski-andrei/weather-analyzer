package com.senla.weatheranalyzer.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WeatherAverageInfoDto {

    private String region;
    private String country;

    private Double averageTempC;
    private Double averageTempF;
    private Double averageWindMph;
    private Double averagePressureMb;
    private Double averageHumidity;
    private Double averageCloud;

}
