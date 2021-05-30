package fr.theomarie.weatherapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import fr.theomarie.weatherapp.adapters.AdapterListCities
import fr.theomarie.weatherapp.database.DataBase
import fr.theomarie.weatherapp.models.Ccity
import fr.theomarie.weatherapp.utils.Constants
import fr.theomarie.weatherapp.utils.Functions
import kotlinx.android.synthetic.main.activity_search_cities.*

class SearchCities : AppCompatActivity(), AdapterListCities.OnCityClickListener {

    private var cities = arrayListOf<Ccity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_cities)


        RV_search_cities_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        RV_search_cities_list.adapter = AdapterListCities(cities, resources, this)

        val database = DataBase(this)

        ET_search_cities_search.doOnTextChanged { text, _, _, _ ->
            Functions.logi("search $text")
            cities = database.getAllCity(text.toString())
            Functions.logi("result ::: $cities")
            RV_search_cities_list.adapter = AdapterListCities(cities, resources, this)
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onCityClick(position: Int) {
        Constants.currentCity = cities[position]
        startActivity(Intent(this, CityDetails::class.java))
        finish()
    }
}