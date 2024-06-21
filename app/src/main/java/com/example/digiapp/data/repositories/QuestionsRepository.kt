package com.example.digiapp.data.repositories

import com.example.digiapp.data.networks.ApiService

class QuestionsRepository(private val apiService: ApiService) {

        /**
        * Get the list of questions
        * @return List<Question>
        */
        suspend fun getQuestions() = apiService.getQuestions() // This function is used to get the list of questions from the API
}