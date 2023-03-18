package com.senla.weatheranalyzer.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
@Slf4j
public class WeatherApiService {

    @Value("${data.api.uri}")
    private String uri;

    @Value("${data.api.headerApiKey}")
    private String headerApiKey;

    @Value("${data.api.headerApiHost}")
    private String headerApiHost;

    @Value("${data.api.method}")
    private String method;

    @Value("${data.api.city}")
    private String city;


    public String getDataFromApi() {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri + "?q=" + city))
                .header("X-RapidAPI-Key", headerApiKey)
                .header("X-RapidAPI-Host", headerApiHost)
                .method(method, HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            log.error("Failed get data from API: {}", uri, e);
        }

        log.info("The data got successfully from API: {}", uri + " " + response.body());
        return response.body();
    }


}
