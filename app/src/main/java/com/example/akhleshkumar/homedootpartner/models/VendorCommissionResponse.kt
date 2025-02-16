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
    val invoices: List<Invoice>,
    @SerializedName("order_items")
    val orderItems: List<OrderItem>
)

data class Invoice(
    @SerializedName("id") val id: Int,
    @SerializedName("order_no") val orderNo: String,
    @SerializedName("product_id") val productId: Int,
    @SerializedName("item_id") val itemId: Int,
    @SerializedName("quantity") val quantity: Int,
    @SerializedName("price") val price: Int,
    @SerializedName("total_amount") val totalAmount: Int,
    @SerializedName("order_current_status") val orderCurrentStatus: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("gst_percentage") val gstPercentage: Int,
    @SerializedName("service_name") val serviceName: String,
    @SerializedName("status_from_vendor") val statusFromVendor: String,
    @SerializedName("commission") val commission: Int,
    @SerializedName("tax_on_commission") val taxOnCommission: Int,
    @SerializedName("item_name") val itemName: String
)

data class OrderItem(
    @SerializedName("order_no") val orderNo: String,
    @SerializedName("discount_total") val discountTotal: Int,
    @SerializedName("discount") val discount: String?
)
