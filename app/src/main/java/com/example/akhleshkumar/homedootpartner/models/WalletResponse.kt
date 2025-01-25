package com.example.akhleshkumar.homedootpartner.models

import com.google.gson.annotations.SerializedName

data class WalletResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: WalletDataResponse
)

data class WalletDataResponse(
    @SerializedName("razor_order_id") val razorOrderId: String,
    @SerializedName("razor_order_status") val razorOrderStatus: String,
    @SerializedName("total") val total: String
)
