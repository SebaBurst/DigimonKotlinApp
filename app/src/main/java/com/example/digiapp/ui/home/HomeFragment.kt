package com.example.digiapp.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.digiapp.R
import com.example.digiapp.data.networks.ApiService
import com.example.digiapp.data.networks.RetrofitClient
import com.example.digiapp.data.repositories.SeriesRepository
import com.example.digiapp.databinding.FragmentHomeBinding
import com.example.digiapp.ui.home.adapters.SeriesAdapter
import com.example.digiapp.ui.home.viewmodels.SeriesViewModel
import com.example.digiapp.ui.home.viewmodels.SeriesViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HomeFragment : Fragment() {

    //add view binding here
    //add variables here
    private lateinit var binding: FragmentHomeBinding //initialize the binding
    private lateinit var seriesAdapter: SeriesAdapter //initialize the series adapter

    private val viewModel : SeriesViewModel by viewModels {
        SeriesViewModelFactory(
            SeriesRepository(
                RetrofitClient().getRetrofit().create(ApiService::class.java)
            )
        )
    }


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
        viewModel.fetchSeries()
        setupObservers()
        initRecyclerViews()
        setupObservers()
        //getSeries() //call the get series function


    }

    /**
     * This function will initialize the recycler views
     * @return Unit
     */

    private fun initRecyclerViews(){
        seriesAdapter = SeriesAdapter(listOf()) //initialize the series adapter
        val layoutManager =
            LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL, false
            )
        binding.recyclerViewSeries.layoutManager = layoutManager
        binding.recyclerViewSeries.adapter = seriesAdapter
    }


    /**
     * This function will setup the observers
     *
     */
    private fun setupObservers(){
        viewModel.series.observe(viewLifecycleOwner) {
            seriesAdapter.updateData(it)
        }

        viewModel.isLoadiing.observe(viewLifecycleOwner){
            if(it){
                binding.gifLoadingDigitama.visibility = View.VISIBLE
                binding.recyclerViewSeries.visibility = View.GONE
            }else{
                binding.gifLoadingDigitama.visibility = View.GONE
                binding.recyclerViewSeries.visibility = View.VISIBLE
            }
        }

    }





}