package com.example.digiapp.data.networks

import com.example.digiapp.data.models.SeriesItemResult
import com.example.digiapp.data.models.SeriesItemResultItem
import com.example.digiapp.data.models.song.SongResult
import retrofit2.http.GET

interface ApiService {
    // Add the API endpoints here

    /**
     * This function will return the list of series from the API
     * @return List of series
     */
    @GET("series.json")
    suspend fun getSeries(): SeriesItemResult

    /**
     * This function will return the list of songs from the API
     * @return List of songs
     */
    @GET("songs.json")
    suspend fun getSongs(): SongResult





}