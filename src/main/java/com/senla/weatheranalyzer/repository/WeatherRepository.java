package com.senla.weatheranalyzer.repository;

import com.senla.weatheranalyzer.model.WeatherInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherRepository extends JpaRepository<WeatherInfo, Long> {

    @Query(value = "SELECT w.id, w.region, w.temp_c, w.wind_mph, w.pressure_mb, w.humidity, w.cloud, w.local_time " +
            "FROM weather_info as w " +
            "ORDER BY w.id " +
            "DESC LIMIT 1", nativeQuery = true)
    WeatherInfo findLastWeatherInfo();


}
