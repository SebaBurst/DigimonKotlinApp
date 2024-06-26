package com.example.digiapp.data.networks

import com.example.digiapp.data.models.serie.SeriesItemResult
import com.example.digiapp.data.models.quiz.QuestionResult
import com.example.digiapp.data.models.song.SongResult
import com.example.digiapp.data.models.tamers.TamersResult
import retrofit2.http.GET

interface ApiService {

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


    /**
     * This function will return the list of Questions items from the API
     * @return List of questions items
     */
    @GET("quiz.json")
    suspend fun getQuestions(): QuestionResult


    @GET("tamers.json")
    suspend fun getTamers(): TamersResult





}