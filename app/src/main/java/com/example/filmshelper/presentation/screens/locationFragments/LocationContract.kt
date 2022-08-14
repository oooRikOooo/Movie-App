package com.example.filmshelper.presentation.screens.locationFragments

import android.location.Address
import com.example.filmshelper.data.models.allMoviesTheatres.Result
import com.google.android.gms.maps.model.Marker

class LocationContract {

    interface Presenter {

        // val listOfMovieTheatres : ArrayList<MarkerOptions>

        fun onDestroy()

        fun search()

        fun requestData(city: String)

        fun addMovieTheatres(value: Marker)

        fun clearMovieTheatre()
    }

    interface LocationView {
        fun searchCity(city: String, arrayList: ArrayList<Address>)

        fun setData(data: List<Result>)

        fun onResponseFailure(throwable: Throwable)

    }

    interface LocationIntractor {

        interface OnFinishedListener {
            fun onFinished(data: List<Result>)

            fun onFailure(throwable: Throwable)
        }

        fun getPlacesList(city: String, onFinishedListener: OnFinishedListener)
    }
}