package com.example.filmshelper.presentation.screens

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.filmshelper.presentation.screens.mainFragment.MainFragmentViewModel
import com.example.filmshelper.presentation.screens.mainFragment.MainFragmentViewModelFactory
import com.example.filmshelper.R
import com.example.filmshelper.appComponent
import com.example.filmshelper.databinding.FragmentFilmDetailsBinding
import com.example.filmshelper.presentation.adapters.CategoriesAdapter
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import javax.inject.Inject


class FilmDetailsFragment : Fragment() {

    private lateinit var binding: FragmentFilmDetailsBinding

    private val viewModel: MainFragmentViewModel by viewModels {
        factory.create()
    }

    @Inject
    lateinit var factory: MainFragmentViewModelFactory.Factory

    private val categoriesAdapter = CategoriesAdapter()

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        binding = FragmentFilmDetailsBinding.inflate(layoutInflater, container, false)

        val filmId = requireArguments().getString("filmId")

        viewModel.getMovieById(filmId.toString())
        viewModel.getYoutubeTrailerById(filmId.toString())
        setupAdapter()
        getData()

        return binding.root
    }

    private fun getData() {

        viewModel.youtubeTrailer.observe(viewLifecycleOwner) {
            when (it) {
                is MainFragmentViewModel.ViewStateYoutubeTrailerById.Error -> {
                    Log.d("riko", "${it.error}1")
                }
                MainFragmentViewModel.ViewStateYoutubeTrailerById.Loading -> {

                    setViewAndProgressBarVisibility(binding.appBarLayout, View.INVISIBLE,
                        binding.progressBarVideo, View.VISIBLE)
                    /*binding.appBarLayout.visibility = View.INVISIBLE
                    binding.progressBarVideo.visibility = View.VISIBLE*/
                    Log.d("riko", "Loading1")
                }
                MainFragmentViewModel.ViewStateYoutubeTrailerById.NoData -> {
                    Log.d("riko", "NoData1")
                }
                is MainFragmentViewModel.ViewStateYoutubeTrailerById.Success -> {

                    binding.infoTrailer = it.data

                    binding.youtubePlayer.addYouTubePlayerListener(object :
                        AbstractYouTubePlayerListener() {
                        override fun onReady(youTubePlayer: YouTubePlayer) {
                            youTubePlayer.cueVideo(it.data.videoId, 0f)
                        }
                    })

                    setViewAndProgressBarVisibility(binding.appBarLayout, View.VISIBLE,
                        binding.progressBarVideo, View.INVISIBLE)
                    /*binding.appBarLayout.visibility = View.VISIBLE
                    binding.progressBarVideo.visibility = View.INVISIBLE*/

                }
            }


        }

        viewModel.filmById.observe(viewLifecycleOwner) {

            when (it) {
                is MainFragmentViewModel.ViewStateMovieById.Error -> {
                    Log.d("riko", "${it.error}2")
                }
                MainFragmentViewModel.ViewStateMovieById.Loading -> {
                    Log.d("riko", "Loading2")
                    setViewAndProgressBarVisibility(binding.nestedScroll, View.INVISIBLE,
                        binding.progressBarNestedScroll, View.VISIBLE)
                    /*binding.nestedScroll.visibility = View.INVISIBLE
                    binding.progressBarNestedScroll.visibility = View.VISIBLE*/
                }
                MainFragmentViewModel.ViewStateMovieById.NoData -> {
                    Log.d("riko", "NoData2")
                }
                is MainFragmentViewModel.ViewStateMovieById.Success -> {
                    binding.apply {
                        categoriesAdapter.list = it.data.genreList
                        textViewFilmTitle.text = it.data.title
                        textViewRatingImdb.text =
                            requireContext().getString(R.string.rating_imdb, it.data.imDbRating)
                        textViewLength.text = it.data.runtimeMins

                        if (it.data.languages.contains(",")) {

                            val kept: String =
                                it.data.languages.substring(0, it.data.languages.indexOf(","))
                            Log.d("riko", kept)
                            textViewLanguage.text = kept

                        } else textViewLanguage.text = it.data.languages

                        textViewContentRating.text = it.data.contentRating
                        textViewDescription.text = it.data.plot


                        /*binding.nestedScroll.visibility = View.VISIBLE
                        binding.progressBarNestedScroll.visibility = View.INVISIBLE*/

                    }
                    setViewAndProgressBarVisibility(binding.nestedScroll, View.VISIBLE,
                        binding.progressBarNestedScroll, View.INVISIBLE)

                }

            }

        }

    }

    private fun setViewAndProgressBarVisibility(view: View, statusView: Int, progressBar: ProgressBar, statusBar: Int){
        view.visibility = statusView
        progressBar.visibility = statusBar
    }

    private fun setupAdapter() {

        binding.recyclerView.apply {
            setHasFixedSize(true)
            val layoutManager = FlexboxLayoutManager(context)
            layoutManager.flexDirection = FlexDirection.ROW
            layoutManager.justifyContent = JustifyContent.FLEX_END

            adapter = categoriesAdapter
        }


    }


}