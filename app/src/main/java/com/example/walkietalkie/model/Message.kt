package com.example.walkietalkie.model

import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("username_from")
    val userNameFrom: String?,

    @SerializedName("timestamp")
    val sentOn: String?,

    @SerializedName("recording")
    val message: String?,

    @SerializedName("username_to")
    val userNameTo: String?
)
