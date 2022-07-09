package com.example.filmshelper.presentation.screens.mainFragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.filmshelper.domain.repository.MainScreenRepository
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class MainFragmentViewModelFactory @AssistedInject constructor(
    private val repository: MainScreenRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass == MainFragmentViewModel::class.java)
        return MainFragmentViewModel(repository) as T
    }

    @AssistedFactory
    interface Factory {

        fun create(): MainFragmentViewModelFactory
    }
}