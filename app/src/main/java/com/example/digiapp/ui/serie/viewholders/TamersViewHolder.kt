package com.example.digiapp.ui.serie.viewholders

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.example.digiapp.R
import com.example.digiapp.data.models.tamers.TamersResultItem
import com.example.digiapp.databinding.ItemTamerBinding
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target

class TamersViewHolder(view: View) : RecyclerView.ViewHolder(view){
    private val binding = ItemTamerBinding.bind(view)

    fun bind(tamer: TamersResultItem){
        Picasso.get().load(tamer.icon).into(object : Target {
            override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {
                binding.tamerIcon.setImageBitmap(bitmap)
                // Set gradient background after image is loaded
                val colors = getDominantGradient(bitmap)
                val gradientDrawable = GradientDrawable(GradientDrawable.Orientation.BR_TL, colors)
                gradientDrawable.cornerRadius = 20f  // Set corner radius for gradient drawable
                binding.tamerCard.setBackgroundDrawable(gradientDrawable)
                binding.tamerCard.strokeWidth = 0
            }

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                // Handle the error
                errorDrawable?.let {
                    binding.tamerIcon.setImageDrawable(it)
                }
                // Add error message here
                Toast.makeText(binding.root.context, "Error loading series icon", Toast.LENGTH_SHORT).show()
            }

            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                // Handle the placeholder
                placeHolderDrawable?.let {
                    binding.tamerIcon.setImageDrawable(it)
                }
            }
        })
        binding.tamerNameCard.text = tamer.name
    }

    private fun getDominantGradient(bitmap: Bitmap): IntArray {
        val palette = Palette.from(bitmap).generate()
        val dominantColor = palette.getDominantColor(ContextCompat.getColor(binding.root.context, R.color.white))
        val lightMutedColor = palette.getLightMutedColor(ContextCompat.getColor(binding.root.context, R.color.white))
        return intArrayOf(dominantColor, lightMutedColor)
    }



}