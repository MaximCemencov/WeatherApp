package com.example.aplication.Widgets

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.text.FontFamily
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.example.aplication.Features.getLocationFromSharedPreferences
import com.example.aplication.MainActivity
import com.example.aplication.R
import com.example.aplication.logicExecution.loadFavoriteCity
import com.example.aplication.logicExecution.settingsProvider
import getItemsColorResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import weatherSmiley
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MiniWidget : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = MyMiniWidget()
}


class MyMiniWidget : GlanceAppWidget() {
    private var icon = ""
    private var temp = 0.0
    private var color = R.color.clear_Sky_Items
    var latitude: Double = 0.0
    var longitude: Double = 0.0


    override suspend fun provideGlance(context: Context, id: GlanceId) {
        withContext(Dispatchers.Default) {
            val favoriteCity = loadFavoriteCity(context)
            val pair = getLocationFromSharedPreferences(context)

            var latitudeLocal: Double?
            var longitudeLocal: Double?

            if (favoriteCity != null) {
                latitudeLocal = favoriteCity.latitude
                longitudeLocal = favoriteCity.longitude
            } else {
                latitudeLocal = pair.first
                longitudeLocal = pair.second
            }


            val url =
                "https://api.open-meteo.com/v1/forecast?latitude=$latitudeLocal&longitude=$longitudeLocal&hourly=temperature_2m,weathercode&daily=sunrise,sunset&timezone=auto"
            val client = OkHttpClient()
            val request = Request.Builder()
                .url(url)
                .build()
            val response = client.newCall(request).execute()

            val data = response.body?.string()
            val mainObject = JSONObject(data)

            val hourly = mainObject.getJSONObject("hourly")
            val daily = mainObject.getJSONObject("daily")

            val timeArray = hourly.getJSONArray("time")
            val arrayLength = timeArray.length()

            // Получаем текущее время
            val currentTime = Calendar.getInstance().time
            val dateFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())
            val currentTimeString = dateFormatter.format(currentTime)
            val currentHour = currentTimeString.substringBefore(":").toInt()


            val sunrise = daily.getJSONArray("sunrise").getString(0)
            val sunset = daily.getJSONArray("sunset").getString(0)

            // Находим индекс текущего часа
            var currentHourIndex = -1
            for (i in 0 until arrayLength) {
                val timeFull = timeArray.getString(i)
                val hour =
                    timeFull.substring(timeFull.indexOf('T') + 1).substringBefore(":").toInt()
                if (hour == currentHour) {
                    currentHourIndex = i
                    break
                }
            }

            val timeFull = timeArray.getString(currentHourIndex)
            val hour = timeFull.substring(timeFull.indexOf('T') + 1)

            icon = weatherSmiley(
                hourly.getJSONArray("weathercode").getInt(currentHourIndex),
                hour,
                sunrise.substring(sunrise.indexOf('T') + 1),
                sunset.substring(sunset.indexOf('T') + 1)
            )
            temp = hourly.getJSONArray("temperature_2m").getDouble(currentHourIndex)
            color = getItemsColorResource(
                weatherSmiley(
                    hourly.getJSONArray("weathercode").getInt(currentHourIndex),
                    hour,
                    sunrise.substring(sunrise.indexOf('T') + 1),
                    sunset.substring(sunset.indexOf('T') + 1)
                )
            )
            MyMiniWidget().update(context, id)
        }

        provideContent { MyContent(context) }
    }


    @Composable
    private fun MyContent(context: Context) {
        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(colorProvider = ColorProvider(color))
                .clickable(actionStartActivity<MainActivity>()),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = icon,
                modifier = GlanceModifier.fillMaxWidth(),
                TextStyle(
                    fontSize = 50.sp,
                    textAlign = TextAlign.Center
                )
            )
            Text(
                text = "${settingsProvider(temp, context)}°",
                modifier = GlanceModifier.fillMaxWidth().padding(2.dp),
                style = TextStyle(
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = FontFamily.SansSerif,
                    color = ColorProvider(Color.White),
                    textAlign = TextAlign.Center
                ),
            )

        }
    }
}