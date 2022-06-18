package com.example.filmshelper

import androidx.lifecycle.*
import com.example.filmshelper.data.models.FilmDetails.FilmDetails
import com.example.filmshelper.data.models.nowShowingMovies.ItemNowShowingMovies
import com.example.filmshelper.data.models.popularMovies.ItemPopularMovies
import com.example.filmshelper.data.models.youtubeTrailer.YoutubeTrailer
import com.example.filmshelper.domain.repository.MainScreenRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val repository: MainScreenRepository
) : ViewModel() {

    val listNowShowingMovies: LiveData<List<ItemNowShowingMovies>>
        get() = _listNowShowingMovies

    val listPopularMovies: LiveData<List<ItemPopularMovies>>
        get() = _listPopularMovies

    val filmById: LiveData<FilmDetails>
        get() = _filmById

    val youtubeTrailer: LiveData<YoutubeTrailer>
        get() = _youtubeTrailer

    private val _listNowShowingMovies = MutableLiveData<List<ItemNowShowingMovies>>()
    private val _listPopularMovies = MutableLiveData<List<ItemPopularMovies>>()
    private val _filmById = MutableLiveData<FilmDetails>()
    private val _youtubeTrailer = MutableLiveData<YoutubeTrailer>()

    init {
        getNowShowingMovies()
        getPopularMovies()
    }

    private fun getNowShowingMovies() = viewModelScope.launch{
        _listNowShowingMovies.postValue(repository.getMoviesInTheaters().items)
    }

    private fun getPopularMovies() = viewModelScope.launch {
        _listPopularMovies.postValue(repository.getPopularMovies().items)
    }

    fun getMovieById(id: String) = viewModelScope.launch {
        _filmById.postValue(repository.getMovieById(id))
    }

    fun getYoutubeTrailerById(id: String) = viewModelScope.launch {
        _youtubeTrailer.postValue(repository.getMovieTrailerById(id))
    }

}