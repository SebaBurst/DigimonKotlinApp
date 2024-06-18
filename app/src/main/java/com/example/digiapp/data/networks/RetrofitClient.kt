package com.example.digiapp.data.networks

import com.example.digiapp.util.Constants.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {
    /**
     * This is the Retrofit client class where we will create the instance of Retrofit
     * and define the base URL for the API
     */


    /**
     * This function will return the Retrofit client instance
     * @return Retrofit instance
     *
     */
    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}