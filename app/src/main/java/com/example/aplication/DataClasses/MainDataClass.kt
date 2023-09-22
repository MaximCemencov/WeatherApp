package com.example.aplication.DataClasses

data class MainDataClass(
    val city: String,
    val temp: Double,
    val minTemp: Double,
    val maxTemp: Double,
    val weatherDesc: String,
    val humidity: Int,
    val feelsLike: Double,
    val visibility: Double,
    val dewPoint: Double,
    val sunrise: String,
    val sunset: String,
    val precipitation: Double,
    val windSpeed: Double,
    val windDirection: String,
    val UVIndex: Int,
    val UVDesc: String,
    val backgroundColor: Int,
    val itemsColor: Int,
)
