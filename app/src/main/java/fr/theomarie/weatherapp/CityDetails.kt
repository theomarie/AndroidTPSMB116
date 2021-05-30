package fr.theomarie.weatherapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import fr.theomarie.weatherapp.database.DataBase
import fr.theomarie.weatherapp.database.SharedPref
import fr.theomarie.weatherapp.models.Ccity
import fr.theomarie.weatherapp.models.Clocation
import fr.theomarie.weatherapp.models.Cweather
import fr.theomarie.weatherapp.utils.Constants
import fr.theomarie.weatherapp.utils.Functions
import kotlinx.android.synthetic.main.activity_city_details.*
import org.json.JSONException
import java.net.UnknownHostException

class CityDetails : AppCompatActivity() {

    lateinit var mainHandler: Handler

    private val updateTextTask = object : Runnable {
        override fun run() {
            updateWeatherData()
            mainHandler.postDelayed(this, 60000)
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_details)

        mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(updateTextTask)

        Functions.logi(Constants.currentCity)

        TV_city_details_name.text = Constants.currentCity!!.name
        IV_city_details_loved.apply {
            if (Constants.currentCity!!.love) {
                this.setImageDrawable(getDrawable(R.drawable.ic_round_favorite_24))
            }
            this.setOnClickListener {
                Constants.currentCity!!.love = !Constants.currentCity!!.love
                if (DataBase(this@CityDetails).updateLovedCity(Constants.currentCity!!)) {
                    if (Constants.currentCity!!.love) {
                        this.setImageDrawable(getDrawable(R.drawable.ic_round_favorite_24))
                    } else {
                        this.setImageDrawable(getDrawable(R.drawable.ic_round_favorite_border_24))
                    }
                } else {
                    Constants.currentCity!!.love = !Constants.currentCity!!.love
                    Toast.makeText(this@CityDetails, "An error has occurred, please try later", Toast.LENGTH_SHORT).show()
                }
            }
        }
        val favoriteCitiyName = SharedPref(this).loadFavoriteCityName()
        IV_city_details_favorite.apply {
            if (favoriteCitiyName == Constants.currentCity!!.name) {
                this.setImageDrawable(getDrawable(R.drawable.ic_round_star_24))
            }
            this.setOnClickListener {
                if (SharedPref(this@CityDetails).loadFavoriteCityName() != Constants.currentCity!!.name) {
                    if (SharedPref(this@CityDetails).setFavoriteCityName(Constants.currentCity!!.name)) {
                        this.setImageDrawable(getDrawable(R.drawable.ic_round_star_24))
                    } else {
                        this.setImageDrawable(getDrawable(R.drawable.ic_round_star_outline_24))
                    }
                } else {
                    SharedPref(this@CityDetails).deleteFavoriteCityName()
                    this.setImageDrawable(getDrawable(R.drawable.ic_round_star_outline_24))
                }
            }
        }
        TV_city_details_name.text = Constants.currentCity!!.name
        IV_city_details_map.setOnClickListener {
            Constants.MapsLocationInformations.latitude = Constants.currentCity!!.location!!.latitude
            Constants.MapsLocationInformations.longitude = Constants.currentCity!!.location!!.longitude
            Constants.MapsLocationInformations.name = Constants.currentCity!!.name
            startActivity(Intent(this, MapsActivity::class.java))
        }
        setWeatherData()
        IV_city_details_back.setOnClickListener {
            Constants.currentCity = null
            mainHandler.removeCallbacks(updateTextTask)
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    override fun onPause() {
        super.onPause()
        mainHandler.removeCallbacks(updateTextTask)
    }

    override fun onResume() {
        super.onResume()
        mainHandler.post(updateTextTask)
    }

    private fun updateWeatherData() {
        Volley.newRequestQueue(this).add(JsonObjectRequest(
            Request.Method.GET, "${Constants.NetworkService.BASE_URL_WITH_API_KEY}&q=${Constants.currentCity!!.name}&units=metric", null,
            { response ->
                try {
                    Functions.logi(response)
                    when(response.getInt("cod")) {
                        200 -> {
                            val jsonWeather = response.getJSONArray("weather").getJSONObject(0)
                            val city = Ccity(
                                Constants.currentCity!!.id,
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
                                    response.getJSONObject("main").getDouble("temp"),
                                    response.getJSONObject("main").getDouble("temp")
                                )
                            )
                            Constants.currentCity = city
                            setWeatherData()
                        }
                        404 -> {
                            Toast.makeText(this, "The request return 404 error code.", Toast.LENGTH_SHORT).show()
                        }
                        500 -> {
                            Toast.makeText(this, "The request return 500 error code.", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: JSONException) {
                    Functions.loge(e)
                    Functions.loge(e.stackTrace)
                    Toast.makeText(this, "The JSON object returned by API is incorrect", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Functions.loge(e)
                    Functions.loge(e.stackTrace)
                    Toast.makeText(this, "Unknown error has occurred when received data", Toast.LENGTH_SHORT).show()
                }
            },
            { e ->
                Functions.loge(e)
                Functions.loge(e.stackTrace)
                if (e.cause is UnknownHostException) {
                    Toast.makeText(this, "No connection available at the moment", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Unknown error has occurred", Toast.LENGTH_SHORT).show()
                }
            }
        ))
    }

    @SuppressLint("SetTextI18n")
    private fun setWeatherData() {
        if (Constants.currentCity!!.weather != null) {
            IV_city_details_weather.setImageDrawable(Constants.currentCity!!.weather!!.getDrawableStatus(resources))
            TV_city_details_temperature.text = "${Constants.currentCity!!.weather!!.temperature}°C"
        }
    }

    override fun onBackPressed() {
        Constants.currentCity = null
        mainHandler.removeCallbacks(updateTextTask)
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}