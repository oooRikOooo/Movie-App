package com.example.filmshelper.data

import com.example.filmshelper.BuildConfig
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface FirebaseApiService {

    @Headers(
        "Authorization: key=${BuildConfig.SERVER_KEY}",
        "Content-Type: application/json"
    )

    @POST("/fcm/send")
    suspend fun getMessage(@Body firebaseJson: JsonObject): Response<JsonObject>
}