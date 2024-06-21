package com.example.digiapp.ui.home.adapters

import SeriesViewHolder
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.digiapp.R
import com.example.digiapp.data.models.SeriesItemResultItem

class SeriesAdapter (private var series: List<SeriesItemResultItem>):
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

    }


    @SuppressLint("NotifyDataSetChanged")
    fun updateData(series: List<SeriesItemResultItem>){
        this.series = series
        notifyDataSetChanged()
    }

}