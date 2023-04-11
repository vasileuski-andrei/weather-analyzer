package com.senla.weatheranalyzer.repository;

import com.senla.weatheranalyzer.model.WeatherInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WeatherRepository extends JpaRepository<WeatherInfo, Long> {

    Optional<WeatherInfo> findTopByOrderByIdDesc();

    @Query("select w from WeatherInfo w where w.localtimeEpoch between :from and :to")
    List<WeatherInfo> findAllWeatherInfoBetween(@Param("from") Long from, @Param("to")Long to);

}
