package com.example.filmshelper.presentation.screens.profileFragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.filmshelper.R
import com.example.filmshelper.appComponent
import com.example.filmshelper.databinding.FragmentEditProfileBinding
import com.google.firebase.auth.UserProfileChangeRequest
import com.squareup.picasso.Picasso
import javax.inject.Inject

class EditProfileFragment : Fragment() {

    private lateinit var binding: FragmentEditProfileBinding

    private val viewModel : ProfileViewModel by viewModels{
        factory.create()
    }

    @Inject
    lateinit var factory: ProfileViewModelFactory.Factory

    private var photoUri: Uri? = null

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)

        binding.editTextPersonName.setText(viewModel.auth.currentUser?.displayName)
        Picasso.get().load(viewModel.auth.currentUser!!.photoUrl).fit().centerCrop().into(binding.imageViewProfilePhoto)
        photoUri = viewModel.auth.currentUser?.photoUrl
        setOnClickListeners()

        return binding.root
    }

    private fun setOnClickListeners(){
        binding.apply {
            buttonUpdate.setOnClickListener {
                if (isEmpty()) {
                    Toast.makeText(requireContext(), "Fill all fields", Toast.LENGTH_SHORT).show()
                } else {
                    val name = editTextPersonName.text

                    uploadPhoto(name)
                }
            }

            fabChangePhoto.setOnClickListener {
                Toast.makeText(requireContext(), "Working", Toast.LENGTH_SHORT).show()
                pickPhoto()
            }
        }
    }

    private fun uploadPhoto(name: Editable) {
        val ref = viewModel.storageRef.child("images/${viewModel.auth.currentUser?.uid}profilePicture")
        if (photoUri == viewModel.auth.currentUser?.photoUrl){
            updateProfile(name)
        } else {
            photoUri?.let { ref.putFile(it) }?.addOnProgressListener {
                binding.progressBar.visibility = View.VISIBLE
            }?.addOnSuccessListener {
                binding.progressBar.visibility = View.GONE
                updateProfile(name)
            }
        }
    }

    private fun updateProfile(name: Editable) {

        viewModel.getPhotoUri()

        viewModel.photoUrl.observe(viewLifecycleOwner){
            Log.d("riko", it.toString())
            if (it == viewModel.auth.currentUser?.photoUrl){
                Log.d("riko","1")
                val updates = UserProfileChangeRequest.Builder().setDisplayName(name.toString())
                    .build()

                viewModel.auth.currentUser?.updateProfile(updates)?.addOnCompleteListener { update ->
                    if (update.isSuccessful){
                        Toast.makeText(requireContext(), "Successfully updated", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_editProfileFragment_to_profileFragment)
                    } else {
                        Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Log.d("riko","2")
                val updates = UserProfileChangeRequest.Builder().setDisplayName(name.toString()).setPhotoUri(it)
                    .build()

                viewModel.auth.currentUser?.updateProfile(updates)?.addOnCompleteListener { update ->
                    if (update.isSuccessful){
                        Toast.makeText(requireContext(), "Successfully updated", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_editProfileFragment_to_profileFragment)
                    } else {
                        Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }


    }

    private fun isEmpty(): Boolean {
        binding.apply {
            return (TextUtils.isEmpty(editTextPersonName.text.toString()))
        }

    }

    private fun pickPhoto(){
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        resultLauncher.launch(intent)
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {

            val data : Uri? = result.data!!.data

            photoUri = data

            Picasso.get().load(photoUri).fit().centerCrop().into(binding.imageViewProfilePhoto)
        }
    }


}