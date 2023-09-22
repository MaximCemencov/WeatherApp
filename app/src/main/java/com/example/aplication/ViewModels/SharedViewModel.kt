package com.example.aplication.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

data class GeoData(
    val latitude: Double? = null,
    val longitude: Double? = null,
    val showLocation: Boolean? = false,
    val showSelectedCity: Boolean? = false
    )


class SharedViewModel : ViewModel() {
    private val _geoData = MutableLiveData<GeoData>()
    val geoData: LiveData<GeoData> get() = _geoData


    fun setUserData(geoData: GeoData) {
        _geoData.value = geoData
    }

}
