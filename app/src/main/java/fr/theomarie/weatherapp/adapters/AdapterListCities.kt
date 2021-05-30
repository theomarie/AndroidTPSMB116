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
import kotlinx.android.synthetic.main.cell_list_city.view.*

class AdapterListCities (
    private val listCities: ArrayList<Ccity>,
    private val resources: Resources,
    OnCityClickListener: OnCityClickListener
) : RecyclerView.Adapter<AdapterListCities.MyViewHolder>() {

    private var mOnClickListener: OnCityClickListener? = OnCityClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.cell_list_city, parent, false)

        return MyViewHolder(itemView, mOnClickListener!!)
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.name.text = listCities[position].name
        if (position == itemCount-1) {
            holder.divider.visibility = View.GONE
        } else {
            holder.divider.visibility = View.VISIBLE
        }
    }

    override fun getItemCount(): Int {
        return listCities.size
    }

    inner class MyViewHolder(itemView: View, OnCityClickListener: OnCityClickListener) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val name: TextView =itemView.TV_cell_list_city_name
        val divider: View = itemView.V_cell_list_city_divider


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