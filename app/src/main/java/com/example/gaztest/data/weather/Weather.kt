package com.example.gaztest.data.weather

data class Weather(
    val main: MainWeather
)

data class MainWeather(
    val temp: Double
)
