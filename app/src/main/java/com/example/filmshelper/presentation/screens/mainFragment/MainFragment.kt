package com.example.filmshelper.presentation.screens.mainFragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.filmshelper.R
import com.example.filmshelper.appComponent
import com.example.filmshelper.data.models.DisplayableItem
import com.example.filmshelper.databinding.FragmentMainBinding
import com.example.filmshelper.presentation.adapters.AdapterDelegatesHome
import com.example.filmshelper.utils.ViewStateWithList
import com.faltenreich.skeletonlayout.applySkeleton
import com.google.android.material.snackbar.Snackbar
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

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentMainBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClickListeners()

        val itemDetailFragmentContainer: View? = view.findViewById(R.id.item_detail_nav_container)

        val popularAdapter = ListDelegationAdapter(
            AdapterDelegatesHome().popularFilmsAdapterDelegate(itemDetailFragmentContainer)
        )

        val popularTvShowsAdapter = ListDelegationAdapter(
            AdapterDelegatesHome().popularTvShowsAdapterDelegate(itemDetailFragmentContainer)
        )

        setupAdapters(popularAdapter, popularTvShowsAdapter)
        getDataForAdapter(popularAdapter, popularTvShowsAdapter)
    }

    private fun setOnClickListeners() {
        binding.apply {
            swipeRefreshLayout?.setOnRefreshListener {
                swipeRefreshLayout.isRefreshing = false
                viewModel.getNowShowingMovies()
                viewModel.getPopularMovies()
                viewModel.getPopularTvShows()
            }
        }
    }


    private fun setupAdapters(
        popularAdapter: ListDelegationAdapter<List<DisplayableItem>>,
        popularTvShowsAdapter: ListDelegationAdapter<List<DisplayableItem>>
    ) {

        binding.recyclerviewNowShowing.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapter = nowShowingAdapter
        }

        binding.recyclerviewPopular.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            isNestedScrollingEnabled = false
            adapter = popularAdapter
        }

        binding.recyclerviewPopularTvShows.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            isNestedScrollingEnabled = false
            adapter = popularTvShowsAdapter
        }
    }

    private fun getDataForAdapter(
        popularAdapter: ListDelegationAdapter<List<DisplayableItem>>,
        popularTvShowsAdapter: ListDelegationAdapter<List<DisplayableItem>>
    ) {
        val skeletonNowShowing =
            binding.recyclerviewNowShowing.applySkeleton(R.layout.now_showing_item_list)

        val skeletonPopular =
            binding.recyclerviewPopular.applySkeleton(R.layout.popular_item_list)

        val skeletonPopularTvShows =
            binding.recyclerviewPopularTvShows.applySkeleton(R.layout.popular_tv_show_item_list)

        skeletonNowShowing.showSkeleton()
        skeletonPopular.showSkeleton()
        skeletonPopularTvShows.showSkeleton()

        viewModel.listNowShowingMovies.observe(viewLifecycleOwner) {

            when (it) {
                is ViewStateWithList.Error -> {
                    skeletonNowShowing.showOriginal()
                    Snackbar.make(binding.root, "No internet connection", Snackbar.LENGTH_SHORT)
                        .show()
                }
                ViewStateWithList.Loading -> {
                }
                ViewStateWithList.NoData -> {
                    skeletonNowShowing.showOriginal()
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
                    skeletonPopular.showOriginal()
                    Snackbar.make(binding.root, "No internet connection", Snackbar.LENGTH_SHORT)
                        .show()
                }
                ViewStateWithList.Loading -> {

                }
                ViewStateWithList.NoData -> {
                    skeletonPopular.showOriginal()
                }
                is ViewStateWithList.Success -> {
                    popularAdapter.items = it.data
                    skeletonPopular.showOriginal()
                }
            }

        }

        viewModel.listPopularTvShows.observe(viewLifecycleOwner) {

            when (it) {
                is ViewStateWithList.Error -> {
                    skeletonPopularTvShows.showOriginal()
                    Snackbar.make(binding.root, "No internet connection", Snackbar.LENGTH_SHORT)
                        .show()
                }
                ViewStateWithList.Loading -> {

                }
                ViewStateWithList.NoData -> {
                    skeletonPopularTvShows.showOriginal()
                }
                is ViewStateWithList.Success -> {
                    popularTvShowsAdapter.items = it.data
                    skeletonPopularTvShows.showOriginal()
                }
            }
        }


    }
}