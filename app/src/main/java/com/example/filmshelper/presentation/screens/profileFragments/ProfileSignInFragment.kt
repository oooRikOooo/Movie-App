package com.example.filmshelper.presentation.screens.profileFragments

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.filmshelper.R
import com.example.filmshelper.appComponent
import com.example.filmshelper.databinding.FragmentProfileSignInBinding
import javax.inject.Inject


class ProfileSignInFragment : Fragment() {

    private lateinit var binding: FragmentProfileSignInBinding

    private val viewModel: ProfileViewModel by viewModels{
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

        binding = FragmentProfileSignInBinding.inflate(inflater, container, false)

        val animation =
            AnimationUtils.loadAnimation(requireContext(), R.animator.fade_in_sign_screen)

        binding.signInContainer.startAnimation(animation)

        setOnClickListeners()

        return binding.root
    }

    private fun setOnClickListeners(){
        binding.textViewToSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_profileSignInFragment_to_profileSignUpFragment)

        }

        binding.buttonSignIn.setOnClickListener {
            if (isEmpty()) {
                Toast.makeText(requireContext(), "Fill all fields", Toast.LENGTH_SHORT).show()
            } else {
                val email = binding.editTextTextEmailAddress.text.toString()
                val password = binding.editTextTextPassword.text.toString()
                viewModel.auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(requireContext(), "Successfully login", Toast.LENGTH_SHORT)
                            .show()
                        findNavController().navigate(R.id.action_profileSignInFragment_to_profileFragment)
                    } else {
                        Toast.makeText(requireContext(), "Wrong password", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

        }
    }

    private fun isEmpty(): Boolean {
        binding.apply {
            return (TextUtils.isEmpty(editTextTextEmailAddress.text.toString()) ||
                    TextUtils.isEmpty(editTextTextPassword.text.toString()))
        }

    }

}