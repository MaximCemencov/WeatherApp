package com.example.aplication.Features

import android.content.Context
import android.location.Geocoder
import com.example.aplication.R
import java.io.IOException
import java.util.*



fun getCityNameFromCoordinates(context: Context, latitude: Double, longitude: Double): String {
    val geocoder = Geocoder(context, Locale.getDefault())

    try {
        val addresses = geocoder.getFromLocation(latitude, longitude, 1)
        addresses?.let {
            if (it.isNotEmpty()) {
                val address = it[0]
                val cityName = address.locality ?: address.subAdminArea
                if (cityName != null) {
                    return cityName
                }
            }
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }

    return context.getString(R.string.city_cannot_be_found)
}



