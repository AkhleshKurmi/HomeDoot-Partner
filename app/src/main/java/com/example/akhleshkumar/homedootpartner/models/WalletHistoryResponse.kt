package com.example.akhleshkumar.homedootpartner.models

data class WalletHistoryResponse(
    val success: Boolean,
    val message: String,
    val data: WalletData
)

data class WalletData(
    val walletHistory: List<Transaction>
)