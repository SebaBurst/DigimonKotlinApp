package com.example.digiapp.ui.quiz

import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import com.example.digiapp.R
import com.example.digiapp.data.models.quiz.Answer
import com.example.digiapp.data.models.quiz.QuestionResult
import com.example.digiapp.data.models.quiz.QuestionResultItem
import com.example.digiapp.data.networks.ApiService
import com.example.digiapp.data.networks.RetrofitClient
import com.example.digiapp.databinding.FragmentQuizBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QuizFragment : Fragment() {

    private lateinit var binding: FragmentQuizBinding
    private var currentQuestionIndex = 0
    private var answerSelected = -1
    private lateinit var questions: QuestionResult
    private var endgame = false
    private var correctAnswers = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentQuizBinding.inflate(layoutInflater)
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
        initListeners()
        getQuestions()
    }

    private fun initUI() {
        binding.quizLayoutStart.visibility = View.VISIBLE
        binding.quizLayoutQuestion.visibility = View.GONE
        binding.nextQuizButton.visibility = View.GONE
        binding.restartQuizButton.visibility = View.GONE
        updateQuestionCounter()
        animateImageViewStart()
    }

    private fun animateImageViewStart(){

        val rotateAnimator = ObjectAnimator.ofFloat(binding.quizImage, "rotation", 0f, -360f).apply {
            duration = 30000 // duración de la animación en milisegundos
            repeatCount = ObjectAnimator.INFINITE // repetir infinitamente
            interpolator = LinearInterpolator() // interpolador lineal para una rotación uniforme
        }

        // Iniciar la animación
        rotateAnimator.start()
    }

    private fun initListeners() {
        binding.startQuizButton.setOnClickListener {
            binding.quizLayoutStart.visibility = View.GONE
            binding.quizLayoutQuestion.visibility = View.VISIBLE
            loadQuestion() // Load the first question when starting the quiz
        }
        binding.nextQuizButton.setOnClickListener {
            nextQuestion()
        }
        binding.restartQuizButton.setOnClickListener {
            currentQuestionIndex = 0
            updateQuestionCounter()
            loadQuestion()
            answerSelected = -1
            changeSelectedOptionBackground()
            binding.option1.isClickable = true
            binding.option2.isClickable = true
            binding.option3.isClickable = true
            binding.option4.isClickable = true
            binding.nextQuizButton.visibility = View.GONE
            binding.restartQuizButton.visibility = View.GONE
            binding.quizLayoutStart.visibility = View.VISIBLE
            binding.quizLayoutQuestion.visibility = View.GONE
        }

        selectAnswer()
    }

    private fun loadQuestion(){
        if (this::questions.isInitialized) {
            val question = questions[currentQuestionIndex]
            binding.question.text = question.question
            binding.option1Text.text = question.answers[0].answer
            binding.option2Text.text = question.answers[1].answer
            binding.option3Text.text = question.answers[2].answer
            binding.option4Text.text = question.answers[3].answer
        }
    }

    private fun selectAnswer() {
        binding.option1.setOnClickListener {
            answerSelected = 0
            changeSelectedOptionBackground()
            checkAnswer()
        }
        binding.option2.setOnClickListener {
            answerSelected = 1
            changeSelectedOptionBackground()
            checkAnswer()
        }
        binding.option3.setOnClickListener {
            answerSelected = 2
            changeSelectedOptionBackground()
            checkAnswer()
        }
        binding.option4.setOnClickListener {
            answerSelected = 3
            changeSelectedOptionBackground()
            checkAnswer()
        }
    }

    private fun checkAnswer() {
        val question = questions[currentQuestionIndex]
        val correctAnswer = question.answers[answerSelected].correct
        if (correctAnswer) {
            binding.nextQuizButton.visibility = View.VISIBLE
            changeCorrectBackground(answerSelected)
            // Handle correct answer scenario
        } else {
           searchCorrectAnswer()
            changeCorrectBackground(correctAnswers)
            binding.restartQuizButton.visibility = View.VISIBLE
        }
    }

    private fun searchCorrectAnswer(){
        val question = questions[currentQuestionIndex]
        for (i in question.answers.indices) {
            if (question.answers[i].correct) {
                correctAnswers = i
                break
            }
        }
    }

    private fun changeCorrectBackground(answerSelected: Int){
        when (answerSelected) {
            0 -> {
                binding.option1.setBackgroundResource(R.drawable.correct_option)
                binding.option1Text.setTextColor(resources.getColor(R.color.white))
                binding.option2.isClickable = false
                binding.option3.isClickable = false
                binding.option4.isClickable = false
            }
            1 -> {
                binding.option2.setBackgroundResource(R.drawable.correct_option)
                binding.option2Text.setTextColor(resources.getColor(R.color.white))
                binding.option1.isClickable = false
                binding.option3.isClickable = false
                binding.option4.isClickable = false
            }
            2 -> {
                binding.option3.setBackgroundResource(R.drawable.correct_option)
                binding.option3Text.setTextColor(resources.getColor(R.color.white))
                binding.option1.isClickable = false
                binding.option2.isClickable = false
                binding.option4.isClickable = false
            }
            3 -> {
                binding.option4.setBackgroundResource(R.drawable.correct_option)
                binding.option4Text.setTextColor(resources.getColor(R.color.white))
                binding.option4.isClickable = false
                binding.option2.isClickable = false
                binding.option3.isClickable = false
            }

        }
    }
    private fun changeSelectedOptionBackground() {
        when (answerSelected) {
            0 -> {
                binding.option1.setBackgroundResource(R.drawable.wrong_option)
                binding.option1Text.setTextColor(resources.getColor(R.color.white))
                binding.option2.setBackgroundResource(R.drawable.option_unselected)
                binding.option2Text.setTextColor(resources.getColor(R.color.semi_black))
                binding.option3.setBackgroundResource(R.drawable.option_unselected)
                binding.option3Text.setTextColor(resources.getColor(R.color.semi_black))
                binding.option4.setBackgroundResource(R.drawable.option_unselected)
                binding.option4Text.setTextColor(resources.getColor(R.color.semi_black))
            }
            1 -> {
                binding.option2.setBackgroundResource(R.drawable.wrong_option)
                binding.option2Text.setTextColor(resources.getColor(R.color.white))
                binding.option1.setBackgroundResource(R.drawable.option_unselected)
                binding.option1Text.setTextColor(resources.getColor(R.color.semi_black))
                binding.option3.setBackgroundResource(R.drawable.option_unselected)
                binding.option3Text.setTextColor(resources.getColor(R.color.semi_black))
                binding.option4.setBackgroundResource(R.drawable.option_unselected)
                binding.option4Text.setTextColor(resources.getColor(R.color.semi_black))
            }
            2 -> {
                binding.option3.setBackgroundResource(R.drawable.wrong_option)
                binding.option3Text.setTextColor(resources.getColor(R.color.white))
                binding.option1.setBackgroundResource(R.drawable.option_unselected)
                binding.option1Text.setTextColor(resources.getColor(R.color.semi_black))
                binding.option2.setBackgroundResource(R.drawable.option_unselected)
                binding.option2Text.setTextColor(resources.getColor(R.color.semi_black))
                binding.option4.setBackgroundResource(R.drawable.option_unselected)
                binding.option4Text.setTextColor(resources.getColor(R.color.semi_black))
            }
            3 -> {
                binding.option4.setBackgroundResource(R.drawable.wrong_option)
                binding.option4Text.setTextColor(resources.getColor(R.color.white))
                binding.option1.setBackgroundResource(R.drawable.option_unselected)
                binding.option1Text.setTextColor(resources.getColor(R.color.semi_black))
                binding.option2.setBackgroundResource(R.drawable.option_unselected)
                binding.option2Text.setTextColor(resources.getColor(R.color.semi_black))
                binding.option3.setBackgroundResource(R.drawable.option_unselected)
                binding.option3Text.setTextColor(resources.getColor(R.color.semi_black))
            }
            else -> {
                binding.option1.setBackgroundResource(R.drawable.option_unselected)
                binding.option1Text.setTextColor(resources.getColor(R.color.semi_black))
                binding.option2.setBackgroundResource(R.drawable.option_unselected)
                binding.option2Text.setTextColor(resources.getColor(R.color.semi_black))
                binding.option3.setBackgroundResource(R.drawable.option_unselected)
                binding.option3Text.setTextColor(resources.getColor(R.color.semi_black))
                binding.option4.setBackgroundResource(R.drawable.option_unselected)
                binding.option4Text.setTextColor(resources.getColor(R.color.semi_black))
            }
        }
    }

    private fun updateQuestionCounter() {
        var questionCounter = getString(R.string.countQuestion)
        val questionCounterInt = currentQuestionIndex + 1
        questionCounter += " $questionCounterInt"
        binding.counterQuestion.text = questionCounter
    }

    private fun nextQuestion() {
        currentQuestionIndex++
        if (currentQuestionIndex < questions.size) {
            updateQuestionCounter()
            loadQuestion()
            answerSelected = -1
            changeSelectedOptionBackground()
            binding.option1.isClickable = true
            binding.option2.isClickable = true
            binding.option3.isClickable = true
            binding.option4.isClickable = true
            binding.nextQuizButton.visibility = View.GONE
            binding.restartQuizButton.visibility = View.GONE
        } else {
            // Handle end of quiz scenario
        }
    }

    private fun getQuestions() {
        val apiService = RetrofitClient().getRetrofit()
        CoroutineScope(Dispatchers.IO).launch {
            val response = apiService.create(ApiService::class.java).getQuestions()
            if (response.isNotEmpty()) {
                questions = response
                withContext(Dispatchers.Main) {
                    loadQuestion() // Load the first question on the main thread
                }
            }
        }
    }
}
