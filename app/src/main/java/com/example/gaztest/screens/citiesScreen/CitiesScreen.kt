@file:OptIn(ExperimentalFoundationApi::class)

package com.example.gaztest.screens.citiesScreen

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.gaztest.ui.theme.Action
import com.example.gaztest.ui.theme.Roboto

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CitiesScreen(
    navController: NavHostController,
    viewModel: CitiesScreenViewModel = hiltViewModel()
) {
    when (val state = viewModel.cityState.collectAsState().value) {
        is CityState.Success -> DisplayCityList(state.cities, navController = navController)
        is CityState.Loading -> SpinningCircle()
        is CityState.Error -> Error(onRetry = viewModel::loadCities)
    }
}

@Composable
fun Error(onRetry: () -> Unit) {
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
                Button(onClick = onRetry, colors = ButtonDefaults.buttonColors(containerColor = Action, contentColor = Color.White)) {
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

@Composable
fun SpinningCircle() {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = ""
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(48.dp) // Set the size of the spinner
    ) {
        Canvas(modifier = Modifier
            .size(48.dp)
            .graphicsLayer {
                rotationZ = angle
            }
        ) {
            drawArc(
                color = Action, // Adjust the color to match your design
                startAngle = 0f,
                sweepAngle = 280f, // This determines the length of the arc
                useCenter = false,
                style = Stroke(width = 6.dp.toPx(), cap = StrokeCap.Round)
            )
        }
    }
}


