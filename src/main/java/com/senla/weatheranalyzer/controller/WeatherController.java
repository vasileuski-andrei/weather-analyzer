package com.senla.weatheranalyzer.controller;
//controller в пакете model не правильно, пакет controller идет 4 пакетом никак не в model

import com.senla.weatheranalyzer.dto.WeatherAverageInfoDto;
import com.senla.weatheranalyzer.dto.WeatherInfoDto;
import com.senla.weatheranalyzer.service.WeatherService;
import com.senla.weatheranalyzer.service.impl.WeatherServiceImpl;
//не используемый импорт "грязь в коде"
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
//https://www.baeldung.com/solid-principles
    //Dependency Inversion инжестим интерфейс а не реализацию
    // зависимость от абстракции а не от реализации
    private final WeatherService weatherService;


    //Обычно выносят в @ControllerAdvice
    //https://www.baeldung.com/exception-handling-for-rest-with-spring
    @ExceptionHandler(Exception.class)
    //Старайся не использовать сырые типы, идея тоже подсвечивает
    public ResponseEntity<String> exceptionHandler(Exception e) {
        log.error("Failed to return the data: " + e);

        return new ResponseEntity<>("Check the body of your POST request. Date format should be: dd-MM-yyyy", HttpStatus.BAD_REQUEST);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    //https://www.baeldung.com/spring-response-entity
    //если возвращаешь HttpStatus используй ResponseEntity<WeatherInfoDto>
    public WeatherInfoDto getCurrentWeatherInfo() {

        return weatherService.getCurrentWeatherInfo();
    }

    @PostMapping
    public ResponseEntity<List<WeatherAverageInfoDto>>  getAverageWeatherInfoBetweenTwoDates(@RequestBody WeatherInfoDto weatherConditionDto) {

        return ResponseEntity.ok(weatherService.getWeatherAverageInfoBetween(weatherConditionDto));
    }
}
