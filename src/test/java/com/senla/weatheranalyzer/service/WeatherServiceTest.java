package com.senla.weatheranalyzer.service;

import com.senla.weatheranalyzer.dto.WeatherInfoDto;
import com.senla.weatheranalyzer.TestBase;
import com.senla.weatheranalyzer.model.WeatherInfo;
import com.senla.weatheranalyzer.parser.ParserWeatherRapid;
import com.senla.weatheranalyzer.repository.WeatherRepository;
import com.senla.weatheranalyzer.util.DateTimeUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class WeatherServiceTest extends TestBase {

    @InjectMocks
    private WeatherService weatherService;

    @Mock
    private WeatherRepository weatherRepository;
    @Mock
    private ParserWeatherRapid parserWeatherRapid;

    private static WeatherInfo weatherInfo;

    @BeforeAll
    public static void init() {
        weatherInfo = WeatherInfo.builder()
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
    void getWeatherInfoBetweenTest() {
        List<WeatherInfo> listWeatherInfo = new ArrayList<>();
        listWeatherInfo.add(new WeatherInfo());
        var weatherInfoDto = WeatherInfoDto.builder()
                .from("12-03-2023")
                .to("13-03-2023")
                .build();
        doReturn(listWeatherInfo).when(weatherRepository).findAllWeatherInfoBetween(anyLong(), anyLong());

        var actual = weatherService.getWeatherInfoBetween(weatherInfoDto);

        assertThat(actual).hasSize(1);
        verify(weatherRepository).findAllWeatherInfoBetween(anyLong(), anyLong());
    }

    @Test
    void calculateAverageValuesTest() {
        List<WeatherInfo> listWeatherInfo = new ArrayList<>();
        listWeatherInfo.add(weatherInfo);
        listWeatherInfo.add(weatherInfo);

        var actual = weatherService.calculateAverageValues(listWeatherInfo);

        assertThat(actual).hasSize(1);
    }

}