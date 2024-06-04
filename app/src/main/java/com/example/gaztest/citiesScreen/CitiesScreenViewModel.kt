package com.example.gaztest.citiesScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.gaztest.data.city.CitiesApi
import com.example.gaztest.data.city.City
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CitiesScreenViewModel @Inject constructor(
    private val citiesApi: CitiesApi
) : ViewModel() {

    private val _cityState = MutableStateFlow<CityState>(CityState.Loading)
    val cityState: StateFlow<CityState> = _cityState.asStateFlow()

    init {
        loadCities()
    }

    fun loadCities() {
        _cityState.value = CityState.Loading
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = citiesApi.getAllCities()
                if (response.isSuccessful) {
                    val cities = response.body()?.sortedBy { it.city } ?: listOf()
                    _cityState.value = CityState.Success(cities)
                } else {
                    _cityState.value = CityState.Error("Failed to load cities: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("CitiesScreenVM", "Failed to load cities", e)
                _cityState.value = CityState.Error("Exception occurred: ${e.localizedMessage}")
            }
        }
    }
}

sealed class CityState {
    object Loading : CityState()
    data class Success(val cities: List<City>) : CityState()
    data class Error(val message: String) : CityState()
}
