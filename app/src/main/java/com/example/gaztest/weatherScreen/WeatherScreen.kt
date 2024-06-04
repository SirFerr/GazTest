package com.example.gaztest.weatherScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.gaztest.data.city.City
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
    if (state is WeatherState.Success)
        Column(
            Modifier
                .fillMaxSize()
                .padding(top = 40.dp, bottom = 36.dp, start = 16.dp, end = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = state.weather.main.temp.roundToInt().toString() + "°C",
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
                onClick = { viewModel.loadWeather(city.latitude, city.longitude) },
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