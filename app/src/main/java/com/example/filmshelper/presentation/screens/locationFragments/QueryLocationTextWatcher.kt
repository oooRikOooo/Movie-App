package com.example.filmshelper.presentation.screens.locationFragments

import android.widget.SearchView
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class QueryLocationTextWatcher : SearchView.OnQueryTextListener {

    private val textChangeSubject = PublishSubject.create<String>()

    init {
        textChangeSubject.onNext("")
    }

    fun queryChangeObserver(): Observable<String> {
        return textChangeSubject.debounce(200, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query.isNullOrEmpty()) textChangeSubject.onNext("")
        else textChangeSubject.onNext(query.toString())
        return false
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        return false
    }
}