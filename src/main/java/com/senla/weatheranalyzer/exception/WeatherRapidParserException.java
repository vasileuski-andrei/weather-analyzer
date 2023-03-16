package com.senla.weatheranalyzer.exception;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;

public class WeatherRapidParserException extends IOException {

    public WeatherRapidParserException(String msg) {
        super(msg);
    }
}
