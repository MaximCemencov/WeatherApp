package com.example.aplication.ViewModels

import City
import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.lifecycle.AndroidViewModel
import com.example.aplication.DataClasses.CitiesDataClass
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CitiesModes(application: Application) : AndroidViewModel(application) {
    private val _cities = MutableStateFlow(listOf<CitiesDataClass>())
    val cities = _cities.asStateFlow()

    private val gson = Gson()
    private val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(application)

    private val citiesKey = "CITIES_KEY"

    init {
        // Загрузка городов из SharedPreferences при инициализации ViewModel
        loadCities()
    }


    fun loadCities() {
        val citiesJson = sharedPreferences.getString(citiesKey, null)
        val citiesList = citiesJson?.let { json ->
            // Преобразование JSON в List<CitiesDataClass>
            // Для простоты используется библиотека Gson, убедитесь, что вы добавили Gson в зависимости проекта.
            val gson = Gson()
            val type = object : TypeToken<List<CitiesDataClass>>() {}.type
            gson.fromJson<List<CitiesDataClass>>(json, type)
        } ?: emptyList()

        _cities.value = citiesList
    }


    private fun saveCities() {
        // Преобразование List<CitiesDataClass> в JSON
        val citiesJson = gson.toJson(_cities.value)

        // Сохранение JSON в SharedPreferences
        sharedPreferences.edit().putString(citiesKey, citiesJson).apply()
    }

    fun addCity(city: City) {
        // Преобразование City в CitiesDataClass и добавление в список
        val citiesList = _cities.value.toMutableList()
        citiesList.add(
            CitiesDataClass(
                city.cityCurrentLanguageName,
                city.latitude.toDouble(),
                city.longitude.toDouble()
            )
        )
        _cities.value = citiesList

        // Сохранение обновленного списка
        saveCities()
    }


    fun selectFavoriteCity(city: CitiesDataClass) {
        val citiesList = _cities.value.toMutableList()

        citiesList.remove(city)

        // Создаем новый город с противоположным значением favorite
        val updatedCity = city.copy(favorite = !city.favorite)

        // Устанавливаем favorite в false для всех остальных городов
        citiesList.forEach { otherCity ->
            otherCity.favorite = false
        }

        // Добавляем обновленный город в список
        citiesList.add(updatedCity)

        // Сохранение обновленного списка
        _cities.value = citiesList
        saveCities()
    }



    fun removeCity(city: CitiesDataClass) {
        // Удаление указанного города из списка
        val citiesList = _cities.value.toMutableList()
        citiesList.remove(city)
        _cities.value = citiesList

        // Сохранение обновленного списка
        saveCities()
    }

}
