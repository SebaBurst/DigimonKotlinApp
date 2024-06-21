package com.example.digiapp.ui.quiz.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digiapp.data.models.quiz.QuestionResultItem
import com.example.digiapp.data.repositories.QuestionsRepository
import kotlinx.coroutines.launch

class QuestionViewModel(private val questionRepository: QuestionsRepository) : ViewModel() {

    private val _questions = MutableLiveData<List<QuestionResultItem>>() //list of questions from the api with MutableLiveData
    val questions: MutableLiveData<List<QuestionResultItem>> get() = _questions //list of questions from the api with LiveData


    /**
     * Fetch the list of questions from the api
     */
    fun fetchQuestions() {
        viewModelScope.launch {
            val result = questionRepository.getQuestions()
            _questions.value = result
        }
    }
}