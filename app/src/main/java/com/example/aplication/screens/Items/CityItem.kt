package com.example.aplication.screens.Items

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aplication.DataClasses.CitiesDataClass
import com.example.aplication.R
import com.example.aplication.ViewModels.CitiesModes
import com.example.aplication.logicExecution.settingsProvider
import com.example.aplication.screens.mainFont


@Composable
fun CityItem(
    city: CitiesDataClass,
    viewModelView: CitiesModes,
    minTemp: Double,
    maxTemp: Double,
    weatherCodeArray: String,
    temp: Double,
    itemColor: Int,
    context: Context
) {

    Row(
        modifier = Modifier
            .width(235.dp)
            .height(120.dp)
            .background(
                colorResource(itemColor),
                shape = MaterialTheme.shapes.medium
            )
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = "${settingsProvider(temp, context)}°",
                style = TextStyle(
                    fontSize = 36.sp,
                    fontFamily = mainFont,
                    fontWeight = FontWeight(600),
                    color = Color(0xFFFFFFFF),
                    letterSpacing = 0.37.sp,
                )

            )
            Text(
                text = "H: ${settingsProvider(maxTemp, context)}° L:${settingsProvider(minTemp, context)}",
                style = TextStyle(
                    fontSize = 12.sp,
                    fontFamily = mainFont,
                    fontWeight = FontWeight(600),
                    color = Color(0xFFFFFFFF)
                ),
            )
            LazyRow (
                modifier = Modifier.width(120.dp)
                    ){
                item {
                    Text(
                        text = city.city,
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontFamily = mainFont,
                            fontWeight = FontWeight(600),
                            color = Color(0xFFFFFFFF)
                        ),
                    )
                }
            }
        }
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = weatherCodeArray,
                style = TextStyle(
                    fontSize = 40.sp,
                ),
                modifier = Modifier.padding(end = 10.dp)
            )
            Row(
                modifier = Modifier
                    .padding(top = 5.dp)
                    .height(25.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.favorite),
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = mainFont,
                        fontWeight = FontWeight(600),
                        color = Color(0xFFFFFFFF)
                    )
                )
                Checkbox(
                    checked = city.favorite,
                    onCheckedChange = {
                        viewModelView.selectFavoriteCity(city)
                    },
                    modifier = Modifier
                        .scale(0.8f)
                        .size(30.dp),
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color.Green
                    )
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = stringResource(R.string.delete),
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = mainFont,
                        fontWeight = FontWeight(600),
                        color = Color(0xFFFFFFFF)
                    )
                )
                IconButton(
                    modifier = Modifier
                        .scale(0.8f)
                        .size(30.dp),
                    onClick = {
                        //здесь удаляем город из памяти
                        viewModelView.removeCity(city)
                    },
                ) {
                    Icon(
                        Icons.Filled.Delete,
                        contentDescription = null,
                        tint = Color.Red,
                        modifier = Modifier
                            .scale(0.8f)
                            .size(40.dp)
                    )
                }
            }

        }
    }
}