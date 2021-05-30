package fr.theomarie.weatherapp.models

data class Ccity(
    var id: Int = 0,
    var name: String = "",
    var subcountry: String = "",
    var country: String = "",
    var weather: Cweather? = null,
    var location: Clocation? = null,
    var love: Boolean = false,
) {
    override fun toString(): String {
        return "Ccity(id='$id', name='$name', subcountry='$subcountry', country='$country', weather=$weather, location=$location, love=$love)"
    }
}
