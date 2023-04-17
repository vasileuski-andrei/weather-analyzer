package com.senla.weatheranalyzer.mapper;

import com.senla.weatheranalyzer.dto.WeatherAverageInfoDto;
import com.senla.weatheranalyzer.dto.WeatherInfoDto;
import com.senla.weatheranalyzer.model.WeatherInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class WeatherInfoMapper {


    public WeatherInfoDto mapToWeatherInfoDto(WeatherInfo weatherInfo) {
        return WeatherInfoDto.builder()
                .id(weatherInfo.getId())
                .region(weatherInfo.getRegion())
                .tempC(weatherInfo.getTempC())
                .windMph(weatherInfo.getWindMph())
                .pressureMb(weatherInfo.getPressureMb())
                .humidity(weatherInfo.getHumidity())
                .cloud(weatherInfo.getCloud())
                .localTime(weatherInfo.getLocalTime())
                .build();
    }

    public WeatherAverageInfoDto convertFromWeatherInfoToWeatherAverageDto(WeatherInfo weatherInfo) {

        return WeatherAverageInfoDto.builder()
                .region(weatherInfo.getRegion())
                .country(weatherInfo.getCountry())
                .localDate(weatherInfo.getLocalTime().toLocalDate())
                .averageTempC(weatherInfo.getTempC().intValue())
                .averageTempF(weatherInfo.getTempF().intValue())
                .averageWindMph(weatherInfo.getWindMph())
                .averagePressureMb(weatherInfo.getPressureMb())
                .averageHumidity(weatherInfo.getHumidity().intValue())
                .averageCloud(weatherInfo.getCloud().intValue())
                .rowAmount(1)
                .build();

    }
}
