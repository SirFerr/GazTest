package com.example.gaztest.screens.weatherScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.gaztest.data.city.City
import com.example.gaztest.data.weather.Weather
import com.example.gaztest.screens.citiesScreen.SpinningCircle
import com.example.gaztest.ui.theme.Action
import com.example.gaztest.ui.theme.Roboto
import kotlin.math.roundToInt

@Composable
fun WeatherScreen(
    city: City,
    navController: NavHostController,
    viewModel: WeatherScreenViewModel = hiltViewModel()
) {
    viewModel.loadWeather(city.latitude, city.longitude)
    val state = viewModel.weatherState.collectAsState().value
    when (state) {
        is WeatherState.Loading -> SpinningCircle()
        is WeatherState.Success -> ContentWeather(state.weather, city, viewModel::loadWeather)
        is WeatherState.Error -> Error(city,viewModel::loadWeather)
    }
}

@Composable
private fun ContentWeather(
    weather: Weather,
    city: City,
    onRetry: (String, String) -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(top = 40.dp, bottom = 36.dp, start = 16.dp, end = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = weather.main.temp.roundToInt().toString() + "°C",
                fontSize = 57.sp,
                lineHeight = 64.sp,
                fontFamily = Roboto,
                fontWeight = FontWeight.Normal,
                maxLines = 1
            )
            Text(
                text = city.city,
                fontSize = 32.sp,
                lineHeight = 40.sp,
                fontFamily = Roboto,
                fontWeight = FontWeight.Normal,
                maxLines = 1
            )
        }

        Button(
            onClick = { onRetry(city.latitude, city.longitude) },
            colors = ButtonDefaults.buttonColors(
                containerColor = Action,
                contentColor = Color.White
            )
        ) {
            Text(
                text = "Обновить",
                fontSize = 14.sp,
                lineHeight = 20.sp,
                maxLines = 1,
                fontFamily = Roboto,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}

@Composable
fun Error(city: City, onRetry: (String, String) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp), contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Произошла ошибка",
                fontSize = 14.sp,
                lineHeight = 20.sp,
                maxLines = 2,
                fontFamily = Roboto,
                fontWeight = FontWeight.Medium,
            )
            Spacer(modifier = Modifier.size(42.dp))
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .clip(CircleShape)
                    .clickable {}
                    .background(color = Color.Transparent, shape = CircleShape)
            ) {
                Button(
                    onClick = { onRetry(city.latitude, city.longitude) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Action,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Обновить",
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        maxLines = 1,
                        fontFamily = Roboto,
                        fontWeight = FontWeight.Medium,
                    )
                }
            }
        }
    }
}