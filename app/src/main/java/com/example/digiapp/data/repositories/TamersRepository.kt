package com.example.digiapp.data.repositories

import com.example.digiapp.data.networks.ApiService

class TamersRepository (private val apiService: ApiService) {

    /**
     * Get the list of tamers
     * @return List<Tamers>
     */
    suspend fun getTamers() = apiService.getTamers()
}