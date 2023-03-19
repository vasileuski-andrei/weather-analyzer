package com.senla.weatheranalyzer.integration.service;

import com.senla.weatheranalyzer.dto.WeatherInfoDto;
import com.senla.weatheranalyzer.integration.TestBase;
import com.senla.weatheranalyzer.integration.annotation.IT;
import com.senla.weatheranalyzer.service.WeatherService;
import com.senla.weatheranalyzer.util.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@IT
@SpringBootTest
@RequiredArgsConstructor
public class WeatherServiceIT extends TestBase {

    private final WeatherService weatherService;
    private static WeatherInfoDto weatherInfoDto;

    private static final int ENTITIES_AMOUNT = 2;
    private static final int ENTITIE_ID = 5;

    @BeforeAll
    public static void init() {
        weatherInfoDto = WeatherInfoDto.builder()
                .localTime(LocalDateTime.now())
                .tempC(4.0)
                .tempF(39.2)
                .windMph(11.9)
                .pressureMb(1022.0)
                .humidity(65.0)
                .cloud(0.0)
                .localtimeEpoch(DateTimeUtil.getMillisecondsFromLocalDateTime(LocalDateTime.now()))
                .build();
    }

    @Test
    public void saveWeatherInfoTest() {
        var actual = weatherService.save(weatherInfoDto);

        assertThat(actual).isEqualTo(ENTITIE_ID);
    }

    @Test
    public void getWeatherAverageInfoBetweenTest() {

        var weatherInfo = WeatherInfoDto.builder()
                .from("18-03-2023")
                .to("19-03-2023")
                .build();

        var actual = weatherService.getWeatherAverageInfoBetween(weatherInfo).size();

        assertThat(actual).isEqualTo(ENTITIES_AMOUNT);

    }

}
