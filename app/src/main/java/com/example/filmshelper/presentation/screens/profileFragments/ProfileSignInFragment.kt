package com.example.filmshelper.presentation.screens.profileFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.filmshelper.R
import com.example.filmshelper.databinding.FragmentProfileSignInBinding


class ProfileSignInFragment : Fragment() {

    private lateinit var binding: FragmentProfileSignInBinding

    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentProfileSignInBinding.inflate(inflater, container, false)

        setOnClickListeners()

        return binding.root
    }

    private fun setOnClickListeners(){
        binding.textViewToSignIn.setOnClickListener {
            findNavController().navigate(R.id.action_profileSignInFragment_to_profileSignUpFragment)
            //findNavController().popBackStack(R.id.profileSignUpFragment, false)
        }

        binding.buttonSignUp.setOnClickListener {
            val email = binding.editTextTextEmailAddress.text.toString()
            val password = binding.editTextTextPassword.text.toString()
            viewModel.auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful){
                    Toast.makeText(requireContext(), "Successfully login", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_profileSignInFragment_to_profileFragment)
                    //findNavController().popBackStack(R.id.profileFragment, false)
                } else {
                    Toast.makeText(requireContext(), "Wrong password", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}