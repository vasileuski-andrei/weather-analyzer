package com.senla.weatheranalyzer.service;

import com.senla.weatheranalyzer.dto.WeatherAverageInfoDto;
import com.senla.weatheranalyzer.dto.WeatherInfoDto;

import java.util.List;

public interface WeatherService {

    WeatherInfoDto getCurrentWeatherInfo();

    List<WeatherAverageInfoDto> getWeatherAverageInfoBetween(WeatherInfoDto weatherInfoDto);
    void save(WeatherInfoDto weatherInfoDto);
}
