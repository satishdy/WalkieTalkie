package com.example.walkietalkie.model

import retrofit2.Response
import retrofit2.http.GET

interface MessagesApi {
    @GET("fluentstream/walkie_talkie_backend/main/mock_data.json")
    suspend fun getMessages(): Response<List<Message>>
}