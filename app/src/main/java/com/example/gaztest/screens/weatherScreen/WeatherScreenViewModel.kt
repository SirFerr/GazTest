package com.example.gaztest.screens.weatherScreen

import androidx.lifecycle.ViewModel
import com.example.gaztest.data.city.City
import com.example.gaztest.data.weather.Weather
import com.example.gaztest.data.weather.WeatherApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherScreenViewModel @Inject constructor(
    private val weatherApi: WeatherApi
) : ViewModel() {

    private val _weatherState = MutableStateFlow<WeatherState>(WeatherState.Loading)
    val weatherState: StateFlow<WeatherState> = _weatherState.asStateFlow()

    fun loadWeather(lat: String, lon: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = weatherApi.getCurrentWeather(lat, lon)
                if (response.isSuccessful) {
                    val weather = response.body()!!
                    _weatherState.value = WeatherState.Success(weather)
                } else {
                    _weatherState.value =
                        WeatherState.Error("Failed to load weather: ${response.message()}")
                }
            } catch (e: Exception) {
                _weatherState.value =
                    WeatherState.Error("Exception occurred: ${e.localizedMessage}")
            }
        }
    }


}

sealed class WeatherState {
    object Loading : WeatherState()
    data class Success(val weather: Weather) : WeatherState()
    data class Error(val message: String) : WeatherState()
}