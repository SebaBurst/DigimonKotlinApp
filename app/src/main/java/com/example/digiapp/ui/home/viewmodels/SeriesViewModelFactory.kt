package com.example.digiapp.ui.home.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.digiapp.data.repositories.SeriesRepository

class SeriesViewModelFactory (private val seriesRepository: SeriesRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SeriesViewModel(seriesRepository) as T
    }
}