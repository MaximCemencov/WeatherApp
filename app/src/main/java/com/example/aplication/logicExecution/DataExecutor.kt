package com.example.aplication.logicExecution

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.aplication.Features.saveLocationToSharedPreferences
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

var latitudeLocal = 0.0
var longitudeLocal = 0.0
suspend fun fetchWeatherData(context: Context, latitude: Double? = null, longitude: Double? = null): String? {


    // Проверка разрешения на использование геопозиции
    val hasLocationPermission = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED


    if (latitude == null && longitude == null) {
        // Проверка включенности геолокации на телефоне
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isLocationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (!isLocationEnabled) {
            showToast(context, "Пожалуйста включите GPS")

            // Ожидание разрешения от пользователя (но не блокирует UI поток)
            suspendCancellableCoroutine { continuation ->
                var resumed = false

                // Вешаем слушатель на изменение состояния GPS
                val locationListener = object : LocationListener {
                    override fun onLocationChanged(location: Location) {
                        // Пользователь включил GPS, можно продолжить выполнение
                        if (!resumed) {
                            resumed = true
                            continuation.resume(Unit)
                            // Убираем слушатель
                            locationManager.removeUpdates(this)
                        }
                    }
                }
                // Регистрируем слушатель для получения обновлений о состоянии GPS
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    0,
                    0f,
                    locationListener
                )

                // Вешаем слушатель на изменение состояния сети (для обработки случаев, когда GPS недоступен)
                val networkListener = object : LocationListener {
                    override fun onLocationChanged(location: Location) {
                        // Пользователь включил GPS, можно продолжить выполнение
                        if (!resumed) {
                            resumed = true
                            continuation.resume(Unit)
                            // Убираем слушатель
                            locationManager.removeUpdates(this)
                        }
                    }
                }
                // Регистрируем слушатель для получения обновлений о состоянии сети
                locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    0,
                    0f,
                    networkListener
                )

                // Вешаем слушатель на таймаут (например, 60 секунд)
                val timeoutRunnable = Runnable {
                    // Если до таймаута не получено обновлений о состоянии GPS, продолжаем выполнение
                    if (!resumed) {
                        resumed = true
                        continuation.resume(Unit)
                        // Убираем слушатели
                        locationManager.removeUpdates(locationListener)
                        locationManager.removeUpdates(networkListener)
                    }
                }
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed(timeoutRunnable, 60000) // Таймаут в 60 секунд
            }
        }

        // Получение последней известной локации пользователя
        val location: Location = suspendCancellableCoroutine { continuation ->
            val fusedLocationClient: FusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(context)
            val task: Task<Location?> = fusedLocationClient.lastLocation

            task.addOnSuccessListener { location ->
                continuation.resume(location)
            }

            task.addOnFailureListener { exception ->
                showToast(context, "Невозможно получить данные о геолокацию")

                continuation.resumeWithException(exception)
            }
        } ?: return null


        latitudeLocal = location.latitude
        longitudeLocal = location.longitude
        saveLocationToSharedPreferences(context, latitudeLocal, longitudeLocal)
    } else {
        if (latitude != null) {
            latitudeLocal = latitude
        }
        if (longitude != null) {
            longitudeLocal = longitude
        }
    }


    val url =
        "https://api.open-meteo.com/v1/forecast?latitude=$latitudeLocal&longitude=$longitudeLocal&hourly=temperature_2m,relativehumidity_2m,dewpoint_2m,apparent_temperature,precipitation_probability,precipitation,weathercode,visibility,windspeed_10m,winddirection_10m&daily=weathercode,temperature_2m_max,temperature_2m_min,sunrise,sunset,uv_index_max,precipitation_probability_max&timezone=auto"
    // Выполнение запроса и получение данных
    return try {
        withContext(Dispatchers.IO) {
            val client = OkHttpClient()
            val request = Request.Builder()
                .url(url)
                .build()

            val response = client.newCall(request).execute()
            response.body?.string()
        }
    } catch (e: IOException) {
        showToast(context, "No internet Connection")
        null
    }


}


fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}