package com.example.aplication.DataClasses

data class CitiesDataClass(
    val city: String,
    val latitude: Double,
    val longitude: Double,
    var favorite: Boolean = false
)