package com.example.akhleshkumar.homedootpartner.models

import com.google.gson.annotations.SerializedName

data class VendorOrderRes(
    val success: Boolean,
    val message: String,
    val data: PaginationData
)

data class PaginationData(
    val current_page: Int,
    val data: ArrayList<OrderResponse>, // Adjust the type if the "data" array contains specific objects
    val first_page_url: String,
    val from: Int?,
    val last_page: Int,
    val last_page_url: String,
    val links: List<Link>,
    val next_page_url: String?,
    val path: String,
    val per_page: Int,
    val prev_page_url: String?,
    val to: Int?,
    val total: Int
)

data class Link(
    val url: String?,
    val label: String,
    val active: Boolean
)



data class OrderResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("order_no") val orderNo: String,
    @SerializedName("razor_order_id") val razorOrderId: String,
    @SerializedName("razor_order_status") val razorOrderStatus: String,
    @SerializedName("coupan_code") val couponCode: String?,
    @SerializedName("plan_id") val planId: Int,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("sub_total") val subTotal: Int,
    @SerializedName("grand_total") val grandTotal: Int,
    @SerializedName("discount_total") val discountTotal: Int,
    @SerializedName("address") val address: String,
    @SerializedName("service_date") val serviceDate: String,
    @SerializedName("old_service_date") val oldServiceDate: String?,
    @SerializedName("old_service_time") val oldServiceTime: String?,
    @SerializedName("service_time") val serviceTime: String,
    @SerializedName("payment_method") val paymentMethod: String?,
    @SerializedName("cash_accepted") val cashAccepted: Int,
    @SerializedName("job_started") val jobStarted: Int,
    @SerializedName("order_status") val orderStatus: String,
    @SerializedName("order_current_status") val orderCurrentStatus: String?,
    @SerializedName("payment_status") val paymentStatus: String,
    @SerializedName("status_from_vendor") val statusFromVendor: String?,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("customers") val customers: Customer,
    @SerializedName("assigned_order") val assignedOrder: AssignedOrder,
    @SerializedName("items") val items: List<Item>
)

data class Customer(
    @SerializedName("id") val id: Int,
    @SerializedName("user_token") val userToken: String,
    @SerializedName("name") val name: String,
    @SerializedName("employer_name") val employerName: String?,
    @SerializedName("designation") val designation: String?,
    @SerializedName("email") val email: String,
    @SerializedName("gender") val gender: String?,
    @SerializedName("email_verification") val emailVerification: String,
    @SerializedName("email_verified_at") val emailVerifiedAt: String?,
    @SerializedName("mobile") val mobile: String,
    @SerializedName("address") val address: String?,
    @SerializedName("city") val city: Int,
    @SerializedName("state") val state: Int,
    @SerializedName("pincode") val pincode: String,
    @SerializedName("country") val country: String?,
    @SerializedName("encrypted_password") val encryptedPassword: String,
    @SerializedName("current_team_id") val currentTeamId: String?,
    @SerializedName("profile_photo_path") val profilePhotoPath: String?,
    @SerializedName("role_id") val roleId: Int,
    @SerializedName("subscription_plan_id") val subscriptionPlanId: Int,
    @SerializedName("purchase_plan_id") val purchasePlanId: String,
    @SerializedName("subscription_type") val subscriptionType: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("profile_photo_url") val profilePhotoUrl: String
)

data class AssignedOrder(
    @SerializedName("id") val id: Int,
    @SerializedName("order_no") val orderNo: String,
    @SerializedName("order_id") val orderId: Int,
    @SerializedName("vendor_ids") val vendorIds: String,
    @SerializedName("vendor_accepted") val vendorAccepted: Int,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String
)

data class Item(
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
    @SerializedName("products") val products: Product
)

data class Product(
    @SerializedName("id") val id: Int,
    @SerializedName("product_token") val productToken: String,
    @SerializedName("category_id") val categoryId: Int,
    @SerializedName("sub_category_id") val subCategoryId: String,
    @SerializedName("child_sub_category_id") val childSubCategoryId: Int,
    @SerializedName("service_name") val serviceName: String,
    @SerializedName("listing_status") val listingStatus: String,
    @SerializedName("main_image") val mainImage: String,
    @SerializedName("more_images") val moreImages: String,
    @SerializedName("description") val description: String,
    @SerializedName("url") val url: String,
    @SerializedName("price") val price: Int,
    @SerializedName("gst_percentage") val gstPercentage: Int,
    @SerializedName("included") val included: String,
    @SerializedName("excluded") val excluded: String,
    @SerializedName("other1") val other1: String,
    @SerializedName("other2") val other2: String,
    @SerializedName("home") val home: Int,
    @SerializedName("assign_to_menu") val assignToMenu: String,
    @SerializedName("status") val status: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("product_items") val productItems: List<ProductItem>,
    @SerializedName("sub_categories") val subCategories: SubCategory
)

data class ProductItem(
    @SerializedName("id") val id: Int,
    @SerializedName("p_id") val pId: Int,
    @SerializedName("item_name") val itemName: String,
    @SerializedName("mrp_price") val mrpPrice: Int,
    @SerializedName("offer_price") val offerPrice: Int,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("reviews") val reviews: List<Any>
)

data class SubCategory(
    @SerializedName("id") val id: Int,
    @SerializedName("sub_category_token") val subCategoryToken: String,
    @SerializedName("sub_category_name") val subCategoryName: String,
    @SerializedName("description") val description: String,
    @SerializedName("category_id") val categoryId: Int,
    @SerializedName("sub_category_image") val subCategoryImage: String,
    @SerializedName("commission") val commission: Int,
    @SerializedName("tax_on_commission") val taxOnCommission: Int,
    @SerializedName("per_day_work") val perDayWork: Int,
    @SerializedName("meta_title") val metaTitle: String,
    @SerializedName("meta_description") val metaDescription: String,
    @SerializedName("meta_keywords") val metaKeywords: String,
    @SerializedName("status") val status: Int,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String
)
