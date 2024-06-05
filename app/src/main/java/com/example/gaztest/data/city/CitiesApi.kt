package com.example.gaztest.data.city

import com.example.gaztest.CityConstants
import retrofit2.Response
import retrofit2.http.GET

interface CitiesApi {

    @GET(CityConstants.GET_ALL_CITIES)
    suspend fun getAllCities(): Response<List<City>>
}