package com.senla.weatheranalyzer.service;

public interface CommonService<T, V> {
    V save(T value);
}
