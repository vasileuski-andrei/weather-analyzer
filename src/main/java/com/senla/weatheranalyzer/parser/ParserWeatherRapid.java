package com.senla.weatheranalyzer.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.senla.weatheranalyzer.dto.WeatherInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class ParserWeatherRapid implements Parser {

    @Override
    public WeatherInfoDto parse(String data) {
        ObjectMapper mapper = new ObjectMapper();
        WeatherInfoDto weatherInfoDto = null;
        JsonNode jsonNode = null;

        try {
            jsonNode = mapper.readTree(data);

            JsonNode location = jsonNode.get("location");
            JsonNode current = jsonNode.get("current");

            weatherInfoDto = WeatherInfoDto.builder()
                    .name(location.get("name").asText())
                    .region(location.get("region").asText())
                    .country(location.get("country").asText())
                    .localtimeEpoch(location.get("localtime_epoch").asLong())
                    .localTime(LocalDateTime.parse(location.get("localtime").asText(), DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm")))

                    .tempC(current.get("temp_c").asDouble())
                    .tempF(current.get("temp_f").asDouble())
                    .windMph(current.get("wind_mph").asDouble())
                    .pressureMb(current.get("pressure_mb").asDouble())
                    .pressureIn(current.get("pressure_in").asDouble())
                    .humidity(current.get("humidity").asDouble())
                    .cloud(current.get("cloud").asDouble())
                    .build();

            log.info("Parse the data weather finished successfully. WeatherInfoDto was created");

        } catch (JsonProcessingException e) {
            log.error("JSON parsing error, JSON: {}", data, e);
        }

        return weatherInfoDto;

    }
}
