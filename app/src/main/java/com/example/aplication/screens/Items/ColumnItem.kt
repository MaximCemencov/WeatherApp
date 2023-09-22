package com.example.aplication.screens.Items


import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aplication.DataClasses.DailyDataClass
import com.example.aplication.logicExecution.settingsProvider
import com.example.aplication.screens.mainFont

@Composable
fun ColumnItem(dailyDataClass: DailyDataClass, itemColors: Color, context: Context) {
    Row(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = Color(0xFFFFFFFF),
                shape = RoundedCornerShape(size = 30.dp)
            )
            .fillMaxWidth()
            .height(65.dp)
            .background(
                color = itemColors,
                shape = RoundedCornerShape(size = 30.dp)
            ),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,

        ) {
        Text(text = dailyDataClass.day,
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 20.sp,
            ),
            fontFamily = mainFont,
            fontWeight = FontWeight(700),
            color = Color(0xFFFFFFFF),
            modifier = Modifier.width(100.dp),
            textAlign = TextAlign.Center
        )
        Text(
            text = "Min: ${settingsProvider(dailyDataClass.minTemp, context)}°",
            style = TextStyle(
                fontSize = 18.sp,
                lineHeight = 20.sp,
            ),
            fontFamily = mainFont,
            fontWeight = FontWeight(400),
            color = Color(0xFFFFFFFF),
        )
        Text(text = dailyDataClass.weatherIcon,
            style = TextStyle(
                fontSize = 25.sp,
                lineHeight = 20.sp,
            ),
            fontFamily = mainFont,
            color = Color(0xFFFFFFFF),
        )
        Text(text = "Max: ${settingsProvider(dailyDataClass.maxTemp,context)}°",
            style = TextStyle(
                fontSize = 17.sp,
                lineHeight = 20.sp,
            ),
            fontFamily = mainFont,
            fontWeight = FontWeight(400),
            color = Color(0xFFFFFFFF),
        )

    }
    Spacer(modifier = Modifier.height(10.dp))
}