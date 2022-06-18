package com.example.filmshelper.presentation.screens

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.filmshelper.MainViewModel
import com.example.filmshelper.MainViewModelFactory
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

    private lateinit var binding : FragmentFilmDetailsBinding
    private val viewModel : MainViewModel by viewModels{
        factory.create()
    }

    @Inject
    lateinit var factory: MainViewModelFactory.Factory

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

        viewModel.youtubeTrailer.observe(viewLifecycleOwner){

            binding.infoTrailer = it
            binding.youtubePlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    //youTubePlayer.loadVideo(it.videoId, 0f)
                    youTubePlayer.cueVideo(it.videoId, 0f)
                }
            })
        }

        viewModel.filmById.observe(viewLifecycleOwner){
            binding.apply {
                categoriesAdapter.list = it.genreList
                textViewFilmTitle.text = it.title
                textViewRatingImdb.text = requireContext().getString(R.string.rating_imdb, it.imDbRating)
                textViewLength.text = it.runtimeMins

                if (it.languages.contains(",")){

                    val kept: String = it.languages.substring(0, it.languages.indexOf(","))
                    Log.d("riko", kept)
                    textViewLanguage.text = kept

                } else textViewLanguage.text = it.languages

                textViewContentRating.text = it.contentRating
                textViewDescription.text = it.plot

            }
        }
    }

    private fun setupAdapter(){

        binding.recyclerView.apply {
            setHasFixedSize(true)
            val layoutManager = FlexboxLayoutManager(context)
            layoutManager.flexDirection = FlexDirection.ROW
            layoutManager.justifyContent = JustifyContent.FLEX_END

            adapter = categoriesAdapter
        }



    }


}