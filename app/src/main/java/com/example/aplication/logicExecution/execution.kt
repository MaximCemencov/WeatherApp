import android.content.Context
import com.example.aplication.DataClasses.DailyDataClass
import com.example.aplication.DataClasses.HourlyDataClass
import com.example.aplication.DataClasses.MainDataClass
import com.example.aplication.Features.getCityNameFromCoordinates
import com.example.aplication.R
import com.example.aplication.logicExecution.latitudeLocal
import com.example.aplication.logicExecution.longitudeLocal
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.Calendar
import java.util.Locale

fun getDayOfWeekName(dateString: String, context: Context): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd")
    val date = dateFormat.parse(dateString)

    val calendar = Calendar.getInstance()
    calendar.time = date

    val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

    val dayOfWeekString = when (dayOfWeek) {
        Calendar.SUNDAY -> context.getString(R.string.sunday)
        Calendar.MONDAY -> context.getString(R.string.monday)
        Calendar.TUESDAY -> context.getString(R.string.tuesday)
        Calendar.WEDNESDAY -> context.getString(R.string.wednesday)
        Calendar.THURSDAY -> context.getString(R.string.thursday)
        Calendar.FRIDAY -> context.getString(R.string.friday)
        Calendar.SATURDAY -> context.getString(R.string.saturday)
        else -> ""
    }

    return dayOfWeekString
}

fun weatherSmiley(
    weatherCode: Int,
    time: String? = null,
    sunrise: String? = null,
    sunset: String? = null
): String {
    val currentTime = time?.let { LocalTime.parse(it) }
    val sunriseTime = sunrise?.let { LocalTime.parse(it) }
    val sunsetTime = sunset?.let { LocalTime.parse(it) }

    if (weatherCode == 0 && currentTime != null && sunriseTime != null && sunsetTime != null) {
        // Преобразуем время в формат "часы * 60 + минуты"
        val currentTimeMinutes = currentTime.hour * 60 + currentTime.minute
        val sunriseMinutes = sunriseTime.hour * 60 + sunriseTime.minute
        val sunsetMinutes = sunsetTime.hour * 60 + sunsetTime.minute

        // Проверяем, находится ли currentTimeMinutes между sunriseMinutes и sunsetMinutes с учетом перехода дня
        if (currentTimeMinutes >= sunriseMinutes && currentTimeMinutes <= sunsetMinutes) {
            return "☀️" // День и солнечная погода
        } else {
            return "🌙" // Месяц
        }
    } else {
        // Возвращаем соответствующий смайлик на основе weatherCode
        return when (weatherCode) {
            0 -> "☀️"
            1 -> "🌤️"
            2 -> "⛅️"
            3 -> "☁️"
            in listOf(80, 81, 82, 95) -> "⛈️"
            in listOf(61, 63, 65, 51, 53, 55) -> "🌧️"
            in listOf(45, 48) -> "🌫️"
            in listOf(56, 57, 66, 67, 71, 73, 75, 85, 86) -> "🌨️"
            else -> "😱" // Смайлик ужаса для неизвестной погоды
        }
    }
}

fun weatherDescription(weatherCode: Int, context: Context): String {
    return when (weatherCode) {
        0 -> context.getString(R.string.sunny)
        1 -> context.getString(R.string.partly_cloudy)
        2 -> context.getString(R.string.cloudy_with_clearings)
        3 -> context.getString(R.string.mainly_cloudy)
        in listOf(80, 81, 82, 95) -> context.getString(R.string.thunderstorm)
        in listOf(61, 63, 65, 51, 53, 55) -> context.getString(R.string.rain)
        in listOf(45, 48) -> context.getString(R.string.fog)
        in listOf(56, 57, 66, 67, 71, 73, 75, 85, 86) -> context.getString(R.string.snow)
        else -> context.getString(R.string.unknown_weather)
    }
}

fun getUVIndexLevel(uvIndex: Int, context: Context): String {
    return when (uvIndex) {
        in 0..2 -> context.getString(R.string.weak)
        in 3..5 -> context.getString(R.string.moderate)
        in 6..7 -> context.getString(R.string.heavy)
        in 8..10 -> context.getString(R.string.very_strong)
        else -> context.getString(R.string.extreme)
    } + " " + context.getString(R.string.ultraviolet_radiation)
}

fun getCompassDirection(degrees: Int, context: Context): String {
    val directions = listOf(
        context.getString(R.string.north),
        context.getString(R.string.north_northeast),
        context.getString(R.string.northeast),
        context.getString(R.string.east_northeast),
        context.getString(R.string.east),
        context.getString(R.string.east_southeast),
        context.getString(R.string.southeast),
        context.getString(R.string.south_southeast),
        context.getString(R.string.south),
        context.getString(R.string.south_southwest),
        context.getString(R.string.southwest),
        context.getString(R.string.west_southwest),
        context.getString(R.string.west),
        context.getString(R.string.west_northwest),
        context.getString(R.string.northwest),
        context.getString(R.string.north_northwest)
    )
    val index = (degrees % 360 + 360) % 360 / 22.5
    return directions[index.toInt()]
}


fun getBackgroundColorResource(weatherSmile: String): Int {
    return when (weatherSmile) {
        "☀️", "🌤", "⛅️" -> R.color.clear_Sky_Background
        "🌙" -> R.color.night_Sky_Background
        "🌫️", "☁️" -> R.color.mainlyCloudy_Sky_Background
        "🌨️", "🌧️", "⛈️" -> R.color.rain_Sky_Background
        else -> R.color.clear_Sky_Background // Используем цвет для ясного неба по умолчанию
    }
}

fun getItemsColorResource(weatherSmile: String): Int {
    return when (weatherSmile) {
        "☀️", "🌤", "⛅️" -> R.color.clear_Sky_Items
        "🌙" -> R.color.night_Sky_Items
        "☁️", "🌫️" -> R.color.mainlyCloudy_Sky_Items
        "🌨️", "🌧️", "⛈️" -> R.color.rain_Sky_Items
        else -> R.color.clear_Sky_Items // Используем цвет элементов для ясного неба по умолчанию
    }
}


fun convertJsonObjectToDailyDataList(
    response: String,
    context: Context
): ArrayList<DailyDataClass> {
    val mainObject = JSONObject(response)
    val jsonObject = mainObject.getJSONObject("daily")

    val dataList = ArrayList<DailyDataClass>()

    val timeArray = jsonObject.getJSONArray("time")
    val minTempArray = jsonObject.getJSONArray("temperature_2m_min")
    val maxTempArray = jsonObject.getJSONArray("temperature_2m_max")
    val weatherCodeArray = jsonObject.getJSONArray("weathercode")

    val arrayLength = timeArray.length()

    for (i in 0 until arrayLength) {
        val day = getDayOfWeekName(timeArray.getString(i), context)
        val minTemp = minTempArray.getDouble(i)
        val maxTemp = maxTempArray.getDouble(i)
        val weatherIcon = weatherSmiley(weatherCodeArray.getInt(i))

        dataList.add(DailyDataClass(day, minTemp, maxTemp, weatherIcon))
    }

    return dataList
}

fun convertJsonObjectToHourlyDataList(response: String): ArrayList<HourlyDataClass> {
    val mainObject = JSONObject(response)
    val jsonObject = mainObject.getJSONObject("hourly")

    val dataList = ArrayList<HourlyDataClass>()

    val timeArray = jsonObject.getJSONArray("time")
    val tempArray = jsonObject.getJSONArray("temperature_2m")
    val weatherCodeArray = jsonObject.getJSONArray("weathercode")
    val daily = mainObject.getJSONObject("daily")

    val fullsunrise = daily.getJSONArray("sunrise").getString(0)
    val fullsunset = daily.getJSONArray("sunset").getString(0)

    val sunrise = fullsunrise.substring(fullsunrise.indexOf('T') + 1)
    val sunset = fullsunset.substring(fullsunset.indexOf('T') + 1)

    // Получаем текущее время
    val currentTime = Calendar.getInstance().time
    val dateFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    val currentTimeString = dateFormatter.format(currentTime)
    val currentHour = currentTimeString.substringBefore(":").toInt()

    val arrayLength = timeArray.length()

    // Находим индекс текущего часа
    var currentHourIndex = -1
    for (i in 0 until arrayLength) {
        val timeFull = timeArray.getString(i)
        val hour = timeFull.substring(timeFull.indexOf('T') + 1).substringBefore(":").toInt()
        if (hour == currentHour) {
            currentHourIndex = i
            break
        }
    }

    if (currentHourIndex != -1) {
        // Заполняем dataList начиная с текущего часа, ограничивая до 24 часов
        val maxHoursToShow = minOf(currentHourIndex + 24, arrayLength)
        for (i in currentHourIndex until maxHoursToShow) {
            val timeFull = timeArray.getString(i)
            val time = timeFull.substring(timeFull.indexOf('T') + 1)
            val temp = tempArray.getDouble(i)
            val weatherIcon = weatherSmiley(weatherCodeArray.getInt(i), time, sunrise, sunset)
            dataList.add(HourlyDataClass(time, weatherIcon, temp))
        }
    }


    return dataList
}

fun convertJsonObjectToMainData(response: String, context: Context): MainDataClass {
    val mainObject = JSONObject(response)

    val hourly = mainObject.getJSONObject("hourly")
    val daily = mainObject.getJSONObject("daily")

    val sunrise = daily.getJSONArray("sunrise").getString(0)
    val sunset = daily.getJSONArray("sunset").getString(0)
    val uvIndex = daily.getJSONArray("uv_index_max").getDouble(0).toInt()


    val timeArray = hourly.getJSONArray("time")
    val arrayLength = timeArray.length()

    // Получаем текущее время
    val currentTime = Calendar.getInstance().time
    val dateFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    val currentTimeString = dateFormatter.format(currentTime)
    val currentHour = currentTimeString.substringBefore(":").toInt()


    // Находим индекс текущего часа
    var currentHourIndex = -1
    for (i in 0 until arrayLength) {
        val timeFull = timeArray.getString(i)
        val hour = timeFull.substring(timeFull.indexOf('T') + 1).substringBefore(":").toInt()
        if (hour == currentHour) {
            currentHourIndex = i
            break
        }
    }

    val timeFull = timeArray.getString(currentHourIndex)
    val hour = timeFull.substring(timeFull.indexOf('T') + 1)

    val dataList = MainDataClass(
        city = getCityNameFromCoordinates(context, latitudeLocal, longitudeLocal),
        temp = hourly.getJSONArray("temperature_2m").getDouble(currentHourIndex),
        minTemp = daily.getJSONArray("temperature_2m_min").getDouble(0),
        maxTemp = daily.getJSONArray("temperature_2m_max").getDouble(0),
        weatherDesc = weatherDescription(
            hourly.getJSONArray("weathercode").getInt(currentHourIndex), context
        ),
        humidity = hourly.getJSONArray("relativehumidity_2m").getInt(currentHourIndex),
        feelsLike = hourly.getJSONArray("apparent_temperature").getDouble(currentHourIndex),
        visibility = hourly.getJSONArray("visibility").getDouble(currentHourIndex),
        dewPoint = hourly.getJSONArray("dewpoint_2m").getDouble(currentHourIndex),
        sunrise = sunrise.substring(sunrise.indexOf('T') + 1),
        sunset = sunset.substring(sunset.indexOf('T') + 1),
        precipitation = hourly.getJSONArray("precipitation").getDouble(currentHourIndex),
        windSpeed = hourly.getJSONArray("windspeed_10m").getDouble(currentHourIndex),
        windDirection = getCompassDirection(
            hourly.getJSONArray("winddirection_10m").getInt(currentHourIndex), context
        ),
        UVIndex = uvIndex,
        UVDesc = getUVIndexLevel(uvIndex, context),
        backgroundColor = getBackgroundColorResource(
            weatherSmiley(
                hourly.getJSONArray("weathercode").getInt(currentHourIndex),
                hour,
                sunrise.substring(sunrise.indexOf('T') + 1),
                sunset.substring(sunset.indexOf('T') + 1)
            )
        ),
        itemsColor = getItemsColorResource(
            weatherSmiley(
                hourly.getJSONArray("weathercode").getInt(currentHourIndex),
                hour,
                sunrise.substring(sunrise.indexOf('T') + 1),
                sunset.substring(sunset.indexOf('T') + 1)
            )
        )
    )
    return dataList
}
