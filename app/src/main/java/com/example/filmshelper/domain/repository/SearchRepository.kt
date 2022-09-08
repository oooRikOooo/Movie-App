package com.example.filmshelper.domain.repository

import com.example.filmshelper.data.models.allFilmsWithQuery.FilmsWithQuery

interface SearchRepository {

    suspend fun getAllFilmsWithQuery(
        title: String,
        titleType: String,
        genres: String
    ): Result<FilmsWithQuery>
}