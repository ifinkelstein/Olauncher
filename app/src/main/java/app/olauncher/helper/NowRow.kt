package app.olauncher.helper

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.provider.CalendarContract
import androidx.core.content.ContextCompat
import app.olauncher.data.Prefs
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

private const val WEATHER_CACHE_MILLIS = 30 * 60 * 1000L

fun Context.hasCalendarPermission(): Boolean =
    ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED

fun Context.hasLocationPermission(): Boolean =
    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

// Next calendar event between now and end of day, e.g. "14:30 Lab meeting"
fun Context.getNextEventToday(): String? {
    if (hasCalendarPermission().not()) return null
    return try {
        val now = System.currentTimeMillis()
        val endOfDay = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        val projection = arrayOf(
            CalendarContract.Instances.BEGIN,
            CalendarContract.Instances.TITLE,
            CalendarContract.Instances.ALL_DAY,
        )
        val uri = CalendarContract.Instances.CONTENT_URI.buildUpon()
            .appendPath(now.toString())
            .appendPath(endOfDay.toString())
            .build()

        contentResolver.query(uri, projection, null, null, CalendarContract.Instances.BEGIN)?.use { cursor ->
            while (cursor.moveToNext()) {
                if (cursor.getInt(2) == 1) continue // skip all-day events
                val begin = cursor.getLong(0)
                if (begin < now) continue
                val title = cursor.getString(1).orEmpty().ifEmpty { return@use null }
                val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                return timeFormat.format(Date(begin)) + "  " + title
            }
            null
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

// Current weather via Open-Meteo (no API key), cached for 30 minutes
fun Context.getWeatherNow(prefs: Prefs): String? {
    if (hasLocationPermission().not()) return null
    val cachedAt = prefs.nowWeatherCachedAt
    if (System.currentTimeMillis() - cachedAt < WEATHER_CACHE_MILLIS && prefs.nowWeatherCache.isNotEmpty())
        return prefs.nowWeatherCache
    return try {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        @Suppress("MissingPermission")
        val location = listOf(
            LocationManager.NETWORK_PROVIDER,
            LocationManager.PASSIVE_PROVIDER,
            LocationManager.GPS_PROVIDER
        ).firstNotNullOfOrNull { provider ->
            try {
                locationManager.getLastKnownLocation(provider)
            } catch (e: Exception) {
                null
            }
        } ?: return null

        val fahrenheit = Locale.getDefault().country.equals("US", ignoreCase = true)
        val unitParam = if (fahrenheit) "&temperature_unit=fahrenheit" else ""
        val url = "https://api.open-meteo.com/v1/forecast" +
                "?latitude=${location.latitude}&longitude=${location.longitude}" +
                "&current_weather=true&daily=temperature_2m_max,temperature_2m_min" +
                "&forecast_days=1&timezone=auto$unitParam"

        val connection = URL(url).openConnection() as HttpURLConnection
        connection.connectTimeout = 5000
        connection.readTimeout = 5000
        val response = connection.inputStream.bufferedReader().use { it.readText() }
        connection.disconnect()

        val json = JSONObject(response)
        val current = json.getJSONObject("current_weather")
        val temp = current.getDouble("temperature").roundToInt()
        val code = current.getInt("weathercode")
        val daily = json.getJSONObject("daily")
        val min = daily.getJSONArray("temperature_2m_min").getDouble(0).roundToInt()
        val max = daily.getJSONArray("temperature_2m_max").getDouble(0).roundToInt()

        val text = "$temp° · $min–$max° · ${weatherCodeToText(code)}"
        prefs.nowWeatherCache = text
        prefs.nowWeatherCachedAt = System.currentTimeMillis()
        text
    } catch (e: Exception) {
        e.printStackTrace()
        prefs.nowWeatherCache.ifEmpty { null }
    }
}

private fun weatherCodeToText(code: Int): String = when (code) {
    0 -> "Clear"
    1, 2 -> "Partly cloudy"
    3 -> "Overcast"
    45, 48 -> "Fog"
    in 51..57 -> "Drizzle"
    in 61..67 -> "Rain"
    in 71..77 -> "Snow"
    in 80..82 -> "Showers"
    85, 86 -> "Snow showers"
    in 95..99 -> "Thunderstorm"
    else -> "—"
}
