package com.example.filmshelper.presentation.screens

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.filmshelper.MainViewModel
import com.example.filmshelper.MainViewModelFactory
import com.example.filmshelper.R
import com.example.filmshelper.appComponent
import com.example.filmshelper.databinding.FragmentMainBinding
import com.example.filmshelper.presentation.adapters.NowShowingFilmsAdapter
import com.example.filmshelper.presentation.adapters.PopularMoviesAdapter
import com.faltenreich.skeletonlayout.applySkeleton
import javax.inject.Inject


class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    private val viewModel: MainViewModel by viewModels {
        factory.create()
    }

    @Inject
    lateinit var factory: MainViewModelFactory.Factory

    private val nowShowingAdapter = NowShowingFilmsAdapter()
    private val popularMoviesAdapter = PopularMoviesAdapter()


    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentMainBinding.inflate(layoutInflater, container, false)

        viewModel.getNowShowingMovies()
        viewModel.getPopularMovies()


        setupAdapters()
        getDataForAdapter()

        return binding.root
    }

    private fun setupAdapters() {

        binding.recyclerviewNowShowing.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapter = nowShowingAdapter
        }

        binding.recyclerviewPopular.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = popularMoviesAdapter
        }
    }

    private fun getDataForAdapter() {
        val skeletonNowShowing =
            binding.recyclerviewNowShowing.applySkeleton(R.layout.now_showing_item_list)

        val skeletonPopular =
            binding.recyclerviewPopular.applySkeleton(R.layout.now_showing_item_list)

        skeletonNowShowing.showSkeleton()
        skeletonPopular.showSkeleton()

        viewModel.listNowShowingMovies.observe(viewLifecycleOwner) {


            when (it) {
                is MainViewModel.ViewStateNowShowingMovies.Error -> {
                    Toast.makeText(requireContext(), "${it.error}", Toast.LENGTH_SHORT).show()
                    Log.d("riko", "${it.error}")
                }
                MainViewModel.ViewStateNowShowingMovies.Loading -> {
                    Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show()
                    Log.d("riko", "Loading")

                     /*binding.recyclerviewNowShowing.visibility = View.GONE
                     binding.textViewNowShowing.visibility = View.GONE
                     binding.buttonSeeMoreNowShowing.visibility = View.GONE*/
                }
                MainViewModel.ViewStateNowShowingMovies.NoData -> {
                    Toast.makeText(requireContext(), "NoData", Toast.LENGTH_SHORT).show()
                    Log.d("riko", "NoData")
                }
                is MainViewModel.ViewStateNowShowingMovies.Success -> {
                    nowShowingAdapter.list = it.data
                    /*binding.recyclerviewNowShowing.visibility = View.VISIBLE
                    binding.textViewNowShowing.visibility = View.VISIBLE
                    binding.buttonSeeMoreNowShowing.visibility = View.VISIBLE*/
                    skeletonNowShowing.showOriginal()
                }
            }

        }

        viewModel.listPopularMovies.observe(viewLifecycleOwner) {

            when (it) {
                is MainViewModel.ViewStatePopularMovies.Error -> {
                    Toast.makeText(requireContext(), "${it.error}1", Toast.LENGTH_SHORT).show()
                    Log.d("riko", "${it.error}1")
                }
                MainViewModel.ViewStatePopularMovies.Loading -> {
                    Toast.makeText(requireContext(), "Loading1", Toast.LENGTH_SHORT).show()
                    Log.d("riko", "Loading1")
                    /*binding.recyclerviewPopular.visibility = View.GONE
                    binding.textViewPopular.visibility = View.GONE
                    binding.buttonSeeMorePopularMovies.visibility = View.GONE*/
                }
                MainViewModel.ViewStatePopularMovies.NoData -> {
                    Toast.makeText(requireContext(), "NoData1", Toast.LENGTH_SHORT).show()
                    Toast.makeText(requireContext(), "NoData1", Toast.LENGTH_SHORT).show()
                }
                is MainViewModel.ViewStatePopularMovies.Success -> {
                    popularMoviesAdapter.list = it.data
                    /*binding.recyclerviewPopular.visibility = View.VISIBLE
                    binding.textViewPopular.visibility = View.VISIBLE
                    binding.buttonSeeMorePopularMovies.visibility = View.VISIBLE*/
                    skeletonPopular.showOriginal()
                }
            }

        }
    }

}