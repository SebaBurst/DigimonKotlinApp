package com.example.digiapp.ui.music.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.digiapp.R
import com.example.digiapp.data.models.song.SongResultItem
import com.example.digiapp.ui.music.viewholders.SongViewHolder

class SongAdapter(private var songs: List<SongResultItem>,private var isPlayer: Boolean, private val onItemSelect: (Int) -> Unit) : RecyclerView.Adapter<SongViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return SongViewHolder(layoutInflater.inflate(R.layout.item_song, parent, false))
    }

    override fun onBindViewHolder(holder:SongViewHolder, position: Int) {
        holder.bind(songs[position], isPlayer)
        holder.itemView.setOnClickListener {
            onItemSelect(position)
        }
    }

    override fun getItemCount(): Int {
        return songs.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateSongs(newSongs: List<SongResultItem>) {
        songs = newSongs
        notifyDataSetChanged()
    }

    fun getSong(position: Int) = songs[position]

}