package com.example.akhleshkumar.homedoot.api

import com.example.akhleshkumar.homedoot.models.CityResponse
import com.example.akhleshkumar.homedoot.models.StateResponse
import com.example.akhleshkumar.homedoot.models.user.OtpResponse
import com.example.akhleshkumar.homedoot.models.user.RegistrationRequest
import com.example.akhleshkumar.homedoot.models.user.RegistrationResponse
import com.example.akhleshkumar.homedoot.models.user.SendOtpRequest
import com.example.akhleshkumar.homedootpartner.models.ApiResponseCategory
import com.example.akhleshkumar.homedootpartner.models.CancelOrderResponse
import com.example.akhleshkumar.homedootpartner.models.OrderResponses
import com.example.akhleshkumar.homedootpartner.models.ReviewResponse
import com.example.akhleshkumar.homedootpartner.models.UploadAndUpdateResponse
import com.example.akhleshkumar.homedootpartner.models.VendorCommissionResponse
import com.example.akhleshkumar.homedootpartner.models.VendorDashboardResponse
import com.example.akhleshkumar.homedootpartner.models.VendorOrderRes
import com.example.akhleshkumar.homedootpartner.models.WalletHistoryResponse
import com.example.akhleshkumar.homedootpartner.models.WalletResponse
import com.example.akhleshkumar.homedootpartner.models.user.ForgotPasswordResponse
import com.example.akhleshkumar.homedootpartner.models.user.LoginUserResponse
import com.example.akhleshkumar.homedootpartner.models.user.UpdatePasswordResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("category")
    fun fetchCategories(): Call<ApiResponseCategory>

    @GET("state")
    fun getState(): Call<StateResponse>

    @POST("city")
    fun getCity(@Query("state_id")stateId:Int) :Call<CityResponse>

    @POST("forgot_password")
    fun forgotPassword(@Query("username") userName:String, @Query("guard") userType:String) : Call<ForgotPasswordResponse>

    @POST("update_password")
    fun updatePassword(@Query("username") userName:String, @Query("guard") userType:String, @Query("password") password: String, @Query("password_confirmation") confirmPassword:String) : Call<UpdatePasswordResponse>


    @POST("login")
    fun userLogin(@Query("username") userName:String, @Query("guard") userType:String, @Query("login_password") password:String) :Call<LoginUserResponse>

    @POST("user-register")
    fun sendOtp(@Body request: SendOtpRequest): Call<OtpResponse>

    @POST("user-register")
    fun userRegister(@Body request: RegistrationRequest): Call<RegistrationResponse>

    @Multipart
    @POST("vendor-business-details") // Replace with your API endpoint
    fun uploadVendorDetails(
        @Part("business_name") businessName: RequestBody,
        @Part("contact_person") contactPerson: RequestBody,
        @Part("contact_mobile") contactMobile: RequestBody,
        @Part("business_address") businessAddress: RequestBody,
        @Part("pan_details") panDetails: RequestBody,
        @Part("aadhar_details") aadharDetails: RequestBody,
        @Part("vendor_id") vendorId: RequestBody,
        @Part ("hid_address_proof")address: RequestBody? = null,
        @Part ("hid_tan_file")tan: RequestBody?= null,
        @Part ("hid_pan_file")pan: RequestBody? = null,
        @Part ("hid_aadhar_proof")aadhar: RequestBody? = null
    ): Call<UploadAndUpdateResponse>


    @Multipart
    @POST("vendor-business-details") // Replace with your API endpoint
    fun uploadVendorDetailsNullImages(
        @Part("business_name") businessName: RequestBody,
        @Part("contact_person") contactPerson: RequestBody,
        @Part("contact_mobile") contactMobile: RequestBody,
        @Part("business_address") businessAddress: RequestBody,
        @Part("pan_details") panDetails: RequestBody,
        @Part("aadhar_details") aadharDetails: RequestBody,
        @Part("vendor_id") vendorId: RequestBody,
        @Part ("hid_address_proof")address: RequestBody? = null,
        @Part ("hid_tan_file")tan:RequestBody?= null,
        @Part ("hid_pan_file")pan: RequestBody? = null,
        @Part ("hid_aadhar_proof")aadhar: RequestBody? = null
    ): Call<UploadAndUpdateResponse>

//    @POST("forgot_password")
//    fun forgotPassword(@Query("username") userName:String, @Query("guard") userType:String) : Call<ForgotPasswordResponse>

//    @POST("update_password")
//    fun updatePassword(@Query("username") userName:String, @Query("guard") userType:String, @Query("password") password: String, @Query("password_confirmation") confirmPassword:String) : Call<UpdatePasswordResponse>


  @POST("update_profile")
    fun updateProfile(
        @Query("profile_guard") profileGuard: String,
        @Query("name") name: String,
        @Query("user_id") userId: Int,
        @Query("email") email: String,
        @Query("mobile") mobile: String,
        @Query("address") address: String,
        @Query("city") city: Int,
        @Query("pincode") pincode: Int
    ): Call<CancelOrderResponse>


//    @POST("user-register")
//    fun registerUserOtp(
//        @Query("role_id") roleId: Int,
//        @Query("name") name: String,
//        @Query("mobile") mobile: String,
//        @Query("email") email: String,
//        @Query("fast_login") fastLogin: Boolean,
//    ): Call<LoginWithOtpRes>
//
//    @POST("user-register")
//    fun registerUser(
//        @Query("role_id") roleId: Int,
//        @Query("name") name: String,
//        @Query("mobile") mobile: String,
//        @Query("email") email: String,
//        @Query("fast_login") fastLogin: Boolean,
//        @Query("VerificationCode") verificationCode: Int,
//        @Query("register_otp") registerOtp: Int
//    ): Call<RegisterWithOtpLoginRes>

//    @POST("update-schedule")
//    fun updateSchedule(@Query("order_no") orderNo:String, @Query("type") type:String = "date", @Query("time")
//    time:String, @Query("date") date:String) : Call<RemoveCartItemRes>

    fun productReview(){

    }

    fun vendorReview(){

    }

    @POST("vendor-dashboard")
     fun getVendorDashboard(@Query("vendor_id")userId: String) : Call<VendorDashboardResponse>

    @Multipart
    @POST("vendor-bank-details") // Replace with the actual endpoint
    fun uploadBankDetails(
        @Part("vendor_id") vendorId: RequestBody,
        @Part("account_number") accountNumber: RequestBody,
        @Part("bank_name") bankName: RequestBody,
        @Part("branch_name") branchName: RequestBody,
        @Part("ifsc_code") ifscCode: RequestBody,
        @Part ("hid_cheque_file")bank: RequestBody?
    ): Call<UploadAndUpdateResponse>

    @POST("vendor-orders")
    fun vendorOrders(@Query("vendor_id") vendorId:String, @Query("status") status:String) : Call<VendorOrderRes>

    @POST("vendor-wallet-transaction")
    fun walletTransaction(@Query("vendor_id") vendorId:Int, @Query("type") type:String) : Call<WalletHistoryResponse>

    @POST("vendor-rating")
    fun vendorReview(@Query("vendor_id") vendorId:String) : Call<ReviewResponse>

    @POST("vendor-commision")
    fun getVendorCommission(
        @Query("vendor_id") vendorId: Int,
        @Query("from_date") fromDate: String,
        @Query("to_date") toDate: String,
        @Query("request_type") requestType: String
    ): Call<VendorCommissionResponse>


    @POST("paysuccess-wallet-transactions")
    fun paymentSuccess(@Query("vendor_id") vendorId:Int, @Query("totalAmount") totalAmount:String,@Query("wallet_pay")walletPay:String,
                   @Query("totalAmtForPay") totalAmtForPay:String,
                   @Query("r_pay_id") razorPayId :String, @Query("r_order_id")razorPayOrderId:String,
                   @Query("r_sign_id") razorPaySignatureId:String) : Call<WalletResponse>


    @POST("fill-wallet")
    fun fillWallet(@Query("vendor_id") vendorId:Int, @Query("amount") totalAmount:String) : Call<WalletResponse>

    @POST("accept-order")
    fun acceptOrder(@Query("vendor_id")vendorId: String,@Query("order_no") orderNo: String, @Query("grand_total") grandTotal: Int) : Call<OrderResponses>

    @POST("accept-cash/{order_no}")
    fun acceptCash(@Path("order_no") order_no: String) : Call<CancelOrderResponse>

    @POST("vendor-update-order")
    fun updateStatus(@Query("vendor_id") vendorId: String, @Query("order_no") orderNo: String, @Query("status") status: String) : Call<OrderResponses>

}

