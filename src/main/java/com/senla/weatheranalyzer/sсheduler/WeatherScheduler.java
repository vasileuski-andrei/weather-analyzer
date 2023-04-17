package com.senla.weatheranalyzer.sсheduler;
//не забываем что названия классов говорящие и по ним др разрабам проще читать и понимать код
//поэтому когда разраб видет пакет sheduler он сразу понимает что туда вынесен функцилнал раб по расписанию

import com.senla.weatheranalyzer.parser.Parser;
import com.senla.weatheranalyzer.requester.Requester;
import com.senla.weatheranalyzer.service.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WeatherScheduler {
//через интерфейс инженким зависимости
    private final Requester weatherRequester;
    private final Parser parserWeatherRapid;
    private final WeatherService weatherService;

    @Scheduled(cron = "${scheduling.job.cron}")
    public void getWeatherInfoFromApiAtRegularIntervals() {
        var dataFromApi = weatherRequester.getDataFromApi();
        var weatherInfoDto = parserWeatherRapid.parse(dataFromApi);
        if (weatherInfoDto != null) {
           weatherService.save(weatherInfoDto);
        }
    }
}
