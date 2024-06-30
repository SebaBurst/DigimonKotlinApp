package com.example.digiapp.ui.serie

import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.digiapp.R
import com.example.digiapp.databinding.ActivitySerieInfoBinding
import com.example.digiapp.ui.serie.adapters.TamersAdapter
import com.example.digiapp.ui.serie.viewmodels.TamersViewModel
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SeriesInfoActivity : AppCompatActivity() {

    // Init the binding and variables
    private lateinit var binding: ActivitySerieInfoBinding
    private  var seriesId: Int =-1
    private lateinit var seriesLogo: String
    private lateinit var seriesName: String
    private lateinit var seriesSinopsis: String
    private lateinit var seriesBanner: String
    private lateinit var tamersAdapter: TamersAdapter

    private val viewModel: TamersViewModel by viewModels()


    /**
     * This function will create the view
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySerieInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.fetchTamers()
        getSeasonDetails()
        initTamers()
        initUI()
    }


    /**
     * This function will initialize the UI of the activity
     */
    private fun initUI(){
        binding.seriesSinopsis.text = seriesSinopsis //set the series sinopsis in the text view
        Picasso.get().load(seriesBanner).into(binding.seriesImageLogo, object : Callback {
            override fun onSuccess() {
                initBackgroundColor()
            }

            override fun onError(e: Exception?) {
                Toast.makeText(this@SeriesInfoActivity, "Error loading  image", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }


    private fun initTamers(){
        tamersAdapter = TamersAdapter(listOf())
        val layoutManager = GridLayoutManager(this, 2)
        binding.tamersRecyclerView.layoutManager = layoutManager
        binding.tamersRecyclerView.adapter = tamersAdapter
        observeTamers()
    }

    private fun observeTamers(){
        viewModel.tamers.observe(this) { tamers ->
            val filterResponse = tamers.filter { it.serieid == seriesId }
            tamersAdapter.updateTamers(filterResponse)
        }
    }


    /**
     * This function will change the status bar color based on the image of banner
     * @see Drawable
     * @see Palette
     */
    private fun initBackgroundColor(){
        binding.seriesImageLogo.drawable?.let {
            changeWindowStatusBar(it)
        }

    }


    /**
     * This function will change the status bar color based on the image of banner
     * @param image
     * @see Drawable
     * @see Palette
     */
    private fun changeWindowStatusBar(image: Drawable){
        val imageCover = image.toBitmap()
        val palette = Palette.from(imageCover).generate()
        val darkVibrantColor =
            palette.getDarkVibrantColor(ContextCompat.getColor(this, R.color.semi_black))
        window.statusBarColor = darkVibrantColor
    }


    /**
     * This function will get the details of the series from the intent extras from HomeFragment
     * @see Intent
     */
    private fun getSeasonDetails(){
        seriesSinopsis = intent.extras?.getString("seriesDescription") ?: ""
        seriesName = intent.extras?.getString("series") ?: ""
        seriesLogo = intent.extras?.getString("seriesImage") ?: ""
        seriesId = intent.extras?.getInt("seriesId") ?: -1
        seriesBanner = intent.extras?.getString("seriesBanner") ?: ""
    }
}