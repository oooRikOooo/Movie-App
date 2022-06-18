package com.example.filmshelper.presentation.screens

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.filmshelper.MainViewModel
import com.example.filmshelper.MainViewModelFactory
import com.example.filmshelper.appComponent
import com.example.filmshelper.databinding.FragmentMainBinding
import com.example.filmshelper.presentation.adapters.NowShowingFilmsAdapter
import com.example.filmshelper.presentation.adapters.PopularMoviesAdapter
import dagger.Lazy
import javax.inject.Inject
import javax.inject.Provider


class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    private val viewModel : MainViewModel by viewModels {
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

        setupAdapters()
        getDataForAdapter()

        return binding.root
    }

    private fun setupAdapters(){
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

    private fun getDataForAdapter(){
        viewModel.listNowShowingMovies.observe(viewLifecycleOwner){
            nowShowingAdapter.list = it
        }

        viewModel.listPopularMovies.observe(viewLifecycleOwner){
            popularMoviesAdapter.list = it
        }
    }

}