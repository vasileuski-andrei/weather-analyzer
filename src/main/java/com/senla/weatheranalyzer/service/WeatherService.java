package com.senla.weatheranalyzer.service;

import com.senla.weatheranalyzer.dto.WeatherAverageInfoDto;
import com.senla.weatheranalyzer.dto.WeatherInfoDto;
import com.senla.weatheranalyzer.model.WeatherInfo;
import com.senla.weatheranalyzer.parser.ParserWeatherRapid;
import com.senla.weatheranalyzer.repository.WeatherRepository;
import com.senla.weatheranalyzer.util.DateTimeUtil;
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
    @Scheduled(cron = "${scheduling.job.cron}")
    public void getWeatherInfoFromApiAtRegularIntervals() {
        WeatherInfoDto weatherInfoDto = parserWeatherRapid.parse();
        if (weatherInfoDto != null) {
            save(weatherInfoDto);
        }
    }

    public Long save(WeatherInfoDto weatherInfoDto) {
        WeatherInfo weatherInfo = convertToWeatherInfo(weatherInfoDto);
        WeatherInfo savedWeatherInfo = null;

        try {
            savedWeatherInfo = weatherRepository.save(weatherInfo);
            log.info("The weather info was saved in database");

        } catch (IllegalArgumentException e) {
            log.error("Failed to save data in database " + e);
        }

        assert savedWeatherInfo != null;

        return savedWeatherInfo.getId();
    }

    public WeatherInfoDto getCurrentWeatherInfo() {
        getWeatherInfoFromApiAtRegularIntervals();
        log.info("The last weather info got successfully from API");

        WeatherInfo weatherInfo = weatherRepository.findLastWeatherInfo();

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

    public List<WeatherAverageInfoDto> getWeatherAverageInfoBetween(WeatherInfoDto weatherInfoDto) {
        var listWeatherInfoBetween = getWeatherInfoBetween(weatherInfoDto);

        return calculateAverageValues(listWeatherInfoBetween);

    }

    public List<WeatherInfo> getWeatherInfoBetween(WeatherInfoDto weatherInfoDto) {
        var from = DateTimeUtil.getMillisecondsOfStartDay(weatherInfoDto.getFrom());
        var to = DateTimeUtil.getMillisecondsOfEndDay(weatherInfoDto.getTo());
        var listWeatherInfo = weatherRepository.findAllWeatherInfoBetween(from, to);
        log.info("Weather info from {} to {} got successfully. Result: {}", from, to, listWeatherInfo.size());

        return listWeatherInfo;
    }

    public List<WeatherAverageInfoDto> calculateAverageValues(List<WeatherInfo> listWeatherInfo) {
        List<WeatherAverageInfoDto> averageInfoDtoList = new ArrayList<>();
        var millisecondsOfEndDay = 0L;
        int i = 0;
        int j = 0;

        while (i < listWeatherInfo.size()-1) {
            var firstMeasurementsOfDay = listWeatherInfo.get(i);
            var localTime = firstMeasurementsOfDay.getLocalTime();
            millisecondsOfEndDay = DateTimeUtil.getMillisecondsEndOfDayFromLocalDateTime(localTime);

            int sumTempC = 0;
            int sumTempF = 0;
            double sumWindMph = 0;
            double sumPressureMb = 0;
            int sumHumidity = 0;
            int sumCloud = 0;

            int elementCounter = 0;

            for (j = i; j < listWeatherInfo.size(); j++) {
                var currentElement = listWeatherInfo.get(j);

                if (currentElement.getLocaltimeEpoch() < millisecondsOfEndDay) {

                    sumTempC += currentElement.getTempC();
                    sumTempF += currentElement.getTempF();
                    sumWindMph += currentElement.getWindMph();
                    sumPressureMb += currentElement.getPressureMb();
                    sumHumidity += currentElement.getHumidity();
                    sumCloud += currentElement.getCloud();

                    elementCounter++;

                } else {
                    break;
                }
            }

            if (elementCounter != 0) {
                var weatherAverageInfoDto = WeatherAverageInfoDto.builder()
                        .region(firstMeasurementsOfDay.getRegion())
                        .country(firstMeasurementsOfDay.getCountry())
                        .localDateTime(localTime.toLocalDate())
                        .averageTempC(sumTempC / elementCounter)
                        .averageTempF(sumTempF / elementCounter)
                        .averageWindMph(round(sumWindMph / elementCounter))
                        .averagePressureMb(round(sumPressureMb / elementCounter))
                        .averageHumidity(sumHumidity / elementCounter)
                        .averageCloud(sumCloud / elementCounter)
                        .rowAmount(elementCounter)
                        .build();

                averageInfoDtoList.add(weatherAverageInfoDto);
            }

            i = j;

            log.info("Calculation of average weather values was successful");

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
