package com.example.akhleshkumar.homedootpartner.models

data class ApiResponseCategory(
    val success: Boolean,
    val message: String,
    val data: DataCat
)

data class DataCat(
    val path: String,
    val category: List<Category> )

data class Category (val id: Int,
                     val category_token: String,
                     val available_slots: String,
                     val category_name: String,
                     val category_image: String,
                     val status: Int,
                     val created_at: String,
                     val updated_at: String)