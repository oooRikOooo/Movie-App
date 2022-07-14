package com.example.filmshelper.presentation.screens.profileFragments

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmshelper.domain.repository.ProfileRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    var auth: FirebaseAuth = Firebase.auth
    var storageRef = Firebase.storage.reference

    val photoUrl: LiveData<ViewStatePhotoUri>
        get() = _photoUrl

    private val _photoUrl = MutableLiveData<ViewStatePhotoUri>()

    fun getPhotoUri() {
        viewModelScope.launch(Dispatchers.IO) {
            _photoUrl.postValue(ViewStatePhotoUri.Loading)

            val result = profileRepository.getPhotoUri(storageRef, auth)
            if (result.isSuccess) {
                result.getOrNull()?.let {
                    _photoUrl.postValue(ViewStatePhotoUri.Success(it))
                } ?: run {
                    _photoUrl.postValue(ViewStatePhotoUri.NoData)
                }
            } else {
                _photoUrl.postValue(ViewStatePhotoUri.Error(result.exceptionOrNull()!!))
            }
        }
    }

    sealed class ViewStatePhotoUri {
        data class Success(val data: Uri) : ViewStatePhotoUri()
        object NoData : ViewStatePhotoUri()
        object Loading : ViewStatePhotoUri()
        data class Error(val error: Throwable) : ViewStatePhotoUri()
    }

}