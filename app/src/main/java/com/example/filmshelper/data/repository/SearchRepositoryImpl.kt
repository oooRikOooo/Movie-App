package com.example.filmshelper.data.repository

import com.example.filmshelper.data.ApiService
import com.example.filmshelper.data.models.allFilmsWithQuery.FilmsWithQuery
import com.example.filmshelper.domain.repository.SearchRepository
import java.io.IOException
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : SearchRepository {
    override suspend fun getAllFilmsWithQuery(
        title: String,
        titleType: String,
        genres: String
    ): Result<FilmsWithQuery> {
        return try {
            Result.success(apiService.getFilmsWithQuery(title, titleType, genres))
        } catch (e: IOException) {
            Result.failure(e)
        }
    }
}