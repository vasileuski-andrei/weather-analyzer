package com.senla.weatheranalyzer.integration.controller;

import com.senla.weatheranalyzer.TestBase;
import com.senla.weatheranalyzer.integration.annotation.IT;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IT
@AutoConfigureMockMvc
@RequiredArgsConstructor
public class WeatherControllerTest extends TestBase {

    private final MockMvc mockMvc;

    @Test
    void getCurrentWeatherInfoTest() throws Exception {
        mockMvc.perform(get("/api/v1/weather"))
                .andExpect(status().is2xxSuccessful());
    }

}
