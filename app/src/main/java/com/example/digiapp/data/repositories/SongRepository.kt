package com.example.digiapp.data.repositories

import com.example.digiapp.data.networks.ApiService

class SongRepository (private val apiService: ApiService){

    /**
     * Get the list of songs
     * @return List<Song>
     */
    suspend fun getSongs() = apiService.getSongs()
}