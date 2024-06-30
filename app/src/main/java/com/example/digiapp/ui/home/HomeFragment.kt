package com.example.digiapp.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.digiapp.databinding.FragmentHomeBinding
import com.example.digiapp.ui.home.adapters.SeriesAdapter
import com.example.digiapp.ui.home.viewmodels.SeriesViewModel
import com.example.digiapp.ui.serie.SeriesInfoActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    //add view binding here
    //add variables here
    private lateinit var binding: FragmentHomeBinding //initialize the binding
    private lateinit var seriesAdapter: SeriesAdapter //initialize the series adapter

    private val viewModel : SeriesViewModel by viewModels()


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
        seriesAdapter = SeriesAdapter(listOf()) {position -> onItemSelected(position)} //initialize the series adapter
        val layoutManager =
            LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL, false
            )
        binding.recyclerViewSeries.layoutManager = layoutManager
        binding.recyclerViewSeries.adapter = seriesAdapter
    }

    /**
     * This function will be called when an item is selected and go to SeriesInfoActivity
     * @param position
     */
    private fun onItemSelected(position: Int) {
        val series = seriesAdapter.getSeries(position)
        val intent = Intent(requireContext(), SeriesInfoActivity::class.java).apply {
            putExtra("series", series.seriesname)
            putExtra("seriesId", series.id)
            putExtra("seriesImage", series.logo)
            putExtra("seriesDescription", series.sinopsis)
            putExtra("seriesBanner", series.banner)
        }
        startActivity(intent)
    }


    /**
     * This function will setup the observers
     *
     */
    private fun setupObservers(){
        viewModel.series.observe(viewLifecycleOwner) {
            seriesAdapter.updateData(it)
        }
        viewModel.isLoading.observe(viewLifecycleOwner){
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