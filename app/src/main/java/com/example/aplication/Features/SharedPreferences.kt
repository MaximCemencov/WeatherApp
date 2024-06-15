package com.example.aplication.Features

import android.content.Context

fun saveLocationToSharedPreferences(context: Context, latitude: Double, longitude: Double) {
    val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putFloat("latitude", latitude.toFloat())
    editor.putFloat("longitude", longitude.toFloat())
    editor.apply()
}

fun getLocationFromSharedPreferences(context: Context): Pair<Double, Double> {
    val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    val latitude = sharedPreferences.getFloat("latitude", 0f).toDouble()
    val longitude = sharedPreferences.getFloat("longitude", 0f).toDouble()
    return Pair(latitude, longitude)
}
