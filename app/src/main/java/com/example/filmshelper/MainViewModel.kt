package com.example.filmshelper

import android.view.View
import androidx.constraintlayout.motion.utils.ViewState
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

    val listNowShowingMovies: LiveData<ViewStateNowShowingMovies>
        get() = _listNowShowingMovies

    val listPopularMovies: LiveData<ViewStatePopularMovies>
        get() = _listPopularMovies

    val filmById: LiveData<ViewStateMovieById>
        get() = _filmById

    val youtubeTrailer: LiveData<ViewStateYoutubeTrailerById>
        get() = _youtubeTrailer

    private val _listNowShowingMovies = MutableLiveData<ViewStateNowShowingMovies>()
    private val _listPopularMovies = MutableLiveData<ViewStatePopularMovies>()
    private val _filmById = MutableLiveData<ViewStateMovieById>()
    private val _youtubeTrailer = MutableLiveData<ViewStateYoutubeTrailerById>()

    /*init {
        getNowShowingMovies()
        getPopularMovies()
    }*/

    /*private fun getNowShowingMovies() = viewModelScope.launch{
        _listNowShowingMovies.postValue(repository.getMoviesInTheaters().items)
    }*/

    fun getNowShowingMovies() = viewModelScope.launch{
        _listNowShowingMovies.postValue(ViewStateNowShowingMovies.Loading)
        val result = repository.getMoviesInTheaters()

        if (result.isSuccess){
            result.getOrNull()?.items?.let {
                _listNowShowingMovies.postValue(ViewStateNowShowingMovies.Success(it))
            } ?: run{
                _listNowShowingMovies.postValue(ViewStateNowShowingMovies.NoData)
            }

        }

        else {
            _listNowShowingMovies.postValue(ViewStateNowShowingMovies.Error(result.exceptionOrNull()!!))
        }
    }

    fun getPopularMovies() = viewModelScope.launch{
        _listPopularMovies.postValue(ViewStatePopularMovies.Loading)
        val result = repository.getPopularMovies()

        if (result.isSuccess){
            result.getOrNull()?.items?.let {
                _listPopularMovies.postValue(ViewStatePopularMovies.Success(it))
            } ?: run{
                _listPopularMovies.postValue(ViewStatePopularMovies.NoData)
            }

        }

        else {
            _listPopularMovies.postValue(ViewStatePopularMovies.Error(result.exceptionOrNull()!!))
        }
    }

    fun getMovieById(id: String) = viewModelScope.launch {
        _filmById.postValue(ViewStateMovieById.Loading)

        val result = repository.getMovieById(id)

        if (result.isSuccess){
            result.getOrNull()?.let {
                _filmById.postValue(ViewStateMovieById.Success(it))
            } ?: run{
                _filmById.postValue(ViewStateMovieById.NoData)
            }
        }

        else{
            _filmById.postValue(ViewStateMovieById.Error(result.exceptionOrNull()!!))
        }

    }

    fun getYoutubeTrailerById(id: String) = viewModelScope.launch {
        _youtubeTrailer.postValue(ViewStateYoutubeTrailerById.Loading)

        val result = repository.getMovieTrailerById(id)

        if (result.isSuccess){
            result.getOrNull()?.let {
                _youtubeTrailer.postValue(ViewStateYoutubeTrailerById.Success(it))
            } ?: run{
                _youtubeTrailer.postValue(ViewStateYoutubeTrailerById.NoData)
            }
        }

        else{
            _youtubeTrailer.postValue(ViewStateYoutubeTrailerById.Error(result.exceptionOrNull()!!))
        }
    }

    /*private fun getPopularMovies() = viewModelScope.launch {
        _listPopularMovies.postValue(repository.getPopularMovies().items)
    }*/

    /*fun getMovieById(id: String) = viewModelScope.launch {
        _filmById.postValue(repository.getMovieById(id))
    }*/

    /*fun getYoutubeTrailerById(id: String) = viewModelScope.launch {
        _youtubeTrailer.postValue(repository.getMovieTrailerById(id))
    }*/

    sealed class ViewStateNowShowingMovies{
        data class Success(val data: List<ItemNowShowingMovies>) : ViewStateNowShowingMovies()
        object NoData: ViewStateNowShowingMovies()
        object Loading: ViewStateNowShowingMovies()
        data class Error(val error: Throwable): ViewStateNowShowingMovies()
    }

    sealed class ViewStatePopularMovies{
        data class Success(val data: List<ItemPopularMovies>) : ViewStatePopularMovies()
        object NoData: ViewStatePopularMovies()
        object Loading: ViewStatePopularMovies()
        data class Error(val error: Throwable): ViewStatePopularMovies()
    }

    sealed class ViewStateMovieById{
        data class Success(val data: FilmDetails) : ViewStateMovieById()
        object NoData: ViewStateMovieById()
        object Loading: ViewStateMovieById()
        data class Error(val error: Throwable): ViewStateMovieById()
    }

    sealed class ViewStateYoutubeTrailerById{
        data class Success(val data: YoutubeTrailer) : ViewStateYoutubeTrailerById()
        object NoData: ViewStateYoutubeTrailerById()
        object Loading: ViewStateYoutubeTrailerById()
        data class Error(val error: Throwable): ViewStateYoutubeTrailerById()
    }

}