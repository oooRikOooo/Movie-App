package com.example.filmshelper.presentation.screens.profileFragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.filmshelper.R
import com.example.filmshelper.appComponent
import com.example.filmshelper.databinding.FragmentProfileBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import javax.inject.Inject

class ProfileFragment : Fragment() {

    private lateinit var binding : FragmentProfileBinding

    private val viewModel : ProfileViewModel by viewModels {
        factory.create()
    }

    @Inject
    lateinit var factory: ProfileViewModelFactory.Factory

    //private var storageRef = Firebase.storage.reference

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentProfileBinding.inflate(inflater, container, false)

        binding.textViewEmail.text = viewModel.auth.currentUser?.email
        binding.textViewProfileName.text = viewModel.auth.currentUser?.displayName
        Picasso.get().load(viewModel.auth.currentUser?.photoUrl).fit().centerCrop().into(binding.imageViewProfilePhoto)

        binding.logout.setOnClickListener {
            viewModel.auth.signOut()
            Toast.makeText(requireContext(), "Successfully logout", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_profileFragment_to_profileSignInFragment)
            //findNavController().popBackStack(R.id.profileSignInFragment, false)
        }

        binding.editProfile.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
        }

        binding.favouriteMovies.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_favouritesFilmsFragment)
        }

        return binding.root
    }

}