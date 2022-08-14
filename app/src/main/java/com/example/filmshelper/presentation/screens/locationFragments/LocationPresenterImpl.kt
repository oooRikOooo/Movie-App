package com.example.filmshelper.presentation.screens.locationFragments

import android.location.Address
import com.example.filmshelper.data.models.allMoviesTheatres.Result
import com.google.android.gms.maps.model.Marker
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

class LocationPresenterImpl(
    private var mainView: LocationContract.LocationView?,
    private val locationIntractor: LocationContract.LocationIntractor,
    private val queryDataSource: Observable<String>
) : LocationContract.Presenter, LocationContract.LocationIntractor.OnFinishedListener {

    private val listOfMovieTheatres = ArrayList<Marker>()
    private val listOfAddresses = ArrayList<Address>()

    override fun addMovieTheatres(value: Marker) {
        listOfMovieTheatres.add(value)
    }

    override fun clearMovieTheatre() {
        listOfMovieTheatres.forEach {
            it.remove()
        }

        listOfMovieTheatres.clear()
    }

    override fun onDestroy() {
        mainView = null
    }

    override fun search() {
        //mainView?.searchCity(city, arrayList)
        // locationIntractor.getPlacesList(city, this)
        queryDataSource.subscribeOn(Schedulers.io())
            .map { query ->
                locationIntractor.getPlacesList(query, this)
                query
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                mainView?.searchCity(it, listOfAddresses)
            }

    }

    override fun requestData(city: String) {
        locationIntractor.getPlacesList(city, this)
    }

    override fun onFinished(data: List<Result>) {
        mainView?.setData(data)
    }

    override fun onFailure(throwable: Throwable) {
        mainView?.onResponseFailure(throwable)
    }
}