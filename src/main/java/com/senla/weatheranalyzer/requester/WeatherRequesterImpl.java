package com.senla.weatheranalyzer.requester;

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
public class WeatherRequesterImpl implements Requester {

    private final String uri;
    private final String headerApiKey;
    private final String headerApiHost;
    private final String method;
    private final String city;

    public WeatherRequesterImpl(@Value("${data.api.uri}") String uri,
                                @Value("${data.api.headerApiKey}") String headerApiKey,
                                @Value("${data.api.headerApiHost}") String headerApiHost,
                                @Value("${data.api.method}") String method,
                                @Value("${data.api.city}") String city) {
        this.uri = uri;
        this.headerApiKey = headerApiKey;
        this.headerApiHost = headerApiHost;
        this.method = method;
        this.city = city;
    }

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
