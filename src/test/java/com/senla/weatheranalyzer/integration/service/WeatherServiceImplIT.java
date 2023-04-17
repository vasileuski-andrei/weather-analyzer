package com.senla.weatheranalyzer.integration.service;

import com.senla.weatheranalyzer.dto.WeatherInfoDto;
import com.senla.weatheranalyzer.TestBase;
import com.senla.weatheranalyzer.integration.annotation.IT;
import com.senla.weatheranalyzer.service.impl.WeatherServiceImpl;
import com.senla.weatheranalyzer.util.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@IT
@RequiredArgsConstructor
public class WeatherServiceImplIT extends TestBase {

    private final WeatherServiceImpl weatherServiceImpl;
    private static WeatherInfoDto weatherInfoDto;

    private static final int ENTITIES_AMOUNT = 2;
    private static final int ENTITY_ID = 5;
    private static final String FROM_DATE = "18-03-2023";
    private static final String TO_DATE = "19-03-2023";

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
                .from(FROM_DATE)
                .to(TO_DATE)
                .build();
    }

    @Test
    public void getWeatherAverageInfoBetweenTest() {

        var actual = weatherServiceImpl.getWeatherAverageInfoBetween(weatherInfoDto).size();

        assertThat(actual).isEqualTo(ENTITIES_AMOUNT);

    }

    @Test
    public void getCurrentWeatherInfoTest() {

        var actual = weatherServiceImpl.getCurrentWeatherInfo().getId();

        assertThat(actual).isGreaterThan(0);

    }

    @Test
    public void saveWeatherInfoTest() {

        var actual = weatherServiceImpl.save(weatherInfoDto);

        assertThat(actual).isGreaterThan(0);

    }

}
