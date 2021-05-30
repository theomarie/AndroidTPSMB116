package fr.theomarie.weatherapp.utils

import fr.theomarie.weatherapp.models.Ccity
import fr.theomarie.weatherapp.models.Clocation

object Constants {

    object NetworkService {
        const val BASE_URL = "https://api.openweathermap.org/data/2.5/weather?"
        const val API_KEY_VALUE = "9992842831e8e4560d87145df8552e65"
        const val BASE_URL_WITH_API_KEY = "${BASE_URL}appid=$API_KEY_VALUE&"
    }

    var currentLocation :Clocation? = null

    var favoriteCity :Ccity? = null

    var currentCity :Ccity? = null

    val defaultCity :Ccity = Ccity(0, "Paris", "Ile-de-france", "France", null, Clocation(48.8534, 2.3488))

    object MapsLocationInformations {
        var latitude = 0.0
        var longitude = 0.0
        var name = "City name"
    }
}