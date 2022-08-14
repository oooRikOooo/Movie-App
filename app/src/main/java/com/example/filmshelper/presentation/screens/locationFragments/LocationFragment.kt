package com.example.filmshelper.presentation.screens.locationFragments

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.filmshelper.R
import com.example.filmshelper.appComponent
import com.example.filmshelper.data.models.allMoviesTheatres.Result
import com.example.filmshelper.databinding.FragmentLocationBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException


class LocationFragment : Fragment(), LocationContract.LocationView {

    private lateinit var mMap: GoogleMap

    private lateinit var binding: FragmentLocationBinding

    private lateinit var presenter: LocationContract.Presenter

    private val queryWatcher = QueryLocationTextWatcher()

    private var currentCity: String? = null

    private val callback = OnMapReadyCallback { googleMap ->
        val kyiv = LatLng(50.4, 30.5)
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(kyiv))
        googleMap.setMinZoomPreference(10F)
        mMap = googleMap
    }

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentLocationBinding.inflate(layoutInflater, container, false)

        binding.searchView.setOnQueryTextListener(queryWatcher)
        presenter =
            LocationPresenterImpl(this, LocationIntractorImpl(), queryWatcher.queryChangeObserver())
        presenter.requestData("movie_theatre%${currentCity ?: "Kyiv"}")
        presenter.search()

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        return binding.root
    }

    override fun searchCity(city: String, arrayList: ArrayList<Address>) {

        currentCity = city

        presenter.requestData("movie_theatre%${currentCity ?: "Kyiv"}")

        val geocoder = Geocoder(requireContext())

        try {
            arrayList.addAll(geocoder.getFromLocationName(city, 1))
        } catch (e: IOException) {
            e.printStackTrace()
        }

        val address = arrayList.last()

        val latLng = LatLng(address.latitude, address.longitude)

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10F))

        binding.searchView.clearFocus()
    }

    override fun setData(data: List<Result>) {

        presenter.clearMovieTheatre()

        data.forEach {
            val marker = mMap.addMarker(
                MarkerOptions()
                    .position(
                        LatLng(
                            it.geometry.location.lat, it.geometry.location.lng
                        )
                    )
            )

            presenter.addMovieTheatres(marker!!)

        }

    }

    override fun onResponseFailure(throwable: Throwable) {
        Log.d("riko", throwable.message.toString())
    }

}