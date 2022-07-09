package com.example.filmshelper.presentation.screens.mainFragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.filmshelper.R
import com.example.filmshelper.appComponent
import com.example.filmshelper.databinding.FragmentMainBinding
import com.example.filmshelper.presentation.adapters.NowShowingFilmsAdapter
import com.example.filmshelper.presentation.adapters.PopularMoviesAdapter
import com.faltenreich.skeletonlayout.applySkeleton
import javax.inject.Inject


class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    private val viewModel: MainFragmentViewModel by viewModels {
        factory.create()
    }

    @Inject
    lateinit var factory: MainFragmentViewModelFactory.Factory

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
                is MainFragmentViewModel.ViewStateNowShowingMovies.Error -> {
                    Log.d("riko", "${it.error}")
                }
                MainFragmentViewModel.ViewStateNowShowingMovies.Loading -> {
                    Log.d("riko", "Loading")
                }
                MainFragmentViewModel.ViewStateNowShowingMovies.NoData -> {
                    Log.d("riko", "NoData")
                }
                is MainFragmentViewModel.ViewStateNowShowingMovies.Success -> {
                    nowShowingAdapter.list = it.data

                    skeletonNowShowing.showOriginal()
                }
            }

        }

        viewModel.listPopularMovies.observe(viewLifecycleOwner) {

            when (it) {
                is MainFragmentViewModel.ViewStatePopularMovies.Error -> {
                    Toast.makeText(requireContext(), "${it.error}1", Toast.LENGTH_SHORT).show()
                }
                MainFragmentViewModel.ViewStatePopularMovies.Loading -> {

                }
                MainFragmentViewModel.ViewStatePopularMovies.NoData -> {
                    Toast.makeText(requireContext(), "NoData1", Toast.LENGTH_SHORT).show()
                }
                is MainFragmentViewModel.ViewStatePopularMovies.Success -> {
                    popularMoviesAdapter.list = it.data
                    skeletonPopular.showOriginal()
                }
            }

        }
    }

}