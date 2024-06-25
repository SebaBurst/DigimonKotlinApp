package com.example.digiapp.ui.music.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digiapp.data.models.song.SongResultItem
import com.example.digiapp.data.repositories.SongRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MusicViewModel @Inject constructor(private val repository: SongRepository): ViewModel() {

    private val _songs = MutableLiveData<List<SongResultItem>>() //list of songs from the api with MutableLiveData
    val songs: LiveData<List<SongResultItem>> get() = _songs //list of songs from the api with LiveData

    private val _loading = MutableLiveData<Boolean>() //loading state with MutableLiveData
    val isLoading: LiveData<Boolean> get() = _loading //loading state with LiveData


    /**
     * Fetch the list of songs from the api
     */
    fun fetchSongs() {
        _loading.value = true
        viewModelScope.launch {
             val result = repository.getSongs()
            _songs.value = result
            _loading.value = false
        }
    }



}