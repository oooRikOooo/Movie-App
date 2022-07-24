package com.example.filmshelper.presentation.screens

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.filmshelper.R
import com.example.filmshelper.appComponent
import com.example.filmshelper.data.models.FirebaseUserFavouriteFilms
import com.example.filmshelper.data.models.filmDetails.FilmDetails
import com.example.filmshelper.databinding.FragmentFilmDetailsBinding
import com.example.filmshelper.presentation.adapters.CategoriesAdapter
import com.example.filmshelper.presentation.screens.mainFragment.MainFragmentViewModel
import com.example.filmshelper.presentation.screens.mainFragment.MainFragmentViewModelFactory
import com.example.filmshelper.utils.ViewState
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
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

                        is ViewState.Success -> {
                            val item =
                                (viewModel.filmById.value as ViewState.Success)

                            //addFavouriteFilm(item)
                            setFavouriteFilmState(item)

                        }
                        is ViewState.Error -> {
                            Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                        }
                        ViewState.Loading -> {
                            Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show()
                        }
                        ViewState.NoData -> {
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

    private fun setFavouriteFilmState(film: ViewState.Success<FilmDetails>) {
        database.collection("users").document(user!!.uid)
            .collection("favouriteFilms").get().addOnSuccessListener {
                val taskList: List<FirebaseUserFavouriteFilms> =
                    it.toObjects(FirebaseUserFavouriteFilms::class.java)
                taskList.forEach { item ->

                    if (item.id == film.data.id) {
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


    private fun removeFavouriteFilm(item: ViewState.Success<FilmDetails>) {
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

    private fun addFavouriteFilm(item: ViewState.Success<FilmDetails>) {

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
        viewModel.filmById.observe(viewLifecycleOwner) {

            when (it) {
                is ViewState.Error -> {
                    Log.d("riko", "${it.error}2")
                }
                ViewState.Loading -> {
                    Log.d("riko", "Loading2")
                    setViewAndProgressBarVisibility(
                        binding.nestedScroll, View.INVISIBLE,
                        binding.progressBarNestedScroll, View.VISIBLE
                    )

                }
                ViewState.NoData -> {
                    Log.d("riko", "NoData2")
                }
                is ViewState.Success -> {
                    binding.apply {

                        categoriesAdapter.list = it.data.genreList
                        textViewFilmTitle.text = it.data.title
                        textViewRatingImdb.text =
                            requireContext().getString(R.string.rating_imdb, it.data.imDbRating)
                        textViewLength.text = it.data.runtimeMins
                        Picasso.get().load(it.data.image).noFade().fit().centerCrop().into(mainImageView)
                        if (it.data.languages.contains(",")) {

                            val kept: String =
                                it.data.languages.substring(0, it.data.languages.indexOf(","))
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