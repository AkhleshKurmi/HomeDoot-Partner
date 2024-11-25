package com.example.akhleshkumar.homedootpartner.models.user

import com.google.gson.annotations.SerializedName


data class UpdatePasswordResponse(
    @SerializedName("data")
    var `data`: Any,
    @SerializedName("message")
    var message: String,
    @SerializedName("success")
    var success: Boolean
)