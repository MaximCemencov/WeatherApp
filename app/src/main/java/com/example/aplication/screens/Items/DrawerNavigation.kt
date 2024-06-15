package com.example.aplication.screens.Items

import MainScreenWithTopAppBar
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.aplication.R
import com.example.aplication.ViewModels.CitiesModes
import com.example.aplication.ViewModels.GeoData
import com.example.aplication.ViewModels.SharedViewModel
import com.example.aplication.screens.CitySearch
import com.example.aplication.screens.Settings
import com.example.aplication.screens.mainFont
import getItemsColorResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import weatherSmiley
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun Drawer(context: Context) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val navController = rememberNavController()
    val sharedViewModel: SharedViewModel = viewModel()

    val viewModelView = viewModel<CitiesModes>()
    val cities by viewModelView.cities.collectAsState()


    ModalNavigationDrawer(drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier
                        .background(colorResource(R.color.clear_Sky_Background))
                        .fillMaxHeight()
                        .width(250.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IconButton(
                            modifier = Modifier
                                .padding(11.dp)
                                .size(35.dp),
                            onClick = {
                                navController.navigate("settings")
                                scope.launch { drawerState.close() }
                            },
                        ) {
                            Icon(
                                Icons.Filled.Settings,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        Text(
                            text = stringResource(R.string.menu_Drawer),
                            style = TextStyle(
                                fontSize = 25.sp,
                                lineHeight = 20.sp,
                                fontFamily = mainFont,
                                fontWeight = FontWeight(600),
                                color = Color(0xFFFFFFFF),
                            ),
                            modifier = Modifier.padding(11.dp)
                        )
                        IconButton(
                            modifier = Modifier
                                .padding(11.dp)
                                .size(35.dp),
                            onClick = {
                                navController.navigate("registration")
                                scope.launch { drawerState.close() }
                            },
                        ) {
                            Icon(
                                Icons.Filled.Add,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                    }
                    LazyColumn(
                        modifier = Modifier
                            .width(250.dp)
                            .fillMaxHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        items(cities.reversed()) { city ->
                            var parsedData by remember(city) {
                                mutableStateOf<String?>(null)
                            }
                            val url =
                                "https://api.open-meteo.com/v1/forecast?latitude=${city.latitude}&longitude=${city.longitude}&hourly=temperature_2m,weathercode&daily=weathercode,temperature_2m_max,temperature_2m_min,sunrise,sunset&timezone=auto"
                            LaunchedEffect(city) {
                                // Выполнение парсинга данных только один раз для каждого элемента списка
                                val result = try {
                                    withContext(Dispatchers.IO) {
                                        val client = OkHttpClient()
                                        val request = Request.Builder()
                                            .url(url)
                                            .build()

                                        val response = client.newCall(request).execute()
                                        response.body?.string()
                                    }
                                } catch (e: IOException) {
                                    null
                                }
                                if (result != null) {
                                    parsedData = result
                                }
                            }

                            val mainObject =
                                if (!parsedData.isNullOrEmpty()) JSONObject(parsedData) else null


                            if (mainObject != null) {
                                val hourly = mainObject.getJSONObject("hourly")
                                val daily = mainObject.getJSONObject("daily")

                                val timeArray = hourly.getJSONArray("time")
                                val arrayLength = timeArray.length()

                                val sunrise = daily.getJSONArray("sunrise").getString(0)
                                val sunset = daily.getJSONArray("sunset").getString(0)

                                // Получаем текущее время
                                val currentTime = Calendar.getInstance().time
                                val dateFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())
                                val currentTimeString = dateFormatter.format(currentTime)
                                val currentHour = currentTimeString.substringBefore(":").toInt()


                                // Находим индекс текущего часа
                                var currentHourIndex = -1
                                for (i in 0 until arrayLength) {
                                    val timeFull = timeArray.getString(i)
                                    val hour = timeFull.substring(timeFull.indexOf('T') + 1)
                                        .substringBefore(":").toInt()
                                    if (hour == currentHour) {
                                        currentHourIndex = i
                                        break
                                    }
                                }

                                val timeFull = timeArray.getString(currentHourIndex)
                                val hour = timeFull.substring(timeFull.indexOf('T') + 1)

                                val minTemp = daily.getJSONArray("temperature_2m_min").getDouble(0)
                                val maxTemp = daily.getJSONArray("temperature_2m_max").getDouble(0)
                                val weatherCodeArray =
                                    weatherSmiley(
                                        hourly.getJSONArray("weathercode").getInt(currentHourIndex),
                                        hour,
                                        sunrise.substring(sunrise.indexOf('T') + 1),
                                        sunset.substring(sunset.indexOf('T') + 1)
                                    )
                                val temp = hourly.getJSONArray("temperature_2m")
                                    .getDouble(currentHourIndex)
                                val itemColor = getItemsColorResource(weatherCodeArray)

                                Box(
                                    modifier = Modifier
                                        .clickable(
                                            enabled = true,
                                            onClick = {
                                                sharedViewModel.setUserData(
                                                        GeoData(
                                                        city.latitude,
                                                        city.longitude,
                                                        showLocation = false,
                                                        showSelectedCity = true
                                                    )
                                                )


                                                scope.launch { drawerState.close() }

                                                navController.navigate("home")
                                            }
                                        )
                                        .padding(vertical = 5.dp)
                                ) {
                                    CityItem(
                                        city,
                                        viewModelView,
                                        minTemp,
                                        maxTemp,
                                        weatherCodeArray,
                                        temp,
                                        itemColor,
                                        context
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        content = {
            NavHost(navController = navController, startDestination = "home") {
                composable("home") {
                    MainScreenWithTopAppBar(
                        drawerState,
                        scope,
                        sharedViewModel.geoData.value,
                        context
                    ) {
                        sharedViewModel.setUserData(
                            geoData = GeoData(
                                null,
                                null,
                                showLocation = true,
                                showSelectedCity = false
                            )
                        )
                        navController.navigate("home")
                    }
                }

                composable("registration") {
                    CitySearch(viewModelView, drawerState, scope) {
                        navController.navigate("home") {
                            popUpTo("home") {
                                inclusive = true
                            }
                        }
                    }
                }

                composable("settings") {
                    Settings(context) {
                        navController.navigate("home") {
                            popUpTo("home") {
                                inclusive = true
                            }
                        }
                    }
                }

            }
        }
    )
}