package com.senla.weatheranalyzer.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.senla.weatheranalyzer.dto.WeatherInfoDto;

import com.senla.weatheranalyzer.service.WeatherApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ParserWeatherRapid implements Parser {

    private WeatherApiService weatherApiService;

    @Autowired
    public ParserWeatherRapid(WeatherApiService weatherApiService) {
        this.weatherApiService = weatherApiService;
    }

    @Override
    public WeatherInfoDto parse() {
        String dataFromApi = weatherApiService.getDataFromApi();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = null;

        try {
            jsonNode = mapper.readTree(dataFromApi);

        } catch (JsonProcessingException e) {
            log.error("JSON parsing error, JSON: {}", dataFromApi, e);

        }

        JsonNode location = jsonNode.get("location");
        JsonNode current = jsonNode.get("current");

        WeatherInfoDto weatherInfoDto = WeatherInfoDto.builder()
                .name(location.get("name").asText())
                .region(location.get("region").asText())
                .country(location.get("country").asText())
                .timezoneId(location.get("tz_id").asText())
                .localtime_epoch(location.get("localtime_epoch").asText())
                .localtime(location.get("localtime").asText())

                .tempC(current.get("temp_c").asDouble())
                .tempF(current.get("temp_f").asDouble())
                .windMph(current.get("wind_mph").asDouble())
                .pressureMb(current.get("pressure_mb").asDouble())
                .pressureIn(current.get("pressure_in").asDouble())
                .humidity(current.get("humidity").asDouble())
                .cloud(current.get("cloud").asDouble())
                .build();

        log.info("Parse the data weather finished successfully. WeatherInfoDto was created");

        return weatherInfoDto;

    }
}
