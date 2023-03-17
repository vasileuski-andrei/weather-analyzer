--liquibase formatted sql

--changeset andreiv:1
CREATE TABLE IF NOT EXISTS weather_info(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50),
    region VARCHAR(50),
    country VARCHAR(50),
    localtime_epoch VARCHAR(50),
    local_time TIMESTAMP,
    temp_c DOUBLE PRECISION,
    temp_f DOUBLE PRECISION,
    wind_mph DOUBLE PRECISION,
    pressure_mb DOUBLE PRECISION,
    pressure_in DOUBLE PRECISION,
    humidity DOUBLE PRECISION,
    cloud DOUBLE PRECISION
);
