package fr.theomarie.weatherapp.adapters

import android.annotation.SuppressLint
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.theomarie.weatherapp.R
import fr.theomarie.weatherapp.models.Ccity
import kotlinx.android.synthetic.main.cell_list_favorite_city.view.*

class AdapterListFavoritesCities (
    private val listCities: ArrayList<Ccity>,
    private val resources: Resources,
    OnCityClickListener: OnCityClickListener
) : RecyclerView.Adapter<AdapterListFavoritesCities.MyViewHolder>() {

    private var mOnClickListener: OnCityClickListener? = OnCityClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.cell_list_favorite_city, parent, false)

        return MyViewHolder(itemView, mOnClickListener!!)
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.name.text = listCities[position].name
        if (listCities[position].weather != null) {
            holder.temperature.text = "${listCities[position].weather!!.temperature}°C"
            holder.status.setImageDrawable(listCities[position].weather!!.getDrawableStatus(resources))
        } else {
            holder.temperature.text = "Loading..."
            holder.status.setImageDrawable(resources.getDrawable(R.drawable.icon_without_background, resources.newTheme()))
        }
    }

    override fun getItemCount(): Int {
        return listCities.size
    }

    inner class MyViewHolder(itemView: View, OnCityClickListener: OnCityClickListener) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val name: TextView =itemView.TV_cell_list_favorite_city_name
        val temperature: TextView =itemView.TV_cell_list_favorite_city_temperature
        val status: ImageView = itemView.IV_cell_list_favorite_city_status


        private var OnCityClickListener : OnCityClickListener? = null
        init {
            this.OnCityClickListener =OnCityClickListener
            itemView.tag = this
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            OnCityClickListener!!.onCityClick(adapterPosition)
        }
    }
    interface OnCityClickListener{
        fun onCityClick(position: Int)
    }
}