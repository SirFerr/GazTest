package com.example.gaztest

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gaztest.citiesScreen.CitiesScreen
import com.example.gaztest.data.city.City
import com.example.gaztest.weatherScreen.WeatherScreen
import com.google.gson.Gson

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Destination.CITY_SCREEN) {
        composable(Destination.CITY_SCREEN) { CitiesScreen(navController = navController) }
        composable(Destination.WEATHER_SCREEN + "/{city}", arguments = listOf(navArgument("city") {
            type = NavType.StringType
        })) {
            val cityJson = it.arguments?.getString("city")
            val city = Gson().fromJson(cityJson, City::class.java)
            WeatherScreen(city = city, navController = navController)
        }
    }

}