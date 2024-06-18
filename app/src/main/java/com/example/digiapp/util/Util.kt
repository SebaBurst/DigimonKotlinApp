package com.example.digiapp.util

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import com.example.digiapp.R

object Util {
    //convert time from milliseconds to minutes and seconds
    fun convertTime(mill: Int): String {
        val seconds = mill / 1000
        val minutes = seconds / 60
        val secondsDisplay = seconds % 60
        val minutesDisplay = minutes % 60
        return String.format("%02d:%02d", minutesDisplay, secondsDisplay)
    }

    //set status bar color
    private fun setStatusBarColor(activity: Activity, color: Int) {
        activity.window.statusBarColor = ContextCompat.getColor(activity.baseContext, color)
    }

    //get color from image
    fun getColorFromImage(image: Drawable, activity: Activity): GradientDrawable {
        val imageCover = image.toBitmap()
        val defaultColor = ContextCompat.getColor(activity.baseContext, R.color.white)
        val palette = Palette.from(imageCover).generate()
        val dominantColor = palette.getDominantColor(defaultColor)
        val darkBackgroundColor = darkenColor(dominantColor, activity.baseContext)
        //setStatusBarColor(activity, darkBackgroundColor)
        // Se Crea un fondo degradado con los colores de la imagen
        return GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(darkBackgroundColor, darkenColor(dominantColor, activity.baseContext))
        )
    }

    private fun darkenColor(color: Int, context: Context): Int {
        val factor = 0.7f
        return ColorUtils.blendARGB(color, ContextCompat.getColor(context,R.color.semi_black), factor)
    }
}