package com.senla.weatheranalyzer.parser;

import com.senla.weatheranalyzer.TestBase;
import com.senla.weatheranalyzer.dto.WeatherInfoDto;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
class ParserWeatherRapidTest extends TestBase {

    private final Parser parserWeatherRapid;

    @Test
    void parseTest() throws IOException {

        var expected = WeatherInfoDto.builder()
                .name("Minsk")
                .region("Minsk")
                .country("Belarus")
                .localtimeEpoch(1681296936L)
                .localTime(LocalDateTime.parse("2023-04-12 13:55", DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm")))
                .tempC(15.0)
                .tempF(59.0)
                .windMph(19.2)
                .pressureMb(1010.0)
                .pressureIn(29.83)
                .humidity(29.0)
                .cloud(0.0)
                .build();

        File file = new File("src/test/resources/weather-info-json.txt");
        var weatherInfoJson = Files.readString(Paths.get(file.getAbsolutePath()), StandardCharsets.UTF_8);
        var actual = parserWeatherRapid.parse(weatherInfoJson);

        assertThat(actual).isEqualTo(expected);

    }

}