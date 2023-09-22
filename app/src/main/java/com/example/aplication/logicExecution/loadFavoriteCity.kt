package com.example.aplication.logicExecution

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.example.aplication.DataClasses.CitiesDataClass
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken



fun loadFavoriteCity(context: Context): CitiesDataClass? {
    val sharedPreferences: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)

    val citiesKey = "CITIES_KEY"


    val citiesJson = sharedPreferences.getString(citiesKey, null)
    val citiesList = citiesJson?.let { json ->
        // Преобразование JSON в List<CitiesDataClass>
        // Для простоты используется библиотека Gson, убедитесь, что вы добавили Gson в зависимости проекта.
        val gson = Gson()
        val type = object : TypeToken<List<CitiesDataClass>>() {}.type
        gson.fromJson<List<CitiesDataClass>>(json, type)
    } ?: emptyList()

    val favoriteCity = citiesList.find { it.favorite }
    return favoriteCity
}