package com.example.filmshelper.data.repository

import android.net.Uri
import com.example.filmshelper.domain.repository.ProfileRepository
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor() : ProfileRepository {
    override suspend fun getPhotoUri(storageRef : StorageReference,auth: FirebaseAuth): Uri {
        return Tasks.await(storageRef.child("images/${auth.currentUser?.uid}profilePicture").downloadUrl)
    }
}