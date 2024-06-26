package com.example.digiapp.ui.home.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digiapp.data.models.serie.SeriesItemResultItem
import com.example.digiapp.data.repositories.SeriesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SeriesViewModel @Inject constructor(private val seriesRepository: SeriesRepository) : ViewModel() {

        private val _series = MutableLiveData<List<SeriesItemResultItem>>() //list of series from the api with MutableLiveData
        val series: MutableLiveData<List<SeriesItemResultItem>> get() = _series //list of series from the api with LiveData

        private val _isLoading = MutableLiveData<Boolean>() //loading state with MutableLiveData
        val isLoading: MutableLiveData<Boolean> get() = _isLoading //loading state with LiveData


        /**
         * Fetch the list of series from the api
         * */

        fun fetchSeries(){
            _isLoading.value = true
            viewModelScope.launch {
                val result = seriesRepository.getSeries()
                _series.value = result
                _isLoading.value = false
            }
        }



}