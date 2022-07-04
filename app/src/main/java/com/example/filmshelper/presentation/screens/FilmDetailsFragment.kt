package com.example.filmshelper.presentation.screens

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.filmshelper.presentation.screens.mainFragment.MainFragmentViewModel
import com.example.filmshelper.presentation.screens.mainFragment.MainFragmentViewModelFactory
import com.example.filmshelper.R
import com.example.filmshelper.appComponent
import com.example.filmshelper.data.models.FirebaseUserFavouriteFilms
import com.example.filmshelper.databinding.FragmentFilmDetailsBinding
import com.example.filmshelper.presentation.adapters.CategoriesAdapter
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.squareup.picasso.Picasso
import javax.inject.Inject


class FilmDetailsFragment : Fragment() {

    private lateinit var binding: FragmentFilmDetailsBinding

    private val viewModel: MainFragmentViewModel by viewModels {
        factory.create()
    }

    private val database = Firebase.firestore
    private var user: FirebaseUser? = Firebase.auth.currentUser

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
        if (user != null){
            isFilmFavourite(filmId!!)
        }
        setOnClickListeners()
        setupAdapter()
        getData()

        return binding.root
    }


    private fun setOnClickListeners() {
        binding.apply {
            imageButtonFavourites.setOnClickListener {
                if (user != null) {
                    when (viewModel.filmById.value) {

                        is MainFragmentViewModel.ViewStateMovieById.Success -> {
                            val item =
                                (viewModel.filmById.value as MainFragmentViewModel.ViewStateMovieById.Success)

                            //addFavouriteFilm(item)
                            setFavouriteFilmState(item)

                        }
                        is MainFragmentViewModel.ViewStateMovieById.Error -> {
                            Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                        }
                        MainFragmentViewModel.ViewStateMovieById.Loading -> {
                            Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show()
                        }
                        MainFragmentViewModel.ViewStateMovieById.NoData -> {
                            Toast.makeText(requireContext(), "No data", Toast.LENGTH_SHORT).show()
                        }
                        null -> {
                            Toast.makeText(requireContext(), "Null", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "Login in your account", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

    private fun setFavouriteFilmState(film: MainFragmentViewModel.ViewStateMovieById.Success) {
        database.collection("users").document(user!!.uid)
            .collection("favouriteFilms").get().addOnSuccessListener {
                val taskList: List<FirebaseUserFavouriteFilms> =
                    it.toObjects(FirebaseUserFavouriteFilms::class.java)
                taskList.forEach { item ->

                    if (item.id == film.data.id) {
                        Log.d("riko", item.id)
                        Log.d("riko", film.data.id)
                        Log.d("riko", "removing")
                        removeFavouriteFilm(film)
                    }
                }

                val count = taskList.filter { filterItem ->
                    filterItem.id == film.data.id
                }.size

                if (count == 0){
                    addFavouriteFilm(film)
                }


            }
    }


    private fun removeFavouriteFilm(item: MainFragmentViewModel.ViewStateMovieById.Success) {
        database.collection("users").document(user!!.uid)
            .collection("favouriteFilms").document(item.data.id).delete()
            .addOnSuccessListener {
                binding.imageButtonFavourites.setImageResource(R.drawable.ic_baseline_favorite_not_pressed_24)
            }.addOnFailureListener {
                Toast.makeText(
                    requireContext(),
                    "Error",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun addFavouriteFilm(item: MainFragmentViewModel.ViewStateMovieById.Success) {

        val film = FirebaseUserFavouriteFilms(
            item.data.id,
            item.data.image,
            item.data.imDbRating ?: "no info",
            item.data.title
        )
        database.collection("users").document(user!!.uid)
            .collection("favouriteFilms")
            .document(film.id).set(film).addOnSuccessListener {
                Toast.makeText(
                    requireContext(),
                    "Added successfully",
                    Toast.LENGTH_SHORT
                ).show()
                binding.imageButtonFavourites.setImageResource(R.drawable.ic_baseline_favorite_24)
            }.addOnFailureListener {
                Toast.makeText(
                    requireContext(),
                    "Not Added",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d("riko", it.toString())
            }
    }

    private fun isFilmFavourite(id: String) {
        database.collection("users").document(user!!.uid)
            .collection("favouriteFilms").get().addOnSuccessListener {
                val taskList: List<FirebaseUserFavouriteFilms> =
                    it.toObjects(FirebaseUserFavouriteFilms::class.java)
                /*taskList.forEach { item ->
                    if (item.id == id) {
                        binding.imageButtonFavourites.setImageResource(R.drawable.ic_baseline_favorite_24)
                    } else {
                        binding.imageButtonFavourites.setImageResource(R.drawable.ic_baseline_favorite_not_pressed_24)
                    }

                }*/
                val count = taskList.filter { filterItem ->
                    filterItem.id == id
                }.size

                if (count == 0){
                    binding.imageButtonFavourites.setImageResource(R.drawable.ic_baseline_favorite_not_pressed_24)
                } else {
                    binding.imageButtonFavourites.setImageResource(R.drawable.ic_baseline_favorite_24)
                }

            }
    }

    private fun getData() {

        /*viewModel.youtubeTrailer.observe(viewLifecycleOwner) {
            when (it) {
                is MainFragmentViewModel.ViewStateYoutubeTrailerById.Error -> {
                    Log.d("riko", "${it.error}1")
                }
                MainFragmentViewModel.ViewStateYoutubeTrailerById.Loading -> {

                    setViewAndProgressBarVisibility(
                        binding.appBarLayout, View.INVISIBLE,
                        binding.progressBarVideo, View.VISIBLE
                    )
                    Log.d("riko", "Loading1")
                }
                MainFragmentViewModel.ViewStateYoutubeTrailerById.NoData -> {
                    Log.d("riko", "NoData1")
                }
                is MainFragmentViewModel.ViewStateYoutubeTrailerById.Success -> {

                    binding.infoTrailer = it.data

                    *//*binding.youtubePlayer.addYouTubePlayerListener(object :
                        AbstractYouTubePlayerListener() {
                        override fun onReady(youTubePlayer: YouTubePlayer) {
                            youTubePlayer.cueVideo(it.data.videoId, 0f)
                        }
                    })*//*

                    setViewAndProgressBarVisibility(
                        binding.appBarLayout, View.VISIBLE,
                        binding.progressBarVideo, View.INVISIBLE
                    )

                }
            }


        }*/

        viewModel.filmById.observe(viewLifecycleOwner) {

            when (it) {
                is MainFragmentViewModel.ViewStateMovieById.Error -> {
                    Log.d("riko", "${it.error}2")
                }
                MainFragmentViewModel.ViewStateMovieById.Loading -> {
                    Log.d("riko", "Loading2")
                    setViewAndProgressBarVisibility(
                        binding.nestedScroll, View.INVISIBLE,
                        binding.progressBarNestedScroll, View.VISIBLE
                    )

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
                        Picasso.get().load(it.data.image).noFade().fit().centerCrop().into(mainImageView)
                        Log.d("riko", it.data.image)
                        if (it.data.languages.contains(",")) {

                            val kept: String =
                                it.data.languages.substring(0, it.data.languages.indexOf(","))
                            Log.d("riko", kept)
                            textViewLanguage.text = kept

                        } else textViewLanguage.text = it.data.languages

                        textViewContentRating.text = it.data.contentRating
                        textViewDescription.text = it.data.plot


                    }
                    setViewAndProgressBarVisibility(
                        binding.nestedScroll, View.VISIBLE,
                        binding.progressBarNestedScroll, View.INVISIBLE
                    )

                }

            }

        }

    }

    private fun setViewAndProgressBarVisibility(
        view: View,
        statusView: Int,
        progressBar: ProgressBar,
        statusBar: Int
    ) {
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