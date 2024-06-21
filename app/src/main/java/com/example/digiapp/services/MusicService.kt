package com.example.digiapp.services

import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder

class MusicService: Service(), AudioManager.OnAudioFocusChangeListener {


    /**
     * Variables
     */
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var audioManager: AudioManager
    private lateinit var audioFocusRequest: AudioFocusRequest
    private var musicUrl: String? = null
    var isPlaying: Boolean = false
    private val binder = MusicBinder()
    private var isStarted = false

    /**
     * onCreate
     * Initialize the MediaPlayer and AudioManager
     */
    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer()
        audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
        // Configurar atributos de audio para API nivel 26+
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build()

            audioFocusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                .setAudioAttributes(audioAttributes)
                .setAcceptsDelayedFocusGain(true)
                .setOnAudioFocusChangeListener(this)
                .build()

        }
        mediaPlayer.setOnCompletionListener {
            // Detener el servicio cuando la reproducción termine
            stopSelf()
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    override fun onAudioFocusChange(focusChange: Int) {
        when (focusChange) {
            AudioManager.AUDIOFOCUS_GAIN -> {
                if (isPlaying) {
                    mediaPlayer.start()
                }
                mediaPlayer.setVolume(1.0f, 1.0f)
            }
            AudioManager.AUDIOFOCUS_LOSS -> {
                mediaPlayer.stop()
                mediaPlayer.release()
                isPlaying = false
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                if (mediaPlayer.isPlaying) {
                    mediaPlayer.pause()
                }
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
                mediaPlayer.setVolume(0.2f, 0.2f)
            }
        }
    }


    fun startMusic(){
        if (!isPlaying){
            mediaPlayer.start()
            isPlaying = true
        }
    }

    fun prepareSong(songFile: String, onPrepared: () -> Unit) {
        mediaPlayer.reset()
        mediaPlayer.setDataSource(songFile)
        mediaPlayer.setOnPreparedListener {
            onPrepared()
        }
        mediaPlayer.prepareAsync()
    }


    fun changeStarted(){
        isStarted = false
    }

    fun seekTo(progress: Int) {
        mediaPlayer.seekTo(progress)
    }

    fun getMusicCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }


    fun getMusicDuration(): Int {
        return mediaPlayer.duration
    }



    fun pauseMusic() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            isPlaying = false
        }
    }


     fun stopMusic() {
             if (mediaPlayer.isPlaying || isPlaying || isStarted) {
                 mediaPlayer.stop()
                 mediaPlayer.reset()
                 isPlaying = false
                 isStarted = false
                 abandonAudioFocus()
                 stopSelf()
             }

    }



    private fun abandonAudioFocus() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            audioManager.abandonAudioFocusRequest(audioFocusRequest)
        } else {
            audioManager.abandonAudioFocus(this)
        }
    }


    /**
     * Class MusicBinder
     */
    inner class MusicBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }

    companion object {
        const val ACTION_PLAY = "com.example.digiapp.action.PLAY"
        const val ACTION_PAUSE = "com.example.digiapp.action.PAUSE"
        const val ACTION_STOP = "com.example.digiapp.action.STOP"
        const val EXTRA_URL = "com.example.digiapp.extra.URL"
        const val CHANNEL_ID = "MusicServiceChannel"
    }
}