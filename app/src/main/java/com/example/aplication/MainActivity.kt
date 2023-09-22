package com.example.aplication

import WeatherFeatureWithLocationPermission
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        MobileAds.initialize(this)

        setContent {
            Surface {
                WeatherFeatureWithLocationPermission(this)
            }
        }
    }
}


