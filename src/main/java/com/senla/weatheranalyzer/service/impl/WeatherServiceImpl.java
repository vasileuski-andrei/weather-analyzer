package com.senla.weatheranalyzer.service.impl;

import com.senla.weatheranalyzer.dto.WeatherAverageInfoDto;
import com.senla.weatheranalyzer.dto.WeatherInfoDto;
import com.senla.weatheranalyzer.mapper.WeatherInfoMapper;
import com.senla.weatheranalyzer.model.WeatherInfo;
import com.senla.weatheranalyzer.repository.WeatherRepository;
import com.senla.weatheranalyzer.service.WeatherService;
import com.senla.weatheranalyzer.util.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeatherServiceImpl implements WeatherService {

    private final WeatherRepository weatherRepository;
    private final ModelMapper modelMapper;
    private final WeatherInfoMapper mapper;

    //не понимаю зачем лонг возвращать в сервисе метод save либо сущность как и в jpa возвращает либо void
    //не усложняй стандартные методы
    @Transactional
    public void save(WeatherInfoDto weatherInfoDto) {
        WeatherInfo weatherInfo = convertToWeatherInfo(weatherInfoDto);
        weatherRepository.save(weatherInfo);
    }

    public WeatherInfoDto getCurrentWeatherInfo() {
        // getWeatherInfoFromApiAtRegularIntervals() зачем это тут сбор идет по расписанию
        // log.info("The last weather info got successfully from API");

        WeatherInfo weatherInfo = weatherRepository.findTopByOrderByIdDesc()
                .orElseThrow(() -> new RuntimeException("Failed to get data from database"));
        return mapper.mapToWeatherInfoDto(weatherInfo);

//        WeatherInfoDto weatherInfoDto = null;
//        Optional<WeatherInfo> optionalWeatherInfo = weatherRepository.findTopByOrderByIdDesc();
//
//        if (optionalWeatherInfo.isPresent()) {
//            //не советую var использовать не наглядно тем более что мы знаем тип WeatherInfoDto как то его не жалуют и я не видела чтоб на проекте использовали
//            var weatherInfo = optionalWeatherInfo.get();
//            //вынести либо в приватный метод либо вообще в отдельный клас WeatherInfoMapper
//            weatherInfoDto = WeatherInfoDto.builder()
//                .id(weatherInfo.getId())
//                .region(weatherInfo.getRegion())
//                .tempC(weatherInfo.getTempC())
//                .windMph(weatherInfo.getWindMph())
//                .pressureMb(weatherInfo.getPressureMb())
//                .humidity(weatherInfo.getHumidity())
//                .cloud(weatherInfo.getCloud())
//                .localTime(weatherInfo.getLocalTime())
//                .build();
//
//        } else {
//            log.error("Failed to get data from database");
//            //почему убрал свое исключение?
//            throw new RuntimeException("Failed to get data from database");
//        }
//
//        return weatherInfoDto;
    }

    public List<WeatherAverageInfoDto> getWeatherAverageInfoBetween(WeatherInfoDto weatherInfoDto) {
        List<WeatherInfo> listWeatherInfoBetween = getWeatherInfoBetween(weatherInfoDto);
        if (listWeatherInfoBetween.size() == 1) {
            List<WeatherAverageInfoDto> weatherAverageInfoDtoList = new ArrayList<>();
            //почитай как ModelMapper modelMapper настраивать если не все поля совпадают я сама не сильно с ним работала
            //аппинг обычно выносят хотя вариант с приватным методом тоже иногда используют
            weatherAverageInfoDtoList.add(mapper.convertFromWeatherInfoToWeatherAverageDto(listWeatherInfoBetween.get(0)));
            return weatherAverageInfoDtoList;
//не оставляй лишних пустых строк
        }
        return calculateAverageValues(listWeatherInfoBetween);
    }

    // tckb этот метод нигде больше не используется и выносится с целью улучшения кодстайла, то делаем private
    public List<WeatherInfo> getWeatherInfoBetween(WeatherInfoDto weatherInfoDto) {
        //про var уже написала
        var from = DateTimeUtil.getMillisecondsOfStartDay(weatherInfoDto.getFrom());
        var to = DateTimeUtil.getMillisecondsOfEndDay(weatherInfoDto.getTo());
        var listWeatherInfo = weatherRepository.findAllWeatherInfoBetween(from, to);
        log.info("Weather info from {} to {} got successfully. Result: {}", from, to, listWeatherInfo.size());
        return listWeatherInfo;
    }

    public List<WeatherAverageInfoDto> calculateAverageValues(List<WeatherInfo> listWeatherInfo) {
        //что то и правда отстойно смотрится, надо наверно всетаки стримы идея использовать стримы как ты сразу делал была лучше))))
        List<WeatherAverageInfoDto> averageInfoDtoList = new ArrayList<>();
        var millisecondsOfEndDay = 0L;
        int i = 0;
        int j = 0;

        while (i < listWeatherInfo.size() - 1) {
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
                        .localDate(localTime.toLocalDate())
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

    //
    private WeatherInfo convertToWeatherInfo(WeatherInfoDto userDto) {
        return modelMapper.map(userDto, WeatherInfo.class);
    }
}
