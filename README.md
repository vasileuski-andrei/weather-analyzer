# weather-analyzer

Weather analyzer

Технологии: Java 17, Spring Boot 2.7.9, Gradle, PostgreSQL, Docker, Liquibase, JUnit, Mockito, Swagger, Lombok, Testcontainers, Slf4j, Modelmapper.
Unit and integration tests in testcontainers

Weather analyzer - REST приложение Weather analyzer c периодичностью запрашивает погоду со стороннего API по определенному городу и сохраняет информация в БД.

Endpoints:

GET /api/v1/weather - возвращает информацию из БД о текущей погоде(температура, скорость ветра и т.д.)
POST /api/v1/weather - возвращает рассчитанную за указанный период информацию о среднесуточной температуре

Run app:
1. Запуск БД: docker-compose -f docker-compose.yml up -d
2. Запуск приложения

Пример:

{
"from": "18-03-2023",
"to": "19-03-2023"
}

Response
{
"region": "Minsk",
"country": "Belarus",
"localDateTime": "2023-03-18",
"average_temp_c": 0,
"average_wind_mph": 9.68,
"average_pressure_mb": 1026.78,
"row_amount": 9
},
{
"region": "Minsk",
"country": "Belarus",
"localDateTime": "2023-03-19",
"average_temp_c": 2,
"average_wind_mph": 9.27,
"average_pressure_mb": 1023.21,
"row_amount": 19
}

