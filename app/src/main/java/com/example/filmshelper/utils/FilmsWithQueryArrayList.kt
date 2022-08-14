package com.example.filmshelper.utils

import java.util.*

class FilmsWithQueryArrayList(vararg value: String) : ArrayList<String>() {

    init {
        this.addAll(value)
    }

    fun withoutBracketsToString(): String {
        return Arrays.toString(this.toArray()).replace("[", "").replace("]", "")
    }
}