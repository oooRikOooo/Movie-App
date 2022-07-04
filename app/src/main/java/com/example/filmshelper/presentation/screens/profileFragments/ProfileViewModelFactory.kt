package com.example.filmshelper.presentation.screens.profileFragments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.filmshelper.domain.repository.ProfileRepository
import com.example.filmshelper.presentation.screens.mainFragment.MainFragmentViewModel
import com.example.filmshelper.presentation.screens.mainFragment.MainFragmentViewModelFactory
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class ProfileViewModelFactory @AssistedInject constructor(
    private val repository: ProfileRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        require(modelClass == ProfileViewModel::class.java)
        return ProfileViewModel( repository) as T
    }

    @AssistedFactory
    interface Factory {

        fun create(): ProfileViewModelFactory
    }
}