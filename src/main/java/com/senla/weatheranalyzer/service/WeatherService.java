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

import java.util.ArrayList;
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

    public List<WeatherAverageInfoDto> getWeatherAverageInfoBetween(WeatherInfoDto weatherInfoDto) {
        var listWeatherInfoBetween = getWeatherInfoBetween(weatherInfoDto);

        return calculateAverageValues(listWeatherInfoBetween, weatherInfoDto);

    }

    public List<WeatherInfo> getWeatherInfoBetween(WeatherInfoDto weatherInfoDto) {
        var from = CommonUtil.getMillisecondsOfStartDay(weatherInfoDto.getFrom());
        var to = CommonUtil.getMillisecondsOfEndDay(weatherInfoDto.getTo());
        var listWeatherInfo = weatherRepository.findAllWeatherInfoBetween(from, to);
        log.info("Weather info from {} to {} got successfully. Result: {}", from, to, listWeatherInfo.size());

        return listWeatherInfo;
    }

    public List<WeatherAverageInfoDto> calculateAverageValues(List<WeatherInfo> listWeatherInfo, WeatherInfoDto weatherInfoDto) {
        List<WeatherAverageInfoDto> averageInfoDtoList = new ArrayList<>();
        WeatherAverageInfoDto weatherAverageInfoDto = null;
        var millisecondsOfEndDay = 0L;

        for (int i = 0; i < listWeatherInfo.size(); i++) {
            var localTime = listWeatherInfo.get(i).getLocalTime();
            System.out.println(localTime);
            millisecondsOfEndDay = CommonUtil.getMillisecondsEndOfDayFromLocalDateTime(localTime);
            System.out.println("millisecondsOfEndDay " + millisecondsOfEndDay);

            int sumTempC = 0;
            int sumTempF = 0;
            double sumWindMph = 0;
            double sumPressureMb = 0;
            int sumHumidity = 0;
            int sumCloud = 0;

            int counter = 0;

            for (int j = i; j < listWeatherInfo.size(); j++) {
                var currentElement = listWeatherInfo.get(j);
                counter++;

                sumTempC += currentElement.getTempC();
                sumTempF += currentElement.getTempF();
                sumWindMph += currentElement.getWindMph();
                sumPressureMb += currentElement.getPressureMb();
                sumHumidity += currentElement.getHumidity();
                sumCloud += currentElement.getCloud();

                if (currentElement.getLocaltimeEpoch() > millisecondsOfEndDay || j == listWeatherInfo.size()-1) {

                    System.out.println("COUNTER " + counter);
                    weatherAverageInfoDto = WeatherAverageInfoDto.builder()
                            .region(currentElement.getRegion())
                            .country(currentElement.getCountry())
                            .localDateTime(currentElement.getLocalTime())
                            .averageTempC(sumTempC / counter)
                            .averageTempF(sumTempF / counter)
                            .averageWindMph(round(sumWindMph / counter))
                            .averagePressureMb(round(sumPressureMb / counter))
                            .averageHumidity(sumHumidity / counter)
                            .averageCloud(sumCloud / counter)
                            .build();

                    averageInfoDtoList.add(weatherAverageInfoDto);
                    i = j;
                    break;
                }
            }
        }

        return averageInfoDtoList;
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
