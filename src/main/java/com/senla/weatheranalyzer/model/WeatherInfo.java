package com.senla.weatheranalyzer.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "weather_info")
public class WeatherInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String region;
    private String country;
    private Long localtimeEpoch;
    private LocalDateTime localTime;

    @Column(name = "temp_c")
    private Double tempC;
    @Column(name = "temp_f")
    private Double tempF;
    private Double windMph;
    private Double pressureMb;
    private Double pressureIn;
    private Double humidity;
    private Double cloud;

}