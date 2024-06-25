package com.example.digiapp.ui.serie

import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.palette.graphics.Palette
import com.example.digiapp.R
import com.example.digiapp.databinding.ActivitySerieInfoBinding
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SeriesInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySerieInfoBinding
    private  var seriesId: Int =-1
    private lateinit var seriesLogo: String
    private lateinit var seriesName: String
    private lateinit var seriesSinopsis: String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySerieInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getSeasonDetails()
        initUI()
    }


    private fun initUI(){
        binding.seriesSinopsis.text = seriesSinopsis
        Picasso.get().load(seriesLogo).into(binding.seriesImageLogo, object : Callback {
            override fun onSuccess() {
                initBackgroundColor()
            }

            override fun onError(e: Exception?) {
                Toast.makeText(this@SeriesInfoActivity, "Error loading song cover", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }


    private fun initBackgroundColor(){
        binding.seriesImageLogo.drawable?.let {
            binding.seriesInfoName.background = getDominantColor(it)
        }

    }



    private fun getDominantColor(image: Drawable): GradientDrawable {
        val imageCover = image.toBitmap()
        val palette = Palette.from(imageCover).generate()
        val mutatecolor =
            palette.getDarkMutedColor(ContextCompat.getColor(this, R.color.semi_black))
        window.statusBarColor = mutatecolor
        return GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(mutatecolor, mutatecolor)
        )
    }

    private fun getSeasonDetails(){
        seriesSinopsis = intent.extras?.getString("seriesDescription") ?: ""
        seriesName = intent.extras?.getString("series") ?: ""
        seriesLogo = intent.extras?.getString("seriesImage") ?: ""
        seriesId = intent.extras?.getInt("seriesId") ?: -1
    }
}