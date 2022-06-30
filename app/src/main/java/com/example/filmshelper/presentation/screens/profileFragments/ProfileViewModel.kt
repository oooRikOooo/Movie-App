package com.example.filmshelper.presentation.screens.profileFragments

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    var auth: FirebaseAuth = Firebase.auth
    var storageRef = Firebase.storage.reference

    val photoUrl: LiveData<Uri>
        get() = _photoUrl

    private val _photoUrl = MutableLiveData<Uri>()

    fun getPhotoUri(){
        viewModelScope.launch(Dispatchers.IO) {
            //return@async storageRef.child("images/${auth.currentUser?.uid}profilePicture").downloadUrl.result
            _photoUrl.postValue(Tasks.await(storageRef.child("images/${auth.currentUser?.uid}profilePicture").downloadUrl))
            //_photoUrl.postValue(storageRef.child("images/${auth.currentUser?.uid}profilePicture").downloadUrl.result)
        }
    }

}