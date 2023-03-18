package com.senla.weatheranalyzer.service;

import com.senla.weatheranalyzer.dto.WeatherInfoDto;
import com.senla.weatheranalyzer.model.WeatherInfo;
import com.senla.weatheranalyzer.repository.WeatherRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doReturn;


@SpringBootTest
class WeatherServiceTest {

    @InjectMocks
    private WeatherService weatherService;

    @Mock
    private WeatherRepository weatherRepository;

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

        assertThat(actual).isNotEmpty();
    }

}