package com.example.filmshelper.presentation.screens.locationFragments

import com.example.filmshelper.data.ApiServiceLocation
import com.example.filmshelper.data.models.allMoviesTheatres.AllMoviesTheatre
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class LocationIntractorImpl : LocationContract.LocationIntractor {

    override fun getPlacesList(
        city: String,
        onFinishedListener: LocationContract.LocationIntractor.OnFinishedListener
    ) {

        val retrofit = Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val retrofitInstance = retrofit.create<ApiServiceLocation>()

        val call = retrofitInstance.getAllMovieTheatres(city)

        call.enqueue(object : Callback<AllMoviesTheatre> {
            override fun onResponse(
                call: Call<AllMoviesTheatre>,
                response: Response<AllMoviesTheatre>
            ) {
                if (response.isSuccessful) {
                    onFinishedListener.onFinished(response.body()!!.results)
                }
            }

            override fun onFailure(call: Call<AllMoviesTheatre>, t: Throwable) {
                onFinishedListener.onFailure(t)
            }

        })
    }
}