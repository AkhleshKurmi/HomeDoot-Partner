package com.example.akhleshkumar.homedootpartner.models

data class CancelOrderResponse (val success:Boolean,
    val message:String,
    val data:Any)

data class OrderResponses (val status:Boolean,
                                val message:String,
                                val data:Any)