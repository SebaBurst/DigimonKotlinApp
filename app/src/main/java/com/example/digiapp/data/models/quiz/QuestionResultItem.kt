package com.example.digiapp.data.models.quiz

data class QuestionResultItem(
    val answers: List<Answer>,
    val id: Int,
    val question: String
)