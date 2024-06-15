package com.example.aplication.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aplication.R
import com.example.aplication.ViewModels.GeoData
import com.example.aplication.ViewModels.MainViewModel
import com.example.aplication.logicExecution.kilometersToMiles
import com.example.aplication.logicExecution.metersToMilesOrKilometers
import com.example.aplication.logicExecution.millimetersToInches
import com.example.aplication.logicExecution.settingsProvider
import com.example.aplication.screens.Items.ColumnItem
import com.example.aplication.screens.Items.RowItem

val mainFont = FontFamily(Font(R.font.arial))

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreen(
    paddingValues: PaddingValues,
    geoData: GeoData? = null,
    context: Context
) {
    val viewModel = viewModel<MainViewModel>()
    val refreshing by viewModel.isloading.collectAsState()

    val pullRefreshState = rememberPullRefreshState(refreshing, { viewModel.reLoad(geoData) })

    val mainList = viewModel.mainList
    val hoursList = viewModel.hoursList
    val daysList = viewModel.daysList

    val itemColor = colorResource(mainList.value.itemsColor)
    val backgroundColor = colorResource(mainList.value.backgroundColor)

    LaunchedEffect(geoData) {
        viewModel.reLoad(geoData)
    }


    if (refreshing) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = Color(0xFF0289FE), // Сделаем его синим
            )
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
//            .fillMaxHeight(0.92f)
                    .background(color = backgroundColor)
                    .padding(paddingValues = paddingValues)
                    .pullRefresh(pullRefreshState)
            )
            {
                item {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        PullRefreshIndicator(
                            refreshing,
                            pullRefreshState,
                            Modifier.align(Alignment.CenterHorizontally)
                        )
                        Text(
                            text = mainList.value.city,
                            style = TextStyle(
                                fontSize = 34.sp,
                                fontWeight = FontWeight(400),
                                color = Color(0xFFFFFFFF),
                                textAlign = TextAlign.Center,
                            ),
                            fontFamily = mainFont
                        )
                        Text(
                            text = "${settingsProvider(mainList.value.temp, context)}°",
                            style = TextStyle(
                                fontSize = 80.sp,
                                fontWeight = FontWeight(700),
                                color = Color(0xFFFFFFFF),
                            ),
                            fontFamily = mainFont

                        )
                        Text(
                            text = mainList.value.weatherDesc,
                            style = TextStyle(
                                fontSize = 20.sp,
                                lineHeight = 24.sp,
                                fontWeight = FontWeight(400),
                                color = Color(0x99EBEBF5),
                            ),
                            fontFamily = mainFont
                        )
                        Row(
                            Modifier.width(220.dp),
                            horizontalArrangement = Arrangement.SpaceAround,
                        ) {
                            Text(
                                "min: ${settingsProvider(mainList.value.minTemp, context)}°",
                                style = TextStyle(
                                    fontSize = 20.sp,
                                    lineHeight = 24.sp,
                                    fontWeight = FontWeight(400),
                                    color = Color(0xffffffff),
                                ),
                                fontFamily = mainFont
                            )
                            Text(
                                "max: ${settingsProvider(mainList.value.maxTemp, context)}°",
                                style = TextStyle(
                                    fontSize = 20.sp,
                                    lineHeight = 24.sp,
                                    fontWeight = FontWeight(400),
                                    color = Color(0xffffffff),
                                ),
                                fontFamily = mainFont
                            )
                        }
                    }

                    Spacer(modifier = Modifier.padding(vertical = 7.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.Start),
                        verticalAlignment = Alignment.Top,
                        modifier = Modifier.padding(horizontal = 15.dp)
                    ) {
                        itemsIndexed(
                            hoursList.value
                        ) { _, item ->
                            RowItem(item, itemColor, context)
                        }
                    }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(570.dp)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        itemsIndexed(
                            daysList.value
                        ) { _, item ->
                            ColumnItem(item, itemColor, context)
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Column(
                                modifier = Modifier
                                    .border(
                                        width = 1.dp,
                                        color = Color(0xFFFFFFFF),
                                        shape = RoundedCornerShape(size = 22.dp)
                                    )
                                    .width(164.dp)
                                    .height(164.dp)
                                    .background(
                                        color = itemColor,
                                        shape = RoundedCornerShape(size = 22.dp)
                                    )
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.humidity),
                                    style = TextStyle(
                                        fontSize = 16.sp,
                                        fontFamily = mainFont,
                                        fontWeight = FontWeight(400),
                                        color = Color(0x7AFFFFFF),
                                    )
                                )
                                Text(
                                    text = "${mainList.value.humidity}%",
                                    style = TextStyle(
                                        fontSize = 32.sp,
                                        fontFamily = mainFont,
                                        fontWeight = FontWeight(400),
                                        color = Color(0xFFFFFFFF),
                                    )
                                )
                            }
                            Column(
                                modifier = Modifier
                                    .border(
                                        width = 1.dp,
                                        color = Color(0xFFFFFFFF),
                                        shape = RoundedCornerShape(size = 22.dp)
                                    )
                                    .width(164.dp)
                                    .height(164.dp)
                                    .background(
                                        color = itemColor,
                                        shape = RoundedCornerShape(size = 22.dp)
                                    )
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.feels_like),
                                    style = TextStyle(
                                        fontSize = 16.sp,
                                        fontFamily = mainFont,
                                        fontWeight = FontWeight(400),
                                        color = Color(0x7AFFFFFF),
                                    )
                                )
                                Text(
                                    text = "${
                                        settingsProvider(
                                            mainList.value.feelsLike,
                                            context
                                        )
                                    }°",
                                    style = TextStyle(
                                        fontSize = 32.sp,
                                        fontFamily = mainFont,
                                        fontWeight = FontWeight(400),
                                        color = Color(0xFFFFFFFF),
                                    )
                                )
                            }
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            LazyColumn(
                                modifier = Modifier
                                    .border(
                                        width = 1.dp,
                                        color = Color(0xFFFFFFFF),
                                        shape = RoundedCornerShape(size = 22.dp)
                                    )
                                    .width(164.dp)
                                    .height(164.dp)
                                    .background(
                                        color = itemColor,
                                        shape = RoundedCornerShape(size = 22.dp)
                                    )
                                    .padding(16.dp)
                            ) {
                                item {
                                    Text(
                                        text = stringResource(R.string.visibility),
                                        style = TextStyle(
                                            fontSize = 16.sp,
                                            fontFamily = FontFamily(Font(R.font.arial)),
                                            fontWeight = FontWeight(400),
                                            color = Color(0x7AFFFFFF),
                                        )
                                    )
                                    Text(
                                        text = metersToMilesOrKilometers(
                                            mainList.value.visibility,
                                            context
                                        ),
                                        style = TextStyle(
                                            fontSize = 32.sp,
                                            fontFamily = mainFont,
                                            fontWeight = FontWeight(400),
                                            color = Color(0xFFFFFFFF),
                                        ),
                                        maxLines = 1,
                                    )
                                    Spacer(modifier = Modifier.padding(vertical = 15.dp))
                                    Text(
                                        text = stringResource(
                                            R.string.dew_point,
                                            settingsProvider(mainList.value.dewPoint, context)
                                        ),
                                        style = TextStyle(
                                            fontSize = 14.sp,
                                            fontFamily = mainFont
                                        ),
                                        fontWeight = FontWeight(400),
                                        color = Color(0xFFFFFFFF),
                                    )
                                }
                            }
                            LazyColumn(
                                modifier = Modifier
                                    .border(
                                        width = 1.dp,
                                        color = Color(0xFFFFFFFF),
                                        shape = RoundedCornerShape(size = 22.dp)
                                    )
                                    .width(164.dp)
                                    .height(164.dp)
                                    .background(
                                        color = itemColor,
                                        shape = RoundedCornerShape(size = 22.dp)
                                    )
                                    .padding(16.dp)
                            ) {
                                item {
                                    Text(
                                        text = stringResource(R.string.sunrise),
                                        style = TextStyle(
                                            fontSize = 16.sp,
                                            fontFamily = FontFamily(Font(R.font.arial)),
                                            fontWeight = FontWeight(400),
                                            color = Color(0x7AFFFFFF),
                                        )
                                    )
                                    Text(
                                        text = mainList.value.sunrise,
                                        style = TextStyle(
                                            fontSize = 25.sp,
                                            fontFamily = mainFont,
                                            fontWeight = FontWeight(400),
                                            color = Color(0xFFFFFFFF),
                                        )
                                    )
                                    Text(
                                        text = stringResource(R.string.sunset),
                                        style = TextStyle(
                                            fontSize = 16.sp,
                                            fontFamily = FontFamily(Font(R.font.arial)),
                                            fontWeight = FontWeight(400),
                                            color = Color(0x7AFFFFFF),
                                        )
                                    )
                                    Text(
                                        text = mainList.value.sunset,
                                        style = TextStyle(
                                            fontSize = 25.sp,
                                            fontFamily = mainFont,
                                            fontWeight = FontWeight(400),
                                            color = Color(0xFFFFFFFF),
                                        )
                                    )
                                }
                            }
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Column(
                                modifier = Modifier
                                    .border(
                                        width = 1.dp,
                                        color = Color(0xFFFFFFFF),
                                        shape = RoundedCornerShape(size = 22.dp)
                                    )
                                    .width(164.dp)
                                    .height(164.dp)
                                    .background(
                                        color = itemColor,
                                        shape = RoundedCornerShape(size = 22.dp)
                                    )
                                    .padding(16.dp)

                            ) {
                                Text(
                                    text = stringResource(R.string.precipitation),
                                    style = TextStyle(
                                        fontSize = 16.sp,
                                        fontFamily = mainFont,
                                        fontWeight = FontWeight(400),
                                        color = Color(0x7AFFFFFF),
                                    )
                                )
                                LazyRow {
                                    item {
                                        Text(
                                            text = millimetersToInches(
                                                mainList.value.precipitation,
                                                context
                                            ),
                                            style = TextStyle(
                                                fontSize = 32.sp,
                                                fontFamily = mainFont,
                                                fontWeight = FontWeight(400),
                                                color = Color(0xFFFFFFFF),
                                            ),
                                            maxLines = 1,
                                        )
                                    }
                                }
                            }
                            LazyColumn(
                                modifier = Modifier
                                    .border(
                                        width = 1.dp,
                                        color = Color(0xFFFFFFFF),
                                        shape = RoundedCornerShape(size = 22.dp)
                                    )
                                    .width(164.dp)
                                    .height(164.dp)
                                    .background(
                                        color = itemColor,
                                        shape = RoundedCornerShape(size = 22.dp)
                                    )
                                    .padding(16.dp)
                            ) {
                                item {
                                    Text(
                                        text = stringResource(R.string.wind),
                                        style = TextStyle(
                                            fontSize = 16.sp,
                                            fontFamily = mainFont,
                                            fontWeight = FontWeight(400),
                                            color = Color(0x7AFFFFFF),
                                        )
                                    )
                                    LazyRow {
                                        item {
                                            Text(
                                                text = kilometersToMiles(
                                                    mainList.value.windSpeed,
                                                    context
                                                ),
                                                style = TextStyle(
                                                    fontSize = 32.sp,
                                                    fontFamily = mainFont,
                                                    fontWeight = FontWeight(400),
                                                    color = Color(0xFFFFFFFF),
                                                )
                                            )
                                        }
                                    }
                                    Text(
                                        text = mainList.value.windDirection,
                                        style = TextStyle(
                                            fontSize = 32.sp,
                                            fontFamily = mainFont,
                                            fontWeight = FontWeight(400),
                                            color = Color(0xFFFFFFFF),
                                        )
                                    )
                                }
                            }
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            LazyColumn(
                                modifier = Modifier
                                    .border(
                                        width = 1.dp,
                                        color = Color(0xFFFFFFFF),
                                        shape = RoundedCornerShape(size = 22.dp)
                                    )
                                    .width(164.dp)
                                    .height(164.dp)
                                    .background(
                                        color = itemColor,
                                        shape = RoundedCornerShape(size = 22.dp)
                                    )
                                    .padding(16.dp)

                            ) {
                                item {
                                    Text(
                                        text = stringResource(R.string.uv_index),
                                        style = TextStyle(
                                            fontSize = 16.sp,
                                            fontFamily = mainFont,
                                            fontWeight = FontWeight(400),
                                            color = Color(0x7AFFFFFF),
                                        )
                                    )
                                    Text(
                                        text = mainList.value.UVIndex.toString(),
                                        style = TextStyle(
                                            fontSize = 32.sp,
                                            fontFamily = mainFont,
                                            fontWeight = FontWeight(400),
                                            color = Color(0xFFFFFFFF),
                                        ),
                                        maxLines = 1,
                                    )
                                    Spacer(modifier = Modifier.padding(vertical = 5.dp))
                                    Text(
                                        text = mainList.value.UVDesc,
                                        style = TextStyle(
                                            fontSize = 16.sp,
                                            fontFamily = mainFont,
                                            fontWeight = FontWeight(400),
                                            color = Color(0xFFFFFFFF),
                                        )
                                    )
                                }
                            }
                            Column(
                                modifier = Modifier
                                    .width(164.dp)
                                    .height(164.dp)
                                    .background(
                                        color = backgroundColor,
                                        shape = RoundedCornerShape(size = 22.dp)
                                    )
                                    .padding(16.dp)

                            ) {}
                        }
                    }
                }
            }
//        AdMobBanner()
        }
    }
}
