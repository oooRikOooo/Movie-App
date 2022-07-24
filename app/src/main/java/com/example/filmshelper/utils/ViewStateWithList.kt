package com.example.filmshelper.utils



sealed class ViewStateWithList<out T> where T: Any?{
    data class Success<T>(val data: List<T>) : ViewStateWithList<T>()
    object NoData: ViewStateWithList<Nothing>()
    object Loading: ViewStateWithList<Nothing>()
    data class Error(val error: Throwable): ViewStateWithList<Nothing>()
}

sealed class ViewState<out T> where T: Any?{
    data class Success<T>(val data: T) : ViewState<T>()
    object NoData: ViewState<Nothing>()
    object Loading: ViewState<Nothing>()
    data class Error(val error: Throwable): ViewState<Nothing>()
}
