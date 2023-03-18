package com.senla.weatheranalyzer.controller;

import com.senla.weatheranalyzer.dto.WeatherAverageInfoDto;
import com.senla.weatheranalyzer.dto.WeatherInfoDto;
import com.senla.weatheranalyzer.service.WeatherService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
    public WeatherAverageInfoDto getAverageWeatherInfoBetweenTwoDates(@RequestBody WeatherInfoDto weatherConditionDto) {
        return weatherService.getWeatherAverageInfoBetween(weatherConditionDto);
    }
}
