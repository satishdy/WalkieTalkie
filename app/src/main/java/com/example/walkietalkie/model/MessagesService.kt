package com.example.walkietalkie.model

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MessagesService {
    const val BASE_URL = "https://raw.githubusercontent.com"

    fun getMessagesService(): MessagesApi {
        val gson = GsonBuilder()
            .setLenient()
            .create()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(MessagesApi::class.java)
    }
}