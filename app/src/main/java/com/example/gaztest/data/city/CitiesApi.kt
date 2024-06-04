package com.example.gaztest.data.city

import retrofit2.Response
import retrofit2.http.GET

interface CitiesApi {

    @GET("SirFerr/2589c274d12e5348bca754988dc0ec9f/raw/11d65c8de9497d79e884bde2947bc4a31d0b2401/cities.json")
    suspend fun getAllCities(): Response<List<City>>
}