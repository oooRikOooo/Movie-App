package com.example.filmshelper.domain.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.StorageReference

interface ProfileRepository {

    suspend fun getPhotoUri(storageRef : StorageReference,auth: FirebaseAuth):Uri
}