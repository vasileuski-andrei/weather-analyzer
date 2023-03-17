package com.senla.weatheranalyzer.service;

import com.senla.weatheranalyzer.dto.WeatherInfoDto;
import com.senla.weatheranalyzer.model.WeatherInfo;
import com.senla.weatheranalyzer.parser.ParserWeatherRapid;
import com.senla.weatheranalyzer.repository.WeatherRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class WeatherService implements CommonService<WeatherInfoDto, Long> {

    private final ParserWeatherRapid parserWeatherRapid;
    private final WeatherRepository weatherRepository;
    private final ModelMapper modelMapper;

    @Async
    @Scheduled(fixedRate = 60000)
    public void getWeatherInfoFromApiAtRegularIntervals() {
        WeatherInfoDto weatherInfoDto = parserWeatherRapid.parse();
        save(weatherInfoDto);
    }

    public void save(WeatherInfoDto weatherInfoDto) {
        WeatherInfo client = convertToWeatherInfo(weatherInfoDto);
        weatherRepository.save(client);
    }

    public WeatherInfoDto getCurrentWeatherInfo() {
        getWeatherInfoFromApiAtRegularIntervals();
        log.info("The last weather info got successfully from API");
        WeatherInfo weatherInfo = weatherRepository.findLastWeatherInfo();

        return WeatherInfoDto.builder()
                .region(weatherInfo.getRegion())
                .tempC(weatherInfo.getTempC())
                .windMph(weatherInfo.getWindMph())
                .pressureMb(weatherInfo.getPressureMb())
                .humidity(weatherInfo.getHumidity())
                .cloud(weatherInfo.getCloud())
                .localTime(weatherInfo.getLocalTime())
                .build();
    }

    private WeatherInfo convertToWeatherInfo(WeatherInfoDto userDto) {
        return modelMapper.map(userDto, WeatherInfo.class);
    }

    private WeatherInfoDto convertFromWeatherInfo(WeatherInfo weatherInfo) {
        return modelMapper.map(weatherInfo, WeatherInfoDto.class);
    }

}
