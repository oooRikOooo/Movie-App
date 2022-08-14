package com.example.filmshelper.presentation.screens

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.filmshelper.R
import com.example.filmshelper.appComponent
import com.example.filmshelper.data.models.DisplayableItem
import com.example.filmshelper.data.models.FirebaseUserFavouriteFilms
import com.example.filmshelper.data.models.filmDetails.FilmDetails
import com.example.filmshelper.databinding.FragmentFilmDetailsBinding
import com.example.filmshelper.presentation.adapters.AdapterDelegatesDetails
import com.example.filmshelper.presentation.adapters.CategoriesAdapter
import com.example.filmshelper.presentation.screens.mainFragment.MainFragmentViewModel
import com.example.filmshelper.presentation.screens.mainFragment.MainFragmentViewModelFactory
import com.example.filmshelper.utils.ViewState
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.squareup.picasso.Picasso
import javax.inject.Inject


class FilmDetailsFragment : Fragment() {

    private lateinit var binding: FragmentFilmDetailsBinding

    val args: FilmDetailsFragmentArgs by navArgs()


    private val viewModel: MainFragmentViewModel by viewModels {
        factory.create()
    }

    private val database = Firebase.firestore
    private var user: FirebaseUser? = Firebase.auth.currentUser

    @Inject
    lateinit var factory: MainFragmentViewModelFactory.Factory

    private val categoriesAdapter = CategoriesAdapter()
    private val castAdapter = ListDelegationAdapter(
        AdapterDelegatesDetails().castCreatorsAdapterDelegate(),
        AdapterDelegatesDetails().castDirectorsAdapterDelegate(),
        AdapterDelegatesDetails().castActorsAdapterDelegate()
    )
    private val similarAdapter = ListDelegationAdapter(
        AdapterDelegatesDetails().similarFilmsAdapterDelegate()
    )

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentFilmDetailsBinding.inflate(layoutInflater, container, false)

        val filmId = args.filmId

        viewModel.getMovieById(filmId.toString())

        if (user != null) {
            isFilmFavourite(filmId!!)
        }
        setOnClickListeners(filmId)
        setupAdapter()
        getData()



        return binding.root
    }


    private fun setOnClickListeners(filmId: String?) {
        binding.apply {
            imageButtonFavourites.setOnClickListener {
                if (user != null) {
                    when (viewModel.filmById.value) {

                        is ViewState.Success -> {
                            val item =
                                (viewModel.filmById.value as ViewState.Success)


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
                    Toast.makeText(requireContext(), "Login in your account", Toast.LENGTH_SHORT)
                        .show()
                }

            }

            imageButtonPlayTrailer.setOnClickListener {
                startActivity(
                    Intent(requireActivity(), TrailerActivity::class.java)
                        .putExtra("filmId", filmId)
                )
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

                if (count == 0) {
                    addFavouriteFilm(film)
                }


            }
    }


    private fun removeFavouriteFilm(item: ViewState.Success<FilmDetails>) {
        database.collection("users").document(user!!.uid)
            .collection("favouriteFilms").document(item.data.id).delete()
            .addOnSuccessListener {
                binding.imageButtonFavourites.setImageResource(R.drawable.ic_baseline_favorite_not_pressed_24)
                Snackbar.make(binding.root, "Removed favourite film", Snackbar.LENGTH_SHORT).show()
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
                binding.imageButtonFavourites.setImageResource(R.drawable.ic_baseline_favorite_24)
                Snackbar.make(binding.root, "Added favourite film", Snackbar.LENGTH_SHORT).show()
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

                val count = taskList.filter { filterItem ->
                    filterItem.id == id
                }.size

                if (count == 0) {
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
                    postponeEnterTransition()
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

                        val commonList = mutableListOf<DisplayableItem>()

                        if (it.data.tvSeriesInfo == null || it.data.tvSeriesInfo.creatorList.isEmpty()) {
                            commonList.addAll(it.data.directorList)
                        } else commonList.addAll(it.data.tvSeriesInfo.creatorList)

                        commonList.addAll(it.data.actorList)

                        castAdapter.items = commonList
                        similarAdapter.items = it.data.similars
                        categoriesAdapter.list = it.data.genreList
                        setData(it)

                    }
                    setViewAndProgressBarVisibility(
                        binding.nestedScroll, View.VISIBLE,
                        binding.progressBarNestedScroll, View.INVISIBLE
                    )

                }

            }

        }

    }

    private fun setData(it: ViewState.Success<FilmDetails>) {
        binding.apply {
            textViewFilmTitle.text = it.data.title
            textViewRatingImdb.text =
                requireContext().getString(R.string.rating_imdb, it.data.imDbRating)
            textViewLength.text = it.data.runtimeMins + " min"
            Picasso.get().load(it.data.image).noFade().fit().centerCrop().into(mainImageView)
            if (it.data.languages.contains(",")) {

                val kept: String =
                    it.data.languages.substring(0, it.data.languages.indexOf(","))
                textViewLanguage.text = kept

            } else textViewLanguage.text = it.data.languages

            textViewContentRating.text = it.data.contentRating
            textViewDescription.text = it.data.plot
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

        binding.recyclerViewCast.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            isNestedScrollingEnabled = false
            adapter = castAdapter
        }

        binding.recyclerViewSimilar.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            isNestedScrollingEnabled = false
            adapter = similarAdapter
        }


    }


}