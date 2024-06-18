package com.example.digiapp.ui.player

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.SeekBar
import android.widget.Toast
import android.window.OnBackInvokedDispatcher
import androidx.activity.OnBackPressedDispatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.digiapp.R
import com.example.digiapp.data.networks.ApiService
import com.example.digiapp.data.networks.RetrofitClient
import com.example.digiapp.databinding.ActivityPlayerBinding
import com.example.digiapp.services.MusicService
import com.example.digiapp.ui.music.MusicFragment.Companion.authorTag
import com.example.digiapp.ui.music.MusicFragment.Companion.fileTag
import com.example.digiapp.ui.music.MusicFragment.Companion.fromPlayer
import com.example.digiapp.ui.music.MusicFragment.Companion.idSeries
import com.example.digiapp.ui.music.MusicFragment.Companion.idTag
import com.example.digiapp.ui.music.MusicFragment.Companion.imageTag
import com.example.digiapp.ui.music.MusicFragment.Companion.songTag
import com.example.digiapp.ui.music.adapters.SongAdapter
import com.example.digiapp.ui.principal.PrincipalActivity
import com.example.digiapp.ui.principal.PrincipalActivity.Companion.IS_MINI_PLAYER_VISIBLE
import com.example.digiapp.ui.principal.PrincipalActivity.Companion.MINI_PLAYER_AUTHOR
import com.example.digiapp.ui.principal.PrincipalActivity.Companion.MINI_PLAYER_IMAGE
import com.example.digiapp.ui.principal.PrincipalActivity.Companion.MINI_PLAYER_TITLE
import com.example.digiapp.util.Util.convertTime
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.properties.Delegates

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private lateinit var adapter: SongAdapter
    private lateinit var imageCover: String
    private lateinit var songName: String
    private lateinit var songAuthor: String
    private lateinit var songFile: String
    private var isPlayer:Boolean = true
    private var seriesId: Int = 0
    private var idSong: Int = 0
    private lateinit var musicService: MusicService
    private var serviceBound = false
    private var isPlaying = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtener detalles de la canción desde el intent
        getSongDetails()

        // Iniciar el servicio de música
        val serviceIntent = Intent(this, MusicService::class.java)
        bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE)

        // Inicializar la interfaz de usuario después de que el servicio esté vinculado
        initUI()
        initListeners()
    }

    private fun initListeners() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MusicService.MusicBinder
            musicService = binder.getService()
            serviceBound = true
            binding.sliderTrack.max = 0
            binding.sliderTrack.progress = 0
            binding.tvCurrentTime.text = convertTime(0)
            binding.tvTotalTime.text = convertTime(0)

            if (isPlayer) {
                println("se esta reproduciendo")
                musicService.stopMusic()
                isPlaying = false
            }
            musicService.stopMusic()

            // Preparar la canción sin reproducirla
            musicService.prepareSong(songFile) {
                runOnUiThread {
                    // Habilitar el botón de reproducción y actualizar el tiempo total
                    binding.btnPlay.isEnabled = true
                    binding.sliderTrack.max = musicService.getMusicDuration()
                    binding.tvTotalTime.text = convertTime(musicService.getMusicDuration())
                }
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            serviceBound = false
        }
    }
    private fun initUI() {
        initSongDetails()
        initRecommendation()
        binding.sliderTrack.max = 0
        binding.sliderTrack.progress = 0
        binding.tvCurrentTime.text = convertTime(0)
        binding.tvTotalTime.text = convertTime(0)

        // Deshabilitar el botón de reproducción inicialmente
        binding.btnPlay.isEnabled = false

        // Configurar botón de reproducción
        binding.btnPlay.setOnClickListener {
            if (isPlaying) {
                musicService.pauseMusic()
                isPlaying = false
                binding.btnPlay.setImageResource(R.drawable.play)
            } else {
                musicService.startMusic()
                isPlaying = true
                binding.btnPlay.setImageResource(R.drawable.pause)
            }
        }

        // Configurar slider de progreso
        binding.sliderTrack.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
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

        // Actualizar el progreso y el tiempo actual de la canción de forma periódica
        updateSeekBarAndTime()
    }

    private fun updateSeekBarAndTime() {
        val handler = Handler(Looper.getMainLooper())
        handler.post(object : Runnable {
            override fun run() {
                if (serviceBound) {
                    binding.sliderTrack.progress = musicService.getMusicCurrentPosition()
                    binding.tvCurrentTime.text = convertTime(musicService.getMusicCurrentPosition())
                }
                handler.postDelayed(this, 1000)
            }
        })
    }


    private fun getSongDetails() {
        imageCover = intent.extras?.getString(imageTag) ?: ""
        songName = intent.extras?.getString(songTag) ?: ""
        songAuthor = intent.extras?.getString(authorTag) ?: ""
        songFile = intent.extras?.getString(fileTag) ?: ""
        idSong = intent.extras?.getInt(idTag) ?: 0
        seriesId = intent.extras?.getInt(idSeries) ?: 0
        isPlayer = intent.extras?.getBoolean(fromPlayer) ?: false
    }


    private fun initSongDetails() {
        binding.tvSongName.text = songName
        binding.tvAuthor.text = songAuthor
        Picasso.get().load(imageCover).into(binding.ivCover, object : Callback {
            override fun onSuccess() {
                initBackgroundColor()
            }
            override fun onError(e: Exception?) {
                Toast.makeText(this@PlayerActivity, "Error loading song cover", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun initBackgroundColor() {
        binding.ivCover.drawable?.let {
            binding.root.background = getDominantGradient(it)
        }
    }

    private fun getDominantGradient(image: Drawable): GradientDrawable {
        val imageCover = image.toBitmap()
        val palette = Palette.from(imageCover).generate()
        val darkVibrantColor = palette.getDarkVibrantColor(ContextCompat.getColor(this, R.color.semi_black))
        val mutatecolor = palette.getDarkMutedColor(ContextCompat.getColor(this, R.color.semi_black))
        val darkBackgroundColor = darkVibrantColor
        window.statusBarColor = mutatecolor
        return GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(mutatecolor, darkBackgroundColor)
        )
    }

    private fun initRecommendation() {
        initTitleRecommendations()
        adapter = SongAdapter(listOf(), true) { position -> onItemSelected(position) }
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvRecommendation.layoutManager = layoutManager
        binding.rvRecommendation.adapter = adapter
        getRecommendations()
    }

    private fun initTitleRecommendations() {
        var title = getString(R.string.recommendation)
        when (seriesId) {
            0 -> title += " " + getString(R.string.adventure)
            1 -> title += " " + getString(R.string.adventure02)
            2 -> title += " " + getString(R.string.tamers)
            3 -> title += " " + getString(R.string.frontier)
            4 -> title += " " + getString(R.string.savers)
            5 -> title += " " + getString(R.string.xros)
            6 -> title += " " + getString(R.string.hunters)
        }
        binding.titleRecommendation.text = title
    }

    private fun getRecommendations() {
        val apiService = RetrofitClient().getRetrofit()
        CoroutineScope(Dispatchers.IO).launch {
            val response = apiService.create(ApiService::class.java).getSongs()
            if (response.isNotEmpty()) {
                val filterResponseSeries = response.filter { it.seriesid == seriesId }
                val filterResponse = filterResponseSeries.filter { it.id != idSong }
                withContext(Dispatchers.Main) {
                    adapter.updateSongs(filterResponse)
                }
            }
        }
    }

    private fun onItemSelected(position: Int) {
        val intent = Intent(this, PlayerActivity::class.java).apply {
            putExtra(fileTag, adapter.getSong(position).file)
            putExtra(songTag, adapter.getSong(position).title)
            putExtra(imageTag, adapter.getSong(position).coverImage)
            putExtra(authorTag, adapter.getSong(position).artist)
            putExtra(idTag, adapter.getSong(position).id)
            putExtra(idSeries, adapter.getSong(position).seriesid)
            putExtra(fromPlayer, true)
        }
        startActivity(intent)
    }



    override fun finish() {
        super.finish()
        //go to principal activity

        val intent = Intent(this, PrincipalActivity::class.java).apply {
            if(isPlaying){
                putExtra(IS_MINI_PLAYER_VISIBLE, true)
                putExtra(MINI_PLAYER_IMAGE, imageCover)
                putExtra(MINI_PLAYER_TITLE, songName)
                putExtra(MINI_PLAYER_AUTHOR, songAuthor)
            }
        }
        startActivity(intent)
        // Aplica la transición personalizada
    }



}