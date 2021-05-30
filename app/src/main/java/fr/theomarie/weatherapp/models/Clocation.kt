package fr.theomarie.weatherapp.models

class Clocation(
    var latitude: Double,
    var longitude: Double
) {

    override fun toString(): String {
        return "Clocation(latitude=$latitude, longitude=$longitude)"
    }
}