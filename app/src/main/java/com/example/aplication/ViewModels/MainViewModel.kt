package com.example.aplication.ViewModels

import android.app.Application
import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.glance.appwidget.updateAll
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplication.DataClasses.DailyDataClass
import com.example.aplication.DataClasses.HourlyDataClass
import com.example.aplication.DataClasses.MainDataClass
import com.example.aplication.R
import com.example.aplication.Widgets.MyDailyWidget
import com.example.aplication.Widgets.MyHourlyWidget
import com.example.aplication.Widgets.MyMiniWidget
import com.example.aplication.logicExecution.fetchWeatherData
import com.example.aplication.logicExecution.loadFavoriteCity
import convertJsonObjectToDailyDataList
import convertJsonObjectToHourlyDataList
import convertJsonObjectToMainData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val context: Context = getApplication<Application>().applicationContext

    private val _isloading = MutableStateFlow(false)
    val isloading = _isloading.asStateFlow()


    val daysList = mutableStateOf(listOf<DailyDataClass>())
    val hoursList = mutableStateOf(listOf<HourlyDataClass>())
    val mainList = mutableStateOf(MainDataClass("", 0.0, 0.0, 0.0, "", 0, 0.0, 0.0, 0.0, "", "", 0.0, 0.0, "", 0, "", R.color.white, R.color.white))


    init {
        reLoad()
    }


    fun reLoad(geoData: GeoData? = null) {
        val latitude = geoData?.latitude
        val longitude = geoData?.longitude
        val showLocation = geoData?.showLocation
        val showSelectedCity = geoData?.showSelectedCity


        viewModelScope.launch {
            _isloading.value = true
            val _favoriteCity = loadFavoriteCity(context)
            val latitudeLocal: Double?
            val longitudeLocal: Double?

            // если есть _favoriteCity и нет showLocation и нет showSelectedCity
            if (_favoriteCity != null && showLocation != true && showSelectedCity != true) {
                latitudeLocal = _favoriteCity.latitude
                longitudeLocal = _favoriteCity.longitude
            }
            //если есть showSelectedCity и нет _favoriteCity и нет showLocation
            else if (showLocation != true && showSelectedCity != false){
                latitudeLocal = latitude
                longitudeLocal = longitude
            }
            // если есть showLocation и нет _favoriteCity и нет showSelectedCity
            else if (showLocation != false && showSelectedCity != true ) {
                latitudeLocal = null
                longitudeLocal = null
            }
            else {
                latitudeLocal = null
                longitudeLocal = null
            }




            val weatherData = fetchWeatherData(context, latitudeLocal, longitudeLocal)
            daysList.value = convertJsonObjectToDailyDataList(weatherData!!, context)
            hoursList.value = convertJsonObjectToHourlyDataList(weatherData)
            mainList.value = convertJsonObjectToMainData(weatherData, context)
            MyMiniWidget().updateAll(context)
            MyHourlyWidget().updateAll(context)
            MyDailyWidget().updateAll(context)
            _isloading.value = false
        }
    }
}