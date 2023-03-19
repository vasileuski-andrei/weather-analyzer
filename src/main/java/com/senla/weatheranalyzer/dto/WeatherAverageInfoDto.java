package com.senla.weatheranalyzer.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WeatherAverageInfoDto {

    private String region;
    private String country;
    private LocalDate localDateTime;

    @JsonProperty("average_temp_c")
    private Integer averageTempC;

    @JsonProperty("average_temp_f")
    private Integer averageTempF;

    @JsonProperty("average_wind_mph")
    private Double averageWindMph;

    @JsonProperty("average_pressure_mb")
    private Double averagePressureMb;

    @JsonProperty("average_humidity")
    private Integer averageHumidity;

    @JsonProperty("average_cloud")
    private Integer averageCloud;

    @JsonProperty("row_amount")
    private Integer rowAmount;



}
