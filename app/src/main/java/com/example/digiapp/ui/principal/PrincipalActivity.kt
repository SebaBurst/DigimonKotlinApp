package com.example.digiapp.ui.principal

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.palette.graphics.Palette
import com.example.digiapp.R
import com.example.digiapp.databinding.ActivityPrincipalBinding
import com.example.digiapp.services.MusicService
import com.example.digiapp.ui.home.HomeFragment
import com.example.digiapp.util.Util
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class PrincipalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPrincipalBinding
    private var isMiniPlayerVisible = false
    private lateinit var miniPlayerImage: String
    private lateinit var miniPlayerTitle: String
    private lateinit var miniPlayerAuthor: String
    private lateinit var musicService: MusicService
    private var serviceBound = false
    private var isPlaying = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initMusicService()
        initUI()
        setupNavigation()
        initListeners()
    }


    //init music service
    private fun initMusicService(){
        // Iniciar el servicio de música
        val serviceIntent = Intent(this, MusicService::class.java)
        bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MusicService.MusicBinder
            musicService = binder.getService()
            serviceBound = true
            if (musicService.isPlaying){
                isPlaying = true
                binding.ivMiniPlay.setImageResource(R.drawable.ic_pause)
            }
            else{
                isPlaying = false
                binding.ivMiniPlay.setImageResource(R.drawable.ic_start)
            }

        }

        override fun onServiceDisconnected(name: ComponentName?) {
            serviceBound = true
        }
    }

    private fun initListeners() {
        binding.ivMiniPlay.setOnClickListener {
            if (isPlaying) {
                musicService.pauseMusic()
                isPlaying = false
                binding.ivMiniPlay.setImageResource(R.drawable.ic_start)

            } else {
                musicService.startMusic()
                isPlaying = true
                binding.ivMiniPlay.setImageResource(R.drawable.ic_pause)

            }

        }
    }



    private fun initUI(){
        initStatusBarColor()
        getMiniPlayerData()
        if (isMiniPlayerVisible){
            initMiniPlayer()
            binding.miniPlayer.visibility = View.VISIBLE
        }
        else{
            binding.miniPlayer.visibility = View.GONE
        }
    }

    private fun setupNavigation(){
        val bottomNavigationView = binding.bottomNav
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        NavigationUI.setupWithNavController(bottomNavigationView, navController)

    }




    private fun initMiniPlayer(){
        Picasso.get().load(miniPlayerImage).into(binding.ivMiniCover, object : Callback {
            override fun onSuccess() {
                initBackgroundColorMiniPlayer()
            }
            override fun onError(e: Exception?) {
                Toast.makeText(this@PrincipalActivity, "Error loading song cover", Toast.LENGTH_SHORT).show()
            }
        })

        binding.tvMiniSongName.text = miniPlayerTitle
        binding.tvMiniAuthor.text = miniPlayerAuthor
        updateSeekBarAndTime()

        binding.sliderTrackMini.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    musicService.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                musicService.pauseMusic()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                musicService.startMusic()
            }
        })
    }


    private fun updateSeekBarAndTime() {
        val handler = Handler(Looper.getMainLooper())
        handler.post(object : Runnable {
            override fun run() {
                if (serviceBound) {
                    binding.sliderTrackMini.progress = musicService.getMusicCurrentPosition()
                    binding.sliderTrackMini.max = musicService.getMusicDuration()
                }
                handler.postDelayed(this, 1000)
            }
        })
    }



    private fun initBackgroundColorMiniPlayer() {
        binding.ivMiniCover.drawable?.let {
            binding.miniPlayer.background = getDominantGradient(it)
        }

    }


    private fun getDominantGradient(image: Drawable): GradientDrawable {
        val imageCover = image.toBitmap()
        val palette = Palette.from(imageCover).generate()
        val darkVibrantColor = palette.getDarkVibrantColor(ContextCompat.getColor(this, R.color.semi_black))
        val mutatecolor = palette.getDarkMutedColor(ContextCompat.getColor(this, R.color.semi_black))
        val darkBackgroundColor = darkVibrantColor
        return GradientDrawable(
            GradientDrawable.Orientation.BR_TL,
            intArrayOf(mutatecolor, darkBackgroundColor)
        )
    }


    private fun getMiniPlayerData(){

        miniPlayerImage = intent.extras?.getString(MINI_PLAYER_IMAGE) ?: ""
        miniPlayerTitle = intent.extras?.getString(MINI_PLAYER_TITLE) ?: ""
        miniPlayerAuthor = intent.extras?.getString(MINI_PLAYER_AUTHOR) ?: ""
        isMiniPlayerVisible = intent.extras?.getBoolean(IS_MINI_PLAYER_VISIBLE, false) ?: false
    }


    /**
     * Initialize the status bar color
     */
    private fun initStatusBarColor(){
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)

    }

    companion object {
        const val MINI_PLAYER_IMAGE = "miniPlayerImage"
        const val MINI_PLAYER_TITLE = "miniPlayerTitle"
        const val MINI_PLAYER_AUTHOR = "miniPlayerAuthor"
        const val IS_MINI_PLAYER_VISIBLE = "isMiniPlayerVisible"
    }


}