package com.example.filmshelper.presentation.screens.profileFragments

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.filmshelper.R
import com.example.filmshelper.appComponent
import com.example.filmshelper.databinding.FragmentProfileSignUpBinding
import javax.inject.Inject

class ProfileSignUpFragment : Fragment() {

    private lateinit var binding : FragmentProfileSignUpBinding

    private val viewModel : ProfileViewModel by viewModels{
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

        binding = FragmentProfileSignUpBinding.inflate(inflater, container, false)
        setOnClickListeners()

        return binding.root
    }

    private fun setOnClickListeners(){
        binding.buttonSignUp.setOnClickListener {
            if (isEmpty()) {
                Toast.makeText(requireContext(), "Fill all fields", Toast.LENGTH_SHORT).show()
            } else {
                val email = binding.editTextTextEmailAddress.text.toString()
                val password = binding.editTextTextPassword.text.toString()
                createUser(email, password)
            }

        }

        binding.textViewToSignIn.setOnClickListener {
            findNavController().navigate(R.id.action_profileSignUpFragment_to_profileSignInFragment)

        }
    }

    private fun createUser(email : String, password: String){
        viewModel.auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    Toast.makeText(requireContext(), "Successfully registered", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_profileSignUpFragment_to_profileFragment)
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