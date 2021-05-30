package fr.theomarie.weatherapp.models

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.drawable.Drawable
import com.google.gson.annotations.SerializedName
import fr.theomarie.weatherapp.R

data class Cweather (
    var temperature: Int,
    var weather_id: Int,
    var weather_main: String,
    var weather_description: String,
) {
    @SuppressLint("UseCompatLoadingForDrawables")
    fun getDrawableStatus(res: Resources) : Drawable {
        return when(weather_id) {
            in 200..232-> {
                res.getDrawable(R.drawable.cloud_thunder, res.newTheme())
            }
            in 300..321-> {
                res.getDrawable(R.drawable.cloud_rain, res.newTheme())
            }
            in 500..504 -> {
                res.getDrawable(R.drawable.cloud_rain_sun, res.newTheme())
            }
            511 -> {
                res.getDrawable(R.drawable.snow, res.newTheme())
            }
            in 520..531 -> {
                res.getDrawable(R.drawable.cloud_rain, res.newTheme())
            }
            in 600..622 -> {
                res.getDrawable(R.drawable.snow, res.newTheme())
            }
            in 701..781 -> {
                res.getDrawable(R.drawable.wind, res.newTheme())
            }
            in 801..804 -> {
                res.getDrawable(R.drawable.clouds, res.newTheme())
            }

            else -> res.getDrawable(R.drawable.sun, res.newTheme())
        }
    }
}
