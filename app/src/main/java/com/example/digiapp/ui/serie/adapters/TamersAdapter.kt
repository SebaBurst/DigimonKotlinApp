package com.example.digiapp.ui.serie.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.digiapp.data.models.tamers.TamersResultItem
import com.example.digiapp.ui.serie.viewholders.TamersViewHolder
import com.example.digiapp.R

class TamersAdapter(private var tamers: List<TamersResultItem>) : RecyclerView.Adapter<TamersViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TamersViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return TamersViewHolder(layoutInflater.inflate(R.layout.item_tamer, parent, false))

    }

    override fun onBindViewHolder(holder: TamersViewHolder, position: Int) {
        holder.bind(tamers[position])
    }

    override fun getItemCount(): Int {
        return tamers.size
    }

    fun getTamer(position: Int) = tamers[position]

    @SuppressLint("NotifyDataSetChanged")
    fun updateTamers(newTamers: List<TamersResultItem>){
        tamers = newTamers
        notifyDataSetChanged()
    }

}