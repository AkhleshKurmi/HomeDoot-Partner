package com.example.akhleshkumar.homedootpartner.models

data class Transaction(
    val id: Int,
    val amount: Double,
    val orderNo: String?,
    val vendorId: Int,
    val razorOrderId: String,
    val razorOrderStatus: String,
    val razorpayPaymentId: String?,
    val paymentStatus: String,
    val createdAt: String,
    val updatedAt: String
)
