package com.example.digiapp.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.digiapp.R
import com.example.digiapp.data.networks.ApiService
import com.example.digiapp.data.networks.RetrofitClient
import com.example.digiapp.databinding.FragmentHomeBinding
import com.example.digiapp.ui.home.adapters.SeriesAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HomeFragment : Fragment() {

    //add view binding here
    //add variables here
    private lateinit var binding: FragmentHomeBinding //initialize the binding
    private lateinit var seriesAdapter: SeriesAdapter //initialize the series adapter

    /**
     * This function will create the view
     * @param savedInstanceState
     * @see Bundle
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentHomeBinding.inflate(layoutInflater) //initialize the binding

    }

    /**
     * This function will create the view
     * @param inflater
     * @param container
     */

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root //return the root view
    }

    /**
     * This function will be called when the view is created
     * @param view
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getSeries() //call the get series function
    }


    /**
     * This function will get the series from the API
     * @return List of series
     * @see ApiService
     * @see RetrofitClient
     * @see CoroutineScope
     * @see Dispatchers
     */
    private fun getSeries(){
        //call the API service here
        val apiService = RetrofitClient().getRetrofit(); //initialize the retrofit client
        CoroutineScope(Dispatchers.IO).launch { //launch the coroutine
            val response = apiService.create(ApiService::class.java).getSeries(); //get the series from the API
            //check if the response is successful print the response
            if(response.isNotEmpty()){ //check if the response is not empty
                withContext(Dispatchers.Main){ //switch to the main thread
                    seriesAdapter = SeriesAdapter(response) //initialize the series adapter
                    val layoutManager =
                        LinearLayoutManager(requireContext(),
                            LinearLayoutManager.VERTICAL, false)  //initialize the layout manager
                    binding.recyclerViewSeries.layoutManager = layoutManager //set the layout manager
                    binding.recyclerViewSeries.adapter = seriesAdapter //set the adapter
                    if(response.isNotEmpty()){ //check if the response is not empty
                        binding.gifLoadingDigitama.visibility = View.GONE //hide the loading digitama
                        binding.recyclerViewSeries.visibility = View.VISIBLE //show the recycler view
                    }
                }
            }

        }

    }




}