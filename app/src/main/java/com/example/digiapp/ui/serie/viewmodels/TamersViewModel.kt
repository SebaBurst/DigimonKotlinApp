package com.example.digiapp.ui.serie.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.digiapp.data.models.tamers.TamersResultItem
import com.example.digiapp.data.repositories.TamersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope
import javax.inject.Inject


@HiltViewModel
class TamersViewModel @Inject constructor(private val tamersRepository: TamersRepository) : ViewModel(){

    private val _tamers = MutableLiveData<List<TamersResultItem>>() //list of tamers from the api with MutableLiveData
    val tamers: MutableLiveData<List<TamersResultItem>> get() = _tamers //list of tamers from the api with LiveData


/**
     * Fetch the list of tamers from the api
     */

    fun fetchTamers() {
       viewModelScope.launch{
           val result = tamersRepository.getTamers()
           _tamers.value = result
       }
    }





}