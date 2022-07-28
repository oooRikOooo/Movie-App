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
import androidx.work.*
import com.example.filmshelper.R
import com.example.filmshelper.appComponent
import com.example.filmshelper.databinding.FragmentMainBinding
import com.example.filmshelper.presentation.adapters.AdapterDelegatesHome
import com.example.filmshelper.presentation.screens.mainFragment.updateDataWorker.UpdateDataWorker
import com.example.filmshelper.utils.ViewStateWithList
import com.faltenreich.skeletonlayout.applySkeleton
import com.google.android.material.snackbar.Snackbar
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import java.util.*
import java.util.concurrent.TimeUnit
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
        setOnClickListeners()

        createWorkManager()

        return binding.root
    }

    private fun setOnClickListeners() {
        binding.apply {
            swipeRefreshLayout.setOnRefreshListener {
                swipeRefreshLayout.isRefreshing = false
                viewModel.getNowShowingMovies()
                viewModel.getPopularMovies()
                viewModel.getPopularTvShows()
            }
        }
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
                    popularMoviesAdapter.items = it.data
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

    private fun createWorkManager() {

        val sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE) ?: return
        val hour = sharedPreferences.getInt("Hour", 18)
        val minute = sharedPreferences.getInt("Minute", 0)

        val currentDate = Calendar.getInstance()
        val dueDate = Calendar.getInstance()

        dueDate.set(Calendar.HOUR_OF_DAY, hour)
        dueDate.set(Calendar.MINUTE, minute)
        dueDate.set(Calendar.SECOND, 0)

        if (dueDate.before(currentDate)) {
            dueDate.add(Calendar.HOUR_OF_DAY, 24)
        }

        val timeDiff = dueDate.timeInMillis - currentDate.timeInMillis

        val data = Data.Builder().putInt("NOTIFICATION_ID", 0).build()
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val myWorkRequest = PeriodicWorkRequestBuilder<UpdateDataWorker>(
            12, TimeUnit.HOURS
        )
            .setConstraints(constraints)
            .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS).setInputData(data).build()

        WorkManager.getInstance(requireActivity()).enqueueUniquePeriodicWork(
            "notification",
            ExistingPeriodicWorkPolicy.REPLACE,
            myWorkRequest
        )
    }

}