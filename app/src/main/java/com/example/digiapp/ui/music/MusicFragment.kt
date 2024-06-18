package com.example.digiapp.ui.music

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.digiapp.data.networks.ApiService
import com.example.digiapp.data.networks.RetrofitClient
import com.example.digiapp.databinding.FragmentMusicBinding
import com.example.digiapp.ui.music.adapters.SongAdapter
import com.example.digiapp.ui.player.PlayerActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MusicFragment : Fragment() {

    private lateinit var binding: FragmentMusicBinding // view binding
    private lateinit var adapters: List<SongAdapter> //list of recycler view adapters

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentMusicBinding.inflate(layoutInflater)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI(){
        initRecyclerViews()
    }

    /**
     * Initialize the recycler views
     * @return Unit
     */
    private fun initRecyclerViews(){
        //initialize the recycler view

        //initialize the recycler view adapters and create a list of recycler views
        val recyclerViews = listOf(binding.rvAdventureMusic,
                                   binding.rvAdventureTwoMusic,
                                   binding.rvTamersMusic,
                                   binding.rvFrontierMusic,
                                   binding.rvSaversMusic,
                                   binding.rvXrosMusic,
                                   binding.rvHuntersMusic)

        //initialize the recycler view adapters with empty list of songs
        adapters = List(recyclerViews.size) { index ->
            SongAdapter(listOf(), false) { position -> onItemSelected(index, position) }
        }

        //initialize the recycler view adapters and layout manager for each recycler view
        recyclerViews.forEachIndexed { index, recyclerView ->
            recyclerView.adapter = adapters[index]
            recyclerView.layoutManager = LinearLayoutManager(requireContext(),
                LinearLayoutManager.HORIZONTAL, false)
        }

        //get the songs for each series
        (0..6).forEach{seriesId ->
            getSongs(seriesId, adapters[seriesId])
        }

    }


    private fun onItemSelected(adapterIndex: Int, position: Int) {
        // Go to the player activity
        println("presione una musiquita")
        val adapter = adapters[adapterIndex]
        val intent = Intent(requireContext(), PlayerActivity::class.java).apply {
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


    /**
     * Get the songs for the series with id [serieId]
     * @param serieId the id of the series
     * @param songAdapter the adapter to update with the songs
     * @return Unit
     */
    private fun getSongs(serieId: Int, songAdapter: SongAdapter) {
        val apiService = RetrofitClient().getRetrofit()
        CoroutineScope(Dispatchers.IO).launch {
            val response = apiService.create(ApiService::class.java).getSongs()
            if (response.isNotEmpty()) {
                val filterResponse = response.filter { it.seriesid == serieId }
                withContext(Dispatchers.Main) {
                    songAdapter.updateSongs(filterResponse)
                }
            }
        }
    }



    companion object{
        var fileTag = "file"
        var songTag = "song"
        var imageTag = "image"
        var authorTag = "author"
        var idTag = "id"
        var idSeries = "idSeries"
        var fromPlayer = "fromPlayer"
    }


}