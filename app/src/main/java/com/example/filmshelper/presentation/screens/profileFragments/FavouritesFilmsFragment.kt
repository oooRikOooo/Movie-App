package com.example.filmshelper.presentation.screens.profileFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.filmshelper.R
import com.example.filmshelper.data.models.FirebaseUserFavouriteFilms
import com.example.filmshelper.databinding.FragmentFavouritesFilmsBinding
import com.example.filmshelper.presentation.adapters.FavouriteFilmsAdapter
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class FavouritesFilmsFragment : Fragment() {

    private lateinit var binding : FragmentFavouritesFilmsBinding

    private val viewModel : ProfileViewModel by viewModels()

    private val favouriteFilmsAdapter = FavouriteFilmsAdapter()

    private val database = Firebase.firestore

    private var user: FirebaseUser? = Firebase.auth.currentUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavouritesFilmsBinding.inflate(inflater, container, false)
        setupAdapter()
        getDataAdapter()

        return binding.root
    }

    private fun setupAdapter(){
        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = favouriteFilmsAdapter
        }
    }

    private fun getDataAdapter(){
        database.collection("users").document(user!!.uid)
            .collection("favouriteFilms").get().addOnSuccessListener {
                val taskList: List<FirebaseUserFavouriteFilms> =
                    it.toObjects(FirebaseUserFavouriteFilms::class.java)
                favouriteFilmsAdapter.list = taskList
            }.addOnFailureListener {
                Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
            }

    }



}