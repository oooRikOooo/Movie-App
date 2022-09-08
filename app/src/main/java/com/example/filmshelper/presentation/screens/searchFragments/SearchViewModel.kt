package com.example.filmshelper.presentation.screens.searchFragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmshelper.data.models.allFilmsWithQuery.FilmsWithQuery
import com.example.filmshelper.data.models.allFilmsWithQuery.ItemFilmsWithQuery
import com.example.filmshelper.domain.repository.SearchRepository
import com.example.filmshelper.utils.FilmsWithQueryArrayList
import com.example.filmshelper.utils.ViewStateWithList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val repository: SearchRepository
) : ViewModel(

) {
    val categories: LiveData<FilmsWithQueryArrayList>
        get() = _categories

    val genres: LiveData<FilmsWithQueryArrayList>
        get() = _genres

    val films: LiveData<ViewStateWithList<ItemFilmsWithQuery>>
        get() = _films

    private val _categories = MutableLiveData<FilmsWithQueryArrayList>()
    private val _genres = MutableLiveData<FilmsWithQueryArrayList>()
    private val _films = MutableLiveData<ViewStateWithList<ItemFilmsWithQuery>>()

    init {
        _categories.value = FilmsWithQueryArrayList("feature")
        _genres.value = FilmsWithQueryArrayList()
    }

    fun addCategory(value: String) {
        _categories.value?.add(value)
    }

    fun removeCategory(value: String) {
        _categories.value?.remove(value)
    }

    fun addGenre(value: String) {
        _genres.value?.add(value)
    }

    fun removeGenre(value: String) {
        _genres.value?.remove(value)
    }

    fun checkIfContainValue(value: String, liveData: LiveData<FilmsWithQueryArrayList>): Boolean {
        return liveData.value?.contains(value) == true
    }

    fun getFilms() {
        viewModelScope.launch(Dispatchers.IO) {
            _films.postValue(ViewStateWithList.Loading)

            val categoriesValue = categories.value?.withoutBracketsToString() ?: ""

            val result = repository.getAllFilmsWithQuery("", categoriesValue, "")

            if (result.isSuccess) {
                result.getOrNull()?.results?.let {
                    _films.postValue(ViewStateWithList.Success(it))
                } ?: run {
                    _films.postValue(ViewStateWithList.NoData)
                }
            } else {
                _films.postValue(ViewStateWithList.Error(result.exceptionOrNull()!!))
            }
        }

    }

    fun getFilmsWithQuery(query: String) {
        viewModelScope.launch(Dispatchers.IO) {

            val categoriesValue: String? = categories.value?.withoutBracketsToString()

            val genresValue: String? = genres.value?.withoutBracketsToString()

            _films.postValue(ViewStateWithList.Loading)

            val result: Result<FilmsWithQuery> = if (categories.value?.isEmpty() == true) {
                repository.getAllFilmsWithQuery(query, "feature", genresValue.toString())
            } else if (genres.value?.isEmpty() == true) {
                repository.getAllFilmsWithQuery(query, categoriesValue.toString(), "")
            } else {
                repository.getAllFilmsWithQuery(
                    query,
                    categoriesValue.toString(),
                    genresValue.toString()
                )
            }


            if (result.isSuccess) {
                result.getOrNull()?.results?.let {
                    _films.postValue(ViewStateWithList.Success(it))
                } ?: run {
                    _films.postValue(ViewStateWithList.NoData)
                }
            } else {
                _films.postValue(ViewStateWithList.Error(result.exceptionOrNull()!!))
            }
        }
    }


}