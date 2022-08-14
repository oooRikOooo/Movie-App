package com.example.filmshelper.presentation.screens.mainFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmshelper.data.models.filmDetails.FilmDetails
import com.example.filmshelper.data.models.nowShowingMovies.ItemNowShowingMovies
import com.example.filmshelper.data.models.popular.popularMovies.ItemPopularMovies
import com.example.filmshelper.data.models.popular.popularTvShows.ItemPopularTvShows
import com.example.filmshelper.data.models.youtubeTrailer.YoutubeTrailer
import com.example.filmshelper.domain.repository.MainScreenRepository
import com.example.filmshelper.utils.ViewState
import com.example.filmshelper.utils.ViewStateWithList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class MainFragmentViewModel @Inject constructor(
    private val repository: MainScreenRepository
) : ViewModel() {

    val listNowShowingMovies: LiveData<ViewStateWithList<ItemNowShowingMovies>>
        get() = _listNowShowingMovies

    val listPopularMovies: LiveData<ViewStateWithList<ItemPopularMovies>>
        get() = _listPopularMovies

    val listPopularTvShows: LiveData<ViewStateWithList<ItemPopularTvShows>>
        get() = _listPopularTvShows


    val filmById: LiveData<ViewState<FilmDetails>>
        get() = _filmById

    val youtubeTrailer: LiveData<ViewState<YoutubeTrailer>>
        get() = _youtubeTrailer

    private val _listNowShowingMovies = MutableLiveData<ViewStateWithList<ItemNowShowingMovies>>()
    private val _listPopularMovies = MutableLiveData<ViewStateWithList<ItemPopularMovies>>()
    private val _listPopularTvShows = MutableLiveData<ViewStateWithList<ItemPopularTvShows>>()
    private val _filmById = MutableLiveData<ViewState<FilmDetails>>()
    private val _youtubeTrailer = MutableLiveData<ViewState<YoutubeTrailer>>()

    init {
        getNowShowingMovies()
        getPopularMovies()
        getPopularTvShows()
    }

    fun getNowShowingMovies() = viewModelScope.launch(Dispatchers.IO) {
        _listNowShowingMovies.postValue(ViewStateWithList.Loading)
        val result = repository.getMoviesInTheaters()

        if (result.isSuccess) {
            result.getOrNull()?.results?.let {
                _listNowShowingMovies.postValue(ViewStateWithList.Success(it))
                it.forEach { itemNowShowingFilm ->
                    repository.addOrUpdateLocaleNowShowingFilms(itemNowShowingFilm)

                }
            } ?: run{
                _listNowShowingMovies.postValue(ViewStateWithList.NoData)
            }

        }

        else {
            _listNowShowingMovies.postValue(ViewStateWithList.Error(result.exceptionOrNull()!!))
        }
    }

    fun getPopularMovies() = viewModelScope.launch(Dispatchers.IO){
        _listPopularMovies.postValue(ViewStateWithList.Loading)
        val result = repository.getPopularMovies()

        if (result.isSuccess){
            result.getOrNull()?.results?.let {
                _listPopularMovies.postValue(ViewStateWithList.Success(it))
                it.forEach{ itemPopularFilm ->
                    repository.addOrUpdateLocalePopularFilms(itemPopularFilm)
                }
            } ?: run {
                _listPopularMovies.postValue(ViewStateWithList.NoData)
            }

        } else {
            _listPopularMovies.postValue(ViewStateWithList.Error(result.exceptionOrNull()!!))
        }
    }

    fun getPopularTvShows() = viewModelScope.launch(Dispatchers.IO) {
        _listPopularTvShows.postValue(ViewStateWithList.Loading)
        val result = repository.getPopularTvShow()

        if (result.isSuccess){
            result.getOrNull()?.results?.let {
                _listPopularTvShows.postValue(ViewStateWithList.Success(it))
                it.forEach { itemPopularTvShows ->
                    repository.addOrUpdateLocalePopularTvShows(itemPopularTvShows)
                }
            } ?: run {
                _listPopularTvShows.postValue(ViewStateWithList.NoData)
            }
        } else {
            _listPopularTvShows.postValue(ViewStateWithList.Error(result.exceptionOrNull()!!))
        }
    }

    fun getMovieById(id: String) = viewModelScope.launch {
        _filmById.postValue(ViewState.Loading)

        val result = repository.getMovieById(id)

        if (result.isSuccess) {
            result.getOrNull()?.let {
                _filmById.postValue(ViewState.Success(it))
            } ?: run {
                _filmById.postValue(ViewState.NoData)
            }
        }

        else{
            _filmById.postValue(ViewState.Error(result.exceptionOrNull()!!))
        }

    }

    fun getYoutubeTrailerById(id: String) = viewModelScope.launch {
        _youtubeTrailer.postValue(ViewState.Loading)

        val result = repository.getMovieTrailerById(id)

        if (result.isSuccess){
            result.getOrNull()?.let {
                _youtubeTrailer.postValue(ViewState.Success(it))
            } ?: run{
                _youtubeTrailer.postValue(ViewState.NoData)
            }
        }

        else{
            _youtubeTrailer.postValue(ViewState.Error(result.exceptionOrNull()!!))
        }
    }



}