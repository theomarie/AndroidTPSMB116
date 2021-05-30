package fr.theomarie.weatherapp.utils

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fr.theomarie.weatherapp.database.DataBase
import fr.theomarie.weatherapp.models.Ccity
import java.util.*
import kotlin.collections.ArrayList

object Functions {

    fun getCitiesJSON(context: Context): ArrayList<Ccity> {
        val json = context.assets.open("cities.json").bufferedReader().use { it.readText() }
        val sType = object : TypeToken<List<Ccity>>() { }.type
        return Gson().fromJson(json, sType)
    }

    fun getCities(context: Context): ArrayList<Ccity> {
        return DataBase(context).getAllCity()
    }
    fun getLovedCities(context: Context): ArrayList<Ccity> {
        return DataBase(context).getAllLovedCity()
    }

    fun getCitiesByName(context: Context, name: String): ArrayList<Ccity> {
        return DataBase(context).getAllCity(name)
    }

    fun getCity(context: Context, name: String): Ccity? {
        return DataBase(context).getOneCity(name)
    }

    fun resetCities(context: Context) {
        getCitiesJSON(context).forEach { city ->
            DataBase(context).addCity(city)
        }
    }

    fun getUrlFindWeatherByName(name: String): String = "${Constants.NetworkService.BASE_URL_WITH_API_KEY}&weather"

    fun String.capitalizeWords(): String = split(" ").joinToString(" ") { it.capitalize(Locale.ROOT) }

    fun logi(text: Any?) {
        if (text != null) {
            Log.i("//////////", text.toString())
        } else {
            Log.e("//////////", "Log text is null")
        }
    }
    fun logd(text: Any?) {
        if (text != null) {
            Log.d("//////////", text.toString())
        } else {
            Log.e("//////////", "Log text is null")
        }
    }
    fun loge(text: Any?) {
        if (text != null) {
            Log.e("//////////", text.toString())
        } else {
            Log.e("//////////", "Log text is null")
        }
    }
}