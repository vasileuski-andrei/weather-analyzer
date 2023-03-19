package com.senla.weatheranalyzer.integration.service;

import com.senla.weatheranalyzer.dto.WeatherInfoDto;
import com.senla.weatheranalyzer.integration.TestBase;
import com.senla.weatheranalyzer.integration.annotation.IT;
import com.senla.weatheranalyzer.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@IT
@SpringBootTest
@RequiredArgsConstructor
public class WeatherServiceIT extends TestBase {

    private final WeatherService weatherService;

    @Test
    public void getWeatherAverageInfoBetweenTest() {

        var weatherInfo = WeatherInfoDto.builder()
                .from("18-03-2023")
                .to("19-03-2023")
                .build();

        var actual = weatherService.getWeatherAverageInfoBetween(weatherInfo).size();

        assertThat(actual).isEqualTo(2);

    }

}
