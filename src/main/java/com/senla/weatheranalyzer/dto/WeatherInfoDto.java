package com.senla.weatheranalyzer.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WeatherInfoDto {

    private String name;
    private String region;
    private String country;
    private Long localtimeEpoch;
    private LocalDateTime localTime;
    @JsonProperty("temp_c")
    private Double tempC;
    private Double tempF;
    @JsonProperty("wind_mph")
    private Double windMph;
    @JsonProperty("pressure_mb")
    private Double pressureMb;
    private Double pressureIn;
    private Double humidity;
    private Double cloud;

    private String from;
    private String to;

}
