package com.example.aplication.screens.Items

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aplication.DataClasses.HourlyDataClass
import com.example.aplication.logicExecution.settingsProvider
import com.example.aplication.screens.mainFont

@Composable
fun RowItem (hourlyModel: HourlyDataClass, itemColors: Color, context: Context) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .shadow(elevation = 10.dp, spotColor = Color(0x40000000), ambientColor = Color(0x40000000))
            .border(width = 1.dp, color = Color(0xFFE7F6FF), shape = RoundedCornerShape(size = 30.dp))
            .width(60.dp)
            .height(146.dp)
            .background(color = itemColors, shape = RoundedCornerShape(size = 30.dp))
            .padding(start = 8.dp, top = 16.dp, end = 8.dp, bottom = 16.dp),
    ) {
        Text(
            text = hourlyModel.time,
            style = TextStyle(
                fontSize = 15.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight(800),
                color = Color(0xFFFFFFFF),
                textAlign = TextAlign.Center,
                fontFamily = mainFont,
            )
        )
        Text(
            text = hourlyModel.weatherIcon,
            style = TextStyle(
                fontSize = 40.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight(800),
                color = Color(0xFFFFFFFF),
                textAlign = TextAlign.Center,
                fontFamily = mainFont,
            )
        )
        Text(
            text = "${settingsProvider(hourlyModel.temp, context)}°",
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight(400),
                color = Color(0xFFFFFFFF),
                letterSpacing = 0.38.sp,
                fontFamily = mainFont,
            )
        )
    }
}