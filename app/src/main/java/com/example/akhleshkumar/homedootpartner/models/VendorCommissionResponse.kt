package com.example.akhleshkumar.homedootpartner.models
import com.google.gson.annotations.SerializedName
data class VendorCommissionResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: DataCommission
)

data class DataCommission(
    @SerializedName("invoices")
    val invoices: List<Any>,
    @SerializedName("order_items")
    val orderItems: List<Any>
)

//data class Invoice(
//    // Add appropriate fields here if the invoice structure is defined
//)
//
//data class OrderItem(
//    // Add appropriate fields here if the order item structure is defined
//)
