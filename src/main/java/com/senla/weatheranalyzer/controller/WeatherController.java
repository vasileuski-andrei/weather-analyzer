package com.senla.weatheranalyzer.controller;

import com.senla.weatheranalyzer.dto.WeatherInfoDto;
import com.senla.weatheranalyzer.service.WeatherService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping("/weather")
    public WeatherInfoDto getCurrentWeatherInfo() {
        return weatherService.getCurrentWeatherInfo();
    }
}
