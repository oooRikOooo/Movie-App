package com.example.filmshelper.data.repository

import android.net.Uri
import com.example.filmshelper.domain.repository.ProfileRepository
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.StorageReference
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor() : ProfileRepository {
    override suspend fun getPhotoUri(
        storageRef: StorageReference,
        auth: FirebaseAuth
    ): Result<Uri> {
        return try {
            Result.success(Tasks.await(storageRef.child("images/${auth.currentUser?.uid}profilePicture").downloadUrl))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}