package com.example.digiapp.data.repositories

import com.example.digiapp.data.networks.ApiService

class SeriesRepository (private val apiService: ApiService) {

    /**
     * Get the list of series
     * @return List<Series>
     */
    suspend fun getSeries() = apiService.getSeries() // This function is used to get the list of series from the API
}