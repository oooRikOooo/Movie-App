package com.example.filmshelper.data.models

import com.example.filmshelper.data.models.popularMovies.ItemPopularMovies

class ViewStates {
    sealed class ViewStatePopularMovies{
        data class Success(val data: List<ItemPopularMovies>) : ViewStatePopularMovies()
        object NoData: ViewStatePopularMovies()
        object Loading: ViewStatePopularMovies()
        data class Error(val error: Throwable): ViewStatePopularMovies()
    }
}
