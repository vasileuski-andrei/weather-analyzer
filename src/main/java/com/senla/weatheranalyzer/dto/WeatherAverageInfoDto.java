package com.senla.weatheranalyzer.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("average_temp_c")
    private Double averageTempC;

    @JsonProperty("average_temp_f")
    private Double averageTempF;

    @JsonProperty("average_wind_mph")
    private Double averageWindMph;

    @JsonProperty("average_pressure_mb")
    private Double averagePressureMb;

    @JsonProperty("average_humidity")
    private Double averageHumidity;

    @JsonProperty("average_cloud")
    private Double averageCloud;

}
