@file:OptIn(ExperimentalFoundationApi::class)

package com.example.gaztest.citiesScreen

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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gaztest.data.City
import com.example.gaztest.ui.theme.Action
import com.example.gaztest.ui.theme.Roboto

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CitiesScreen(viewModel: CitiesScreenViewModel = hiltViewModel()) {
    val state = viewModel.cityState.collectAsState().value

    when (state) {
        is CityState.Loading -> SpinningCircle()
        is CityState.Success -> DisplayCityList(state.cities)
        is CityState.Error -> ErrorScreen(onRetry = viewModel::loadCities)
    }
}

@Composable
fun DisplayCityList(cities: List<City>) {
    Box(Modifier.fillMaxSize()) {
        val groupedCities = remember(cities) {
            cities.groupBy {
                it.city.firstOrNull() ?: ' '
            }
        }
        val startIndexes = remember(cities) {
            getStartIndexes(groupedCities.entries)
        }
        val endIndexes = remember(cities) {
            getEndIndexes(groupedCities.entries)
        }
        val commonModifier = Modifier.width(56.dp)

        val listState = rememberLazyListState()
        val moveStickyHeader by remember {
            derivedStateOf {
                endIndexes.contains(listState.firstVisibleItemIndex + 1)
            }
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState,
        ) {
            itemsIndexed(cities) { index, city ->
                if ((city.city.firstOrNull() ?: ' ') != ' ')
                    CityItem(
                        city,
                        showCharHeader = startIndexes.contains(index) && listState.firstVisibleItemIndex != index,
                        commonModifier
                    )
            }
        }
        LetterHeader(
            initial = cities[listState.firstVisibleItemIndex].city.firstOrNull() ?: ' ',
            modifier = commonModifier
                .padding(top = 8.dp)
                .then(
                    if (moveStickyHeader) {
                        Modifier.offset {
                            IntOffset(0, -listState.firstVisibleItemScrollOffset)
                        }
                    } else {
                        Modifier
                    }
                )
        )
    }
}

@Composable
fun LetterHeader(initial: Char, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .padding(start = 16.dp)
            .size(40.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initial.toString(),
            fontSize = 24.sp,
            fontFamily = Roboto,
            fontWeight = FontWeight.Medium,
            modifier = Modifier,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun CityItem(
    city: City,
    showCharHeader: Boolean,
    modifier: Modifier
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (showCharHeader) {
            LetterHeader(initial = city.city.firstOrNull() ?: ' ', modifier = modifier)
        } else {
            Spacer(modifier = modifier)
        }
        val shape: Shape = RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape)
                .wrapContentHeight() // Обрезка Box по форме
                .clickable {} // Применение clickable к обрезанной области
                .background(color = Color.Transparent, shape = shape) // Применение фона с обрезкой
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.size(16.dp))
                Text(
                    text = city.city,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    maxLines = 1
                )
            }
        }
    }
}


@Composable
fun ErrorScreen(onRetry: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp), contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Произошла ошибка")
            Spacer(modifier = Modifier.size(42.dp))
            Button(onClick = onRetry) {
                Text(text = "Обновить")
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

private fun getStartIndexes(entries: Set<Map.Entry<Char, List<City>>>): List<Int> {
    var acc = 0
    val list = mutableListOf<Int>()
    entries.forEach { entry ->
        list.add(acc)
        acc += entry.value.size
    }
    return list
}

private fun getEndIndexes(entries: Set<Map.Entry<Char, List<City>>>): List<Int> {
    var acc = 0
    val list = mutableListOf<Int>()
    entries.forEach { entry ->
        acc += entry.value.size
        list.add(acc)
    }
    return list
}
