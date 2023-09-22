package com.example.aplication.logicExecution

import android.content.Context
import com.example.aplication.R
import com.example.aplication.screens.loadSwitchState

fun settingsProvider(value: Double, context: Context): Any {
    val changeDecimal = loadSwitchState(context, "Change_decimal")
    val changeFahrenheit = loadSwitchState(context, "Change_fahrenheit")

    return if (changeFahrenheit) {
        val fahrenheit = celsiusToFahrenheit(value)
        if (changeDecimal) {
            // Округляем до одного знака после запятой
        fahrenheit.toInt()
        } else {
            String.format("%.1f", fahrenheit)
        }
    } else {
        if (changeDecimal) {
            value.toInt()
        } else {
            value
        }
    }
}


fun celsiusToFahrenheit(celsius: Double): Double {
    return celsius * 9 / 5 + 32
}



fun millimetersToInches(millimeters: Double, context: Context): String {
    // Преобразовываем миллиметры в дюймы
    val inches = millimeters / 25.4

    val changeFahrenheit = loadSwitchState(context, "Change_fahrenheit")
    val result = if (changeFahrenheit) {
        // Возвращаем значение в дюймах
        String.format("%.2f", inches) + context.getString(R.string.inch)
    } else {
        // Если changeFahrenheit = false, то возвращаем исходное значение в миллиметрах
        String.format("%.2f", millimeters) + context.getString(R.string.millimeters)
    }

    return result
}


fun kilometersToMiles(kilometers: Double, context: Context): String {
    val changeFahrenheit = loadSwitchState(context, "Change_fahrenheit")
    val result = if (changeFahrenheit) {
        // 1 километр = 0.621371 миль
        val miles = kilometers * 0.621371
        String.format("%.2f", miles) + context.getString(R.string.miles)
    } else {
        // Если changeFahrenheit = false, то возвращаем исходное значение в километрах
        String.format("%.2f", kilometers) + context.getString(R.string.kilometers)
    }
    return result
}


fun metersToMilesOrKilometers(meters: Double, context: Context): String {
    // Преобразовываем метры в километры
    val kilometers = meters / 1000.0

    val changeFahrenheit = loadSwitchState(context, "Change_fahrenheit")
    val result = if (changeFahrenheit) {
        // 1 километр = 0.621371 миль
        val miles = kilometers * 0.621371
        String.format("%.2f", miles) + context.getString(R.string.mi)
    } else {
        // Если changeFahrenheit = false, то возвращаем исходное значение в километрах
        String.format("%.2f", kilometers) + context.getString(R.string.km)
    }

    return result
}


