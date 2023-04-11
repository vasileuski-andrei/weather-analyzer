package com.senla.weatheranalyzer.parser;

import com.senla.weatheranalyzer.dto.WeatherInfoDto;

public interface Parser {
    WeatherInfoDto parse(String data);
}
