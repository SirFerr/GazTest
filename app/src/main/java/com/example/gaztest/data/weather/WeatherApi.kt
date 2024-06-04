package com.example.gaztest.data.weather

import com.example.gaztest.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String ,
        @Query("exclude") exclude: String = "minutely%2Chourly%2Cdaily%2Calerts",
        @Query("units") units: String = "metric",
        @Query("appid") apiKey: String = Constants.API_WEATHER
    ): Response<Weather>
}