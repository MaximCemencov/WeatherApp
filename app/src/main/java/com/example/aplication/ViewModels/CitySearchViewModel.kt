package com.example.aplication.ViewModels

import City
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException

class SearchCity : ViewModel() {
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()


    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()


    private val _cities = MutableStateFlow(listOf<City>())
    val cities = searchText
        .debounce(1000L)
        .onEach { _isSearching.update { true } }
        .combine(_cities) { text, cities ->
            _cities.value = parseCities(_searchText.value)
            cities
        }
        .onEach { _isSearching.update { false } }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            _cities.value
        )


    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }
}


private suspend fun parseCities(cityName: String): List<City> {
    val url =
        "http://api.geonames.org/searchJSON?name=$cityName&fuzzy=0.5&maxRows=15&username=maximcemencov"

    val client = OkHttpClient()

    val request = Request.Builder()
        .url(url)
        .build()

    val allPersons = mutableListOf<City>()

    withContext(Dispatchers.IO) {
        try {
            val response = client.newCall(request).execute()
            val data = response.body?.string()

            // Парсим JSON-ответ
            val mainObject = JSONObject(data)
            val citiesArray = mainObject.getJSONArray("geonames")

            // Создаем список для хранения объектов City

            // Проходим по массиву и извлекаем данные
            for (i in 0 until citiesArray.length()) {
                val cityObject = citiesArray.getJSONObject(i)
                val cityName = cityObject.getString("name")
                val countryName =
                    "${cityObject.getString("countryName")} ${cityObject.getString("countryCode")}"
                val latitude = cityObject.getString("lat")
                val longitude = cityObject.getString("lng")

                // Создаем объект City и добавляем его в список
                val city = City(cityName, countryName, latitude, longitude)
                allPersons.add(city)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    return allPersons

}