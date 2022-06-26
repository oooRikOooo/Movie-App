package com.example.filmshelper

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.filmshelper.domain.repository.MainScreenRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class MainViewModelFactory @AssistedInject constructor(
    private val repository: MainScreenRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        require(modelClass == MainViewModel::class.java)
        return MainViewModel( repository) as T
    }

    @AssistedFactory
    interface Factory {

        fun create(): MainViewModelFactory
    }
}