package fr.theomarie.weatherapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import fr.theomarie.weatherapp.adapters.AdapterListFavoritesCities
import fr.theomarie.weatherapp.database.DataBase
import fr.theomarie.weatherapp.database.SharedPref
import fr.theomarie.weatherapp.models.Ccity
import fr.theomarie.weatherapp.models.Clocation
import fr.theomarie.weatherapp.models.Cweather
import fr.theomarie.weatherapp.utils.Constants
import fr.theomarie.weatherapp.utils.Functions
import fr.theomarie.weatherapp.utils.Functions.capitalizeWords
import fr.theomarie.weatherapp.utils.Functions.getLovedCities
import fr.theomarie.weatherapp.utils.Functions.loge
import fr.theomarie.weatherapp.utils.Functions.logi
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONException
import java.net.UnknownHostException


class MainActivity : AppCompatActivity(), AdapterListFavoritesCities.OnCityClickListener {

    private var lovedCities = arrayListOf<Ccity>()

    lateinit var mainHandler: Handler

    private val updateTextTask = object : Runnable {
        override fun run() {
            updateWeatherDataForLovedCities()
            mainHandler.postDelayed(this, 600000)
        }
    }

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (DataBase(this).getAllCity().size == 0) {
            Functions.resetCities(this)
        }

        mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(updateTextTask)

        lovedCities = getLovedCities(this)
        logi(lovedCities)

        val lm: LocationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var gpsEnabled = false
        var networkEnabled = false

        try {
            gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (e: Exception) { loge(e) }

        try {
            networkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (e: Exception) { loge(e) }

        if (gpsEnabled && networkEnabled) {
            logi("gps and network is enable")
            LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(LocationRequest.create().apply {
                this.interval = 10000L
                this.fastestInterval = 1000L
                this.smallestDisplacement = 100F
                this.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }, object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    for (location in locationResult.locations) {
                        if (location != null) {
                            Constants.currentLocation = Clocation(location.latitude, location.longitude)
                            updateCurrentPosition(Constants.currentLocation!!)
                            logi("locationResult.locations is ${Constants.currentLocation}")
                        } else {
                            Constants.currentLocation = null
                            loge("locationResult.locations is null")
                        }
                    }
                }
            }, mainLooper)
        } else {
            logi("gps and network is disable")
            val favoriteCityName = SharedPref(this).loadFavoriteCityName()
            if (favoriteCityName != null) {
                Volley.newRequestQueue(this).add(JsonObjectRequest(
                    Request.Method.GET, "${Constants.NetworkService.BASE_URL_WITH_API_KEY}&q=${favoriteCityName}&units=metric", null,
                    { response ->
                        try {
                            logi(response)
                            when(response.getInt("cod")) {
                                200 -> {
                                    val jsonWeather = response.getJSONArray("weather").getJSONObject(0)
                                    val city = Ccity(
                                        0,
                                        response.getString("name"),
                                        "",
                                        "",
                                        Cweather(
                                            response.getJSONObject("main").getDouble("temp").toInt(),
                                            jsonWeather.getInt("id"),
                                            jsonWeather.getString("main"),
                                            jsonWeather.getString("description")
                                        ),
                                        Clocation(
                                            response.getJSONObject("coord").getDouble("lat"),
                                            response.getJSONObject("coord").getDouble("lon")
                                        )
                                    )
                                    Constants.favoriteCity = city
                                    setFavoriteCityData()
                                }
                                404 -> {
                                    Toast.makeText(this, "The request return 404 error code.", Toast.LENGTH_SHORT).show()
                                }
                                500 -> {
                                    Toast.makeText(this, "The request return 500 error code.", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } catch (e: JSONException) {
                            loge(e)
                            loge(e.stackTrace)
                            Toast.makeText(this, "The JSON object returned by API is incorrect", Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) {
                            loge(e)
                            loge(e.stackTrace)
                            Toast.makeText(this, "Unknown error has occurred when received data", Toast.LENGTH_SHORT).show()
                        }
                    },
                    { e ->
                        loge(e)
                        loge(e.stackTrace)
                        if (e.cause is UnknownHostException) {
                            Toast.makeText(this, "No connection available at the moment", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Unknown error has occurred", Toast.LENGTH_SHORT).show()
                        }
                    }
                ))
            } else {
                Volley.newRequestQueue(this).add(JsonObjectRequest(
                    Request.Method.GET, "${Constants.NetworkService.BASE_URL_WITH_API_KEY}&lat=${Constants.defaultCity.location!!.latitude}&lon=${Constants.defaultCity.location!!.longitude}&units=metric", null,
                    { response ->
                        try {
                            logi(response)
                            when(response.getInt("cod")) {
                                200 -> {
                                    val jsonWeather = response.getJSONArray("weather").getJSONObject(0)
                                    Constants.defaultCity.weather = Cweather(
                                        response.getJSONObject("main").getDouble("temp").toInt(),
                                        jsonWeather.getInt("id"),
                                        jsonWeather.getString("main"),
                                        jsonWeather.getString("description")
                                    )
                                    Constants.favoriteCity = Constants.defaultCity
                                    setFavoriteCityData()
                                }
                                404 -> {
                                    Toast.makeText(this, "The request return 404 error code.", Toast.LENGTH_SHORT).show()
                                }
                                500 -> {
                                    Toast.makeText(this, "The request return 500 error code.", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } catch (e: JSONException) {
                            loge(e)
                            loge(e.stackTrace)
                            Toast.makeText(this, "The JSON object returned by API is incorrect", Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) {
                            loge(e)
                            loge(e.stackTrace)
                            Toast.makeText(this, "Unknown error has occurred when received data", Toast.LENGTH_SHORT).show()
                        }
                    },
                    { e ->
                        loge(e)
                        loge(e.stackTrace)
                        if (e.cause is UnknownHostException) {
                            Toast.makeText(this, "No connection available at the moment", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Unknown error has occurred", Toast.LENGTH_SHORT).show()
                        }
                    }
                ))
            }
        }



        RV_main_activity_favorites_cities.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        RV_main_activity_favorites_cities.adapter = AdapterListFavoritesCities(lovedCities, resources, this)

        IV_main_search.setOnClickListener {
            startActivity(Intent(this, SearchCities::class.java))
            finish()
        }
    }

    private fun updateCurrentPosition(location: Clocation) {
        logi(location)
        Volley.newRequestQueue(this).add(JsonObjectRequest(
            Request.Method.GET, "${Constants.NetworkService.BASE_URL_WITH_API_KEY}&lat=${location.latitude}&lon=${location.longitude}&units=metric", null,
            { response ->
                try {
                    logi(response)
                    when(response.getInt("cod")) {
                        200 -> {
                            val jsonWeather = response.getJSONArray("weather").getJSONObject(0)
                            val city = Ccity(
                                0,
                                response.getString("name"),
                                "",
                                "",
                                Cweather(
                                    response.getJSONObject("main").getDouble("temp").toInt(),
                                    jsonWeather.getInt("id"),
                                    jsonWeather.getString("main"),
                                    jsonWeather.getString("description")
                                ),
                                location
                            )
                            Constants.favoriteCity = city
                            setFavoriteCityData()
                        }
                        404 -> {
                            Toast.makeText(this, "The request return 404 error code.", Toast.LENGTH_SHORT).show()
                        }
                        500 -> {
                            Toast.makeText(this, "The request return 500 error code.", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: JSONException) {
                    loge(e)
                    loge(e.stackTrace)
                    Toast.makeText(this, "The JSON object returned by API is incorrect", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    loge(e)
                    loge(e.stackTrace)
                    Toast.makeText(this, "Unknown error has occurred when received data", Toast.LENGTH_SHORT).show()
                }
            },
            { e ->
                loge(e)
                loge(e.stackTrace)
                if (e.cause is UnknownHostException) {
                    Toast.makeText(this, "No connection available at the moment", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Unknown error has occurred", Toast.LENGTH_SHORT).show()
                }
            }
        ))
    }

    @SuppressLint("SetTextI18n")
    private fun setFavoriteCityData() {
        logi("update favorite city")
        IV_main_favorite_city_weather.setImageDrawable(Constants.favoriteCity!!.weather!!.getDrawableStatus(resources))
        TV_main_favorite_city_name.text = Constants.favoriteCity!!.name.capitalizeWords()
        TV_main_favorite_city_temperature.text = "${Constants.favoriteCity!!.weather!!.temperature}°C"
        IV_main_favorite_city_map.setOnClickListener {
            Constants.MapsLocationInformations.latitude = Constants.favoriteCity!!.location!!.latitude
            Constants.MapsLocationInformations.longitude = Constants.favoriteCity!!.location!!.longitude
            Constants.MapsLocationInformations.name = Constants.favoriteCity!!.name
            startActivity(Intent(this, MapsActivity::class.java))
        }
    }

    private fun updateWeatherDataForLovedCities() {
        lovedCities.forEach {
            Volley.newRequestQueue(this).add(JsonObjectRequest(
                Request.Method.GET, "${Constants.NetworkService.BASE_URL_WITH_API_KEY}&q=${it.name}&units=metric", null,
                { response ->
                    try {
                        logi(response)
                        when(response.getInt("cod")) {
                            200 -> {
                                val jsonWeather = response.getJSONArray("weather").getJSONObject(0)
                                it.weather = Cweather(
                                    response.getJSONObject("main").getDouble("temp").toInt(),
                                    jsonWeather.getInt("id"),
                                    jsonWeather.getString("main"),
                                    jsonWeather.getString("description")
                                )
                                RV_main_activity_favorites_cities.adapter!!.notifyDataSetChanged()
                            }
                            404 -> {
                                Toast.makeText(this, "The request return 404 error code.", Toast.LENGTH_SHORT).show()
                            }
                            500 -> {
                                Toast.makeText(this, "The request return 500 error code.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } catch (e: JSONException) {
                        loge(e)
                        loge(e.stackTrace)
                        Toast.makeText(this, "The JSON object returned by API is incorrect", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        loge(e)
                        loge(e.stackTrace)
                        Toast.makeText(this, "Unknown error has occurred when received data", Toast.LENGTH_SHORT).show()
                    }
                },
                { e ->
                    loge(e)
                    loge(e.stackTrace)
                    if (e.cause is UnknownHostException) {
                        Toast.makeText(this, "No connection available at the moment", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Unknown error has occurred", Toast.LENGTH_SHORT).show()
                    }
                }
            ))
        }
    }

    override fun onCityClick(position: Int) {
        Constants.currentCity = lovedCities[position]
        logi(Constants.currentCity)
        startActivity(Intent(this, CityDetails::class.java))
        finish()
    }
}