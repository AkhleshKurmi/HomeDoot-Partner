package com.example.akhleshkumar.homedootpartner.models.user

import com.google.gson.annotations.SerializedName


data class ForgotPasswordResponse(
    @SerializedName("data")
    var `data`: Data,
    @SerializedName("message")
    var message: String,
    @SerializedName("success")
    var success: Boolean
)
data class Data(
    @SerializedName("email")
    var email: Any,
    @SerializedName("mobile")
    var mobile: String,
    @SerializedName("VerificationCode")
    var verificationCode: Int
)