package com.senla.weatheranalyzer.integration.controller;

import com.senla.weatheranalyzer.TestBase;
import com.senla.weatheranalyzer.integration.annotation.IT;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IT
@AutoConfigureMockMvc
@RequiredArgsConstructor
public class WeatherControllerTest extends TestBase {

    private final MockMvc mockMvc;
    public static final String URL = "/api/v1/weather";

    @Test
    void getCurrentWeatherInfoTest() throws Exception {

        mockMvc.perform(get(URL))
                .andExpect(status().isOk());

    }

    @Test
    public void getAverageWeatherInfoBetweenTwoDatesTest() throws Exception {

        String date = "{ \"from\": \"18-03-2023\", \"to\": \"19-03-2023\" }";

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(date)
                        .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andReturn();

    }

}
