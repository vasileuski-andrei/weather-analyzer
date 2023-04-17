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
    //не понимаю зачем в лог пришлось перегонять вроде можно же нормально с датой работать....
    //findBySegmentDttmBetween(LocalDateTime segmentCreateDateTimeFrom, LocalDateTime segmentCreateDateTimeTo)
    // если TIMESTAMP в бд, обычно даты так хранятся по практике это более наглядно и понятно
    //не знаю я не сильно вникала что от сервера погоды приходит, но для дат в бд обычно используют TIMESTAMP/LocalDateTime в джаве
    @Query("select w from WeatherInfo w where w.localtimeEpoch between :from and :to")
    List<WeatherInfo> findAllWeatherInfoBetween(@Param("from") Long from, @Param("to")Long to);
}
