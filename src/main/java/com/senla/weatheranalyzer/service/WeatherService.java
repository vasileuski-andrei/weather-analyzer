package com.senla.weatheranalyzer.service;

import com.senla.weatheranalyzer.dto.WeatherAverageInfoDto;
import com.senla.weatheranalyzer.dto.WeatherInfoDto;
import com.senla.weatheranalyzer.model.WeatherInfo;
import com.senla.weatheranalyzer.parser.ParserWeatherRapid;
import com.senla.weatheranalyzer.repository.WeatherRepository;
import com.senla.weatheranalyzer.util.CommonUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

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

        log.info("The weather info was saved in database");
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

    public WeatherAverageInfoDto getWeatherAverageInfoBetween(WeatherInfoDto weatherInfoDto) {
        var listWeatherInfoBetween = getWeatherInfoBetween(weatherInfoDto);

        return !listWeatherInfoBetween.isEmpty() ? calculateAverageValues(listWeatherInfoBetween) : new WeatherAverageInfoDto();

    }

    public List<WeatherInfo> getWeatherInfoBetween(WeatherInfoDto weatherInfoDto) {
        var from = CommonUtil.convertTimeToMilliseconds(weatherInfoDto.getFrom());
        var to = CommonUtil.convertTimeToMilliseconds(weatherInfoDto.getTo());
        var listWeatherInfo = weatherRepository.findAllWeatherInfoBetween(from, to);
        log.info("Weather info from {} to {} got successfully. Result: {}", from, to, listWeatherInfo.size());

        return listWeatherInfo;
    }

    public WeatherAverageInfoDto calculateAverageValues(List<WeatherInfo> listWeatherInfo) {
        WeatherAverageInfoDto weatherAverageInfoDto = null;

        try {
            var firstElement = listWeatherInfo.get(0);

            weatherAverageInfoDto = WeatherAverageInfoDto.builder()
                    .region(firstElement.getRegion())
                    .country(firstElement.getCountry())
                    .averageTempC(round(listWeatherInfo.stream().mapToDouble(WeatherInfo::getTempC).average().getAsDouble()))
                    .averageTempF(round(listWeatherInfo.stream().mapToDouble(WeatherInfo::getTempF).average().getAsDouble()))
                    .averageWindMph(round(listWeatherInfo.stream().mapToDouble(WeatherInfo::getWindMph).average().getAsDouble()))
                    .averagePressureMb(round(listWeatherInfo.stream().mapToDouble(WeatherInfo::getPressureMb).average().getAsDouble()))
                    .averageHumidity(round(listWeatherInfo.stream().mapToDouble(WeatherInfo::getHumidity).average().getAsDouble()))
                    .averageCloud(round(listWeatherInfo.stream().mapToDouble(WeatherInfo::getCloud).average().getAsDouble()))
                    .build();

        } catch (RuntimeException e) {
            log.warn("Some weather info isn't present " + e);
        }

        return weatherAverageInfoDto;
    }

    private double round(double number) {
        return Math.round(number * 100.0) / 100.0;
    }

    private WeatherInfo convertToWeatherInfo(WeatherInfoDto userDto) {
        return modelMapper.map(userDto, WeatherInfo.class);
    }

    private WeatherInfoDto convertFromWeatherInfo(WeatherInfo weatherInfo) {
        return modelMapper.map(weatherInfo, WeatherInfoDto.class);
    }

}
