package com.example.akhleshkumar.homedootpartner.models

import com.google.gson.annotations.SerializedName

data class VendorDashboardResponse (
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: Data
)

data class Data(
    @SerializedName("Dashboard") val dashboard: List<Any>, // Assuming it's an empty array
    @SerializedName("VendorDetails") val vendorDetails: VendorDetails
)

data class VendorDetails(
    @SerializedName("id") val id: Int,
    @SerializedName("user_token") val userToken: String,
    @SerializedName("wallet") val wallet: Int,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("email_verification") val emailVerification: String,
    @SerializedName("email_verified_at") val emailVerifiedAt: String?,
    @SerializedName("mobile") val mobile: String,
    @SerializedName("encrypted_password") val encryptedPassword: String,
    @SerializedName("address") val address: String,
    @SerializedName("country") val country: Int,
    @SerializedName("city") val city: Int,
    @SerializedName("state") val state: Int,
    @SerializedName("pincode") val pincode: String,
    @SerializedName("current_team_id") val currentTeamId: String?,
    @SerializedName("profile_photo_path") val profilePhotoPath: String,
    @SerializedName("category") val category: Int,
    @SerializedName("sub_category") val subCategory: String,
    @SerializedName("date_range") val dateRange: String,
    @SerializedName("non_availability_from") val nonAvailabilityFrom: String,
    @SerializedName("non_availability_to") val nonAvailabilityTo: String,
    @SerializedName("role_id") val roleId: Int,
    @SerializedName("admin_block_date") val adminBlockDate: Int,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("status") val status: Int,
    @SerializedName("vendor_profile_path") val vendorProfilePath: String,
    @SerializedName("vendor_bank_cheque_path") val vendorBankChequePath: String,
    @SerializedName("vendor_business_pan") val vendorBusinessPan: String,
    @SerializedName("vendor_business_tan") val vendorBusinessTan: String,
    @SerializedName("vendor_business_address") val vendorBusinessAddress: String,
    @SerializedName("vendor_business_aadhar_path") val vendorBusinessAadharPath: String,
    @SerializedName("profile_photo_url") val profilePhotoUrl: String,
    @SerializedName("bank_details") val bankDetails: BankDetails,
    @SerializedName("business_details") val businessDetails: BusinessDetails
)

data class BankDetails(
    @SerializedName("id") val id: Int,
    @SerializedName("bank_token") val bankToken: String,
    @SerializedName("user_id") val userId: String?,
    @SerializedName("vendor_id") val vendorId: Int,
    @SerializedName("account_number") val accountNumber: String,
    @SerializedName("bank_name") val bankName: String,
    @SerializedName("branch_name") val branchName: String,
    @SerializedName("ifsc_code") val ifscCode: String,
    @SerializedName("cheque_file") val chequeFile: String,
    @SerializedName("approval") val approval: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String
)

data class BusinessDetails(
    @SerializedName("id") val id: Int,
    @SerializedName("business_token") val businessToken: String,
    @SerializedName("vendor_id") val vendorId: Int,
    @SerializedName("business_name") val businessName: String,
    @SerializedName("aadhar_proof") val aadharProof: String,
    @SerializedName("aadhar_details") val aadharDetails: String,
    @SerializedName("address_proof") val addressProof: String,
    @SerializedName("contact_person") val contactPerson: String,
    @SerializedName("contact_mobile") val contactMobile: String,
    @SerializedName("business_address") val businessAddress: String,
    @SerializedName("gst_details") val gstDetails: String?,
    @SerializedName("gst_file") val gstFile: String,
    @SerializedName("pan_details") val panDetails: String,
    @SerializedName("pan_file") val panFile: String,
    @SerializedName("tan_details") val tanDetails: String?,
    @SerializedName("tan_file") val tanFile: String,
    @SerializedName("approval") val approval: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String
)