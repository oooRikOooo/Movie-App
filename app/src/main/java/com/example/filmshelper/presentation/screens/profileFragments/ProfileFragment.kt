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
import com.example.filmshelper.databinding.FragmentProfileBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso

class ProfileFragment : Fragment() {

    private lateinit var binding : FragmentProfileBinding

    private val viewModel : ProfileViewModel by viewModels()

    private var storageRef = Firebase.storage.reference

    override fun onAttach(context: Context) {
        //context.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentProfileBinding.inflate(inflater, container, false)

        /*if (viewModel.auth.currentUser == null){
            findNavController().navigate(R.id.action_profileFragment_to_profileSignUpFragment)
        }*/

        binding.textViewEmail.text = viewModel.auth.currentUser?.email
        binding.textViewProfileName.text = viewModel.auth.currentUser?.displayName
        //binding.imageViewProfilePhoto.setImageURI(viewModel.auth.currentUser?.photoUrl)
        Picasso.get().load(viewModel.auth.currentUser?.photoUrl).fit().centerCrop().into(binding.imageViewProfilePhoto)
        /*storageRef.child("images/${viewModel.auth.currentUser?.uid}profilePicture").downloadUrl.addOnCompleteListener {
            //Picasso.get().load(viewModel.auth.currentUser?.photoUrl).into(binding.imageViewProfilePhoto)
            Picasso.get().load(it.result).fit().centerCrop().into(binding.imageViewProfilePhoto)
        }*/

        binding.logout.setOnClickListener {
            viewModel.auth.signOut()
            Toast.makeText(requireContext(), "Successfully logout", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_profileFragment_to_profileSignInFragment)
            //findNavController().popBackStack(R.id.profileSignInFragment, false)
        }

        binding.editProfile.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
        }

        return binding.root
    }

}