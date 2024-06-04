package com.example.gaztest.screens.citiesScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.gaztest.Destination
import com.example.gaztest.data.city.City
import com.example.gaztest.ui.theme.Roboto
import com.google.gson.Gson

@Composable
fun DisplayCityList(cities: List<City>, navController: NavHostController) {
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
                        showCharHeader = startIndexes.contains(index) && remember { derivedStateOf { listState.firstVisibleItemIndex } }.value != index,
                        commonModifier,
                        navController = navController
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
            maxLines = 1,
            fontWeight = FontWeight.Normal,
            modifier = Modifier,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun CityItem(
    city: City,
    showCharHeader: Boolean,
    modifier: Modifier,
    navController: NavHostController
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
                .wrapContentHeight()
                .clickable {
                    navController.navigate(Destination.WEATHER_SCREEN+"/${Gson().toJson(city)}")
                }
                .background(color = Color.Transparent, shape = shape)
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
                    fontFamily = Roboto,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    maxLines = 1
                )
            }
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