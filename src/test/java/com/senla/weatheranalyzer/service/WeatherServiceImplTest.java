package com.senla.weatheranalyzer.service;

import com.senla.weatheranalyzer.dto.WeatherInfoDto;
import com.senla.weatheranalyzer.TestBase;
import com.senla.weatheranalyzer.model.WeatherInfo;
import com.senla.weatheranalyzer.parser.ParserWeatherRapid;
import com.senla.weatheranalyzer.repository.WeatherRepository;
import com.senla.weatheranalyzer.service.impl.WeatherServiceImpl;
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

class WeatherServiceImplTest extends TestBase {

    public static final int EXPECTED_SIZE = 1;
    private static final String FROM_DATE = "12-03-2023";
    private static final String TO_DATE = "13-03-2023";

    @InjectMocks
    private WeatherServiceImpl weatherServiceImpl;

    @Mock
    private WeatherRepository weatherRepository;
    @Mock
    private ParserWeatherRapid parserWeatherRapid;

    private static WeatherInfo weatherInfo;
    private static WeatherInfoDto weatherInfoDto;

    @BeforeAll
    public static void init() {
        weatherInfo = WeatherInfo.builder()
                .id(2L)
                .localTime(LocalDateTime.now())
                .tempC(4.0)
                .tempF(39.2)
                .windMph(11.9)
                .pressureMb(1022.0)
                .humidity(65.0)
                .cloud(0.0)
                .localtimeEpoch(DateTimeUtil.getMillisecondsFromLocalDateTime(LocalDateTime.now()))
                .build();

        weatherInfoDto = WeatherInfoDto.builder()
                .from(FROM_DATE)
                .to(TO_DATE)
                .build();
    }

    @Test
    void getWeatherInfoBetweenTest() {

        List<WeatherInfo> listWeatherInfo = new ArrayList<>();
        listWeatherInfo.add(new WeatherInfo());
        doReturn(listWeatherInfo).when(weatherRepository).findAllWeatherInfoBetween(anyLong(), anyLong());

        var actual = weatherServiceImpl.getWeatherInfoBetween(weatherInfoDto);

        assertThat(actual).hasSize(EXPECTED_SIZE);
        verify(weatherRepository).findAllWeatherInfoBetween(anyLong(), anyLong());

    }

    @Test
    void calculateAverageInfoTest() {

        List<WeatherInfo> listWeatherInfo = new ArrayList<>();
        listWeatherInfo.add(weatherInfo);
        listWeatherInfo.add(weatherInfo);

        var actual = weatherServiceImpl.calculateAverageValues(listWeatherInfo);

        assertThat(actual).hasSize(EXPECTED_SIZE);

    }

}