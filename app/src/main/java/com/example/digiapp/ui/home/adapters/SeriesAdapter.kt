package com.example.digiapp.ui.home.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.digiapp.R
import com.example.digiapp.data.models.serie.SeriesItemResultItem

class SeriesAdapter (private var series: List<SeriesItemResultItem> , private val onItemSelect: (Int) -> Unit):
    RecyclerView.Adapter<SeriesViewHolder>() {

    //create the view holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeriesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return SeriesViewHolder(layoutInflater.inflate(R.layout.item_series, parent, false))
    }

    override fun getItemCount(): Int {
        return series.size
    }

    override fun onBindViewHolder(holder: SeriesViewHolder, position: Int) {
        holder.bind(series[position])
        holder.itemView.setOnClickListener {
            onItemSelect(position)
        }

    }
    @SuppressLint("NotifyDataSetChanged")
    fun updateData(series: List<SeriesItemResultItem>){
        this.series = series
        notifyDataSetChanged()
    }


    fun getSeries(position: Int) = series[position]

}