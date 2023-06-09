package com.senla.weatheranalyzer.controller;

import com.senla.weatheranalyzer.dto.WeatherAverageInfoDto;
import com.senla.weatheranalyzer.dto.WeatherInfoDto;
import com.senla.weatheranalyzer.service.WeatherService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/weather")
public class WeatherController {

    private final WeatherService weatherService;

    @ExceptionHandler(Exception.class)
    public ResponseEntity exceptionHandler(Exception e) {
        log.error("Failed to return the data: " + e);

        return new ResponseEntity("Check the body of your POST request. Date format should be: dd-MM-yyyy", HttpStatus.BAD_REQUEST);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public WeatherInfoDto getCurrentWeatherInfo() {

        return weatherService.getCurrentWeatherInfo();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public List<WeatherAverageInfoDto> getAverageWeatherInfoBetweenTwoDates(@RequestBody WeatherInfoDto weatherConditionDto) {

        return weatherService.getWeatherAverageInfoBetween(weatherConditionDto);
    }
}
