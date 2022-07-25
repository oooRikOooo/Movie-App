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
import com.example.filmshelper.presentation.adapters.AdapterDelegatesHome
import com.example.filmshelper.utils.ViewStateWithList
import com.faltenreich.skeletonlayout.applySkeleton
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import javax.inject.Inject


class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    private val viewModel: MainFragmentViewModel by viewModels {
        factory.create()
    }

    @Inject
    lateinit var factory: MainFragmentViewModelFactory.Factory

    private val nowShowingAdapter = ListDelegationAdapter(
        AdapterDelegatesHome().nowShowingFilmsAdapterDelegate()
    )
    private val popularMoviesAdapter = ListDelegationAdapter(
        AdapterDelegatesHome().popularFilmsAdapterDelegate()
    )
    private val popularTvShowsAdapter = ListDelegationAdapter(
        AdapterDelegatesHome().popularTvShowsAdapterDelegate()
    )


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
        viewModel.getPopularTvShows()

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
            isNestedScrollingEnabled = false
            adapter = popularMoviesAdapter
        }

        binding.recyclerviewPopularTvShows.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            isNestedScrollingEnabled = false
            adapter = popularTvShowsAdapter
        }
    }

    private fun getDataForAdapter() {
        val skeletonNowShowing =
            binding.recyclerviewNowShowing.applySkeleton(R.layout.now_showing_item_list)

        val skeletonPopular =
            binding.recyclerviewPopular.applySkeleton(R.layout.now_showing_item_list)

        val skeletonPopularTvShows =
            binding.recyclerviewPopularTvShows.applySkeleton(R.layout.popular_tv_show_item_list)

        skeletonNowShowing.showSkeleton()
        skeletonPopular.showSkeleton()
        skeletonPopularTvShows.showSkeleton()

        viewModel.listNowShowingMovies.observe(viewLifecycleOwner) {

            when (it) {
                is ViewStateWithList.Error -> {
                    Log.d("riko", "${it.error}")
                }
                ViewStateWithList.Loading -> {
                    Log.d("riko", "Loading")
                }
                ViewStateWithList.NoData -> {
                    Log.d("riko", "NoData")
                }
                is ViewStateWithList.Success -> {
                    nowShowingAdapter.items = it.data

                    skeletonNowShowing.showOriginal()
                }
            }

        }

        viewModel.listPopularMovies.observe(viewLifecycleOwner) {

            when (it) {
                is ViewStateWithList.Error -> {
                    Toast.makeText(requireContext(), "${it.error}1", Toast.LENGTH_SHORT).show()
                }
                ViewStateWithList.Loading -> {

                }
                ViewStateWithList.NoData -> {
                    Toast.makeText(requireContext(), "NoData1", Toast.LENGTH_SHORT).show()
                }
                is ViewStateWithList.Success -> {
                    popularMoviesAdapter.items = it.data
                    skeletonPopular.showOriginal()
                }
            }

        }

        viewModel.listPopularTvShows.observe(viewLifecycleOwner) {

            when (it) {
                is ViewStateWithList.Error -> {
                    Toast.makeText(requireContext(), "${it.error}1", Toast.LENGTH_SHORT).show()
                }
                ViewStateWithList.Loading -> {

                }
                ViewStateWithList.NoData -> {
                    Toast.makeText(requireContext(), "NoData1", Toast.LENGTH_SHORT).show()
                }
                is ViewStateWithList.Success -> {
                    popularTvShowsAdapter.items = it.data
                    skeletonPopularTvShows.showOriginal()
                }
            }
        }

    }

}