package fr.theomarie.weatherapp.database

import android.annotation.SuppressLint
import android.content.Context


@SuppressLint("CommitPrefEdits")
class SharedPref(val context: Context) {
    val sharedPreferences = context.getSharedPreferences("pref", Context.MODE_PRIVATE)

    fun setFavoriteCityName(cityName: String): Boolean = sharedPreferences.edit().putString(favoriteCityName, cityName).commit()
    fun loadFavoriteCityName(): String? = sharedPreferences.getString(favoriteCityName, null)
    fun favoriteCityNameExist(): Boolean = sharedPreferences.contains(favoriteCityName)
    fun deleteFavoriteCityName(): Boolean = sharedPreferences.edit().remove(favoriteCityName).commit()

    companion object {
        const val favoriteCityName ="favorite_city_name"
    }
}