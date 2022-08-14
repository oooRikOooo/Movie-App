package com.example.filmshelper.presentation.screens.profileFragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.filmshelper.R
import com.example.filmshelper.appComponent
import com.example.filmshelper.databinding.FragmentProfileBinding
import com.squareup.picasso.Picasso
import javax.inject.Inject

class ProfileFragment : Fragment() {

    private lateinit var binding : FragmentProfileBinding

    private val viewModel : ProfileViewModel by viewModels {
        factory.create()
    }

    @Inject
    lateinit var factory: ProfileViewModelFactory.Factory

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

        binding.logout.textViewLogout.setOnClickListener {
            viewModel.auth.signOut()
            Toast.makeText(requireContext(), "Successfully logout", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_profileFragment_to_profileSignInFragment)
        }

        binding.editProfile.textViewEditProfile.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
        }

        binding.favouriteMovies.textViewFavouriteMovies.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_favouritesFilmsFragment)
        }

        return binding.root
    }

}