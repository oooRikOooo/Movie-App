package com.example.filmshelper.presentation.screens.searchFragments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.filmshelper.domain.repository.SearchRepository
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class SearchViewModelFactory @AssistedInject constructor(
    private val repository: SearchRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass == SearchViewModel::class.java)
        return SearchViewModel(repository) as T
    }

    @AssistedFactory
    interface Factory {

        fun create(): SearchViewModelFactory
    }
}