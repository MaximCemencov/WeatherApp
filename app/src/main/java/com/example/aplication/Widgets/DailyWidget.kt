package com.example.aplication.Widgets

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.text.FontFamily
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.example.aplication.DataClasses.DailyDataClass
import com.example.aplication.Features.getLocationFromSharedPreferences
import com.example.aplication.logicExecution.loadFavoriteCity
import com.example.aplication.logicExecution.settingsProvider
import convertJsonObjectToDailyDataList
import getBackgroundColorResource
import getItemsColorResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

class DailyWidget : GlanceAppWidgetReceiver() { override val glanceAppWidget: GlanceAppWidget = MyDailyWidget() }




class MyDailyWidget : GlanceAppWidget() {
    var hourlyData: ArrayList<DailyDataClass> = ArrayList()

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        // Load data needed to render the AppWidget.
        // Use `withContext` to switch to another thread for long running
        // operations.
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
                "https://api.open-meteo.com/v1/forecast?latitude=$latitudeLocal&longitude=$longitudeLocal&daily=temperature_2m_min,temperature_2m_max,weathercode&timezone=auto"
            val client = OkHttpClient()
            val request = Request.Builder()
                .url(url)
                .build()
            val response = client.newCall(request).execute()

            val data = response.body?.string()

            hourlyData = convertJsonObjectToDailyDataList(data!!, context)
        }

        provideContent {
            MyContent(context)
        }
    }

    @Composable
    private fun MyContent(context: Context) {
        val colorBackground = getBackgroundColorResource(hourlyData[0].weatherIcon)

        LazyColumn(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(colorProvider = ColorProvider(colorBackground))
                .padding(7.dp)
        ) {
            items(hourlyData) { data ->
                val color = getItemsColorResource(data.weatherIcon)
                Column {
                    Row(
                        modifier = GlanceModifier
                            .background(colorProvider = ColorProvider(color))
                            .cornerRadius(7.dp)
                            .fillMaxWidth()
                            .height(50.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = data.day,
                            modifier = GlanceModifier.padding(2.dp).width(100.dp) ,
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = FontFamily.SansSerif,
                                color = ColorProvider(Color.White),
                                textAlign = TextAlign.Center
                            ),
                        )
                        Text(
                            text = "${settingsProvider(data.minTemp, context)}°",
                            modifier = GlanceModifier.padding(2.dp).width(58.dp),
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = FontFamily.SansSerif,
                                color = ColorProvider(Color.White),
                                textAlign = TextAlign.Center
                            ),
                        )
                        Text(
                            text = data.weatherIcon,
                            modifier = GlanceModifier.padding(2.dp),
                            style = TextStyle(
                                fontSize = 25.sp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = FontFamily.SansSerif,
                                color = ColorProvider(Color.White),
                                textAlign = TextAlign.Center
                            ),
                        )
                        Text(text = "${settingsProvider(data.maxTemp, context)}°",
                            modifier = GlanceModifier.width(55.dp),
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = FontFamily.SansSerif,
                                color = ColorProvider(Color.White),
                                textAlign = TextAlign.Center
                            ),
                        )
                    }
                    Spacer(GlanceModifier.height(4.dp))
                }
            }
        }
    }
}