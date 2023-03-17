package com.senla.weatheranalyzer.controller;

import com.senla.weatheranalyzer.dto.WeatherInfoDto;
import com.senla.weatheranalyzer.service.WeatherService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/api/v1/weather")
@AllArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping
    public WeatherInfoDto getCurrentWeatherInfo() {
        return weatherService.getCurrentWeatherInfo();
    }

    @PostMapping
    public WeatherInfoDto getAverageWeatherInfoBetweenTwoDates(@RequestBody WeatherInfoDto weatherConditionDto) {
        return weatherService.getWeatherInfoBetween(weatherConditionDto);

    }
}
