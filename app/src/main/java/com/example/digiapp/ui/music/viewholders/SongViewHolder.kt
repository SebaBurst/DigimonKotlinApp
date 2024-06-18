package com.example.digiapp.ui.music.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.digiapp.data.models.song.SongResultItem
import com.example.digiapp.databinding.ItemSongBinding
import com.squareup.picasso.Picasso

class SongViewHolder(view: View) : RecyclerView.ViewHolder(view){
    private val binding = ItemSongBinding.bind(view)

    fun bind(song: SongResultItem, isPlayer: Boolean){
        binding.tvSongNameIcon.text= song.title
        binding.tvSongAuthorIcon.text = song.artist
        Picasso.get().load(song.coverImage).into(binding.ivSongCoverIcon)

        if(isPlayer){
            binding.tvSongNameIcon.setTextColor(itemView.resources.getColor(android.R.color.white))
            binding.tvSongAuthorIcon.setTextColor(itemView.resources.getColor(android.R.color.white))
        }
    }
}