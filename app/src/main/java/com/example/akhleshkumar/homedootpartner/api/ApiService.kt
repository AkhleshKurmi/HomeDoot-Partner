package com.example.akhleshkumar.homedoot.api

import com.example.akhleshkumar.homedoot.models.CityResponse
import com.example.akhleshkumar.homedoot.models.StateResponse
import com.example.akhleshkumar.homedoot.models.user.OtpResponse
import com.example.akhleshkumar.homedoot.models.user.RegistrationRequest
import com.example.akhleshkumar.homedoot.models.user.RegistrationResponse
import com.example.akhleshkumar.homedoot.models.user.SendOtpRequest
import com.example.akhleshkumar.homedootpartner.models.ApiResponseCategory
import com.example.akhleshkumar.homedootpartner.models.user.LoginUserResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @GET("category")
    fun fetchCategories(): Call<ApiResponseCategory>

    @GET("state")
    fun getState(): Call<StateResponse>

    @POST("city")
    fun getCity(@Query("state_id")stateId:Int) :Call<CityResponse>

   @POST("login")
    fun userLogin(@Query("username") userName:String, @Query("guard") userType:String, @Query("login_password") password:String) :Call<LoginUserResponse>

    @POST("user-register")
    fun sendOtp(@Body request: SendOtpRequest): Call<OtpResponse>

    @POST("user-register")
    fun userRegister(@Body request: RegistrationRequest): Call<RegistrationResponse>
//    @POST("forgot_password")
//    fun forgotPassword(@Query("username") userName:String, @Query("guard") userType:String) : Call<ForgotPasswordResponse>

//    @POST("update_password")
//    fun updatePassword(@Query("username") userName:String, @Query("guard") userType:String, @Query("password") password: String, @Query("password_confirmation") confirmPassword:String) : Call<UpdatePasswordResponse>


//  @POST("update_profile")
//    fun updateProfile(
//        @Query("profile_guard") profileGuard: String,
//        @Query("name") name: String,
//        @Query("user_id") userId: Int,
//        @Query("email") email: String,
//        @Query("mobile") mobile: String,
//        @Query("address") address: String,
//        @Query("city") city: Int,
//        @Query("pincode") pincode: Int
//    ): Call<CancelOrderResponse>


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

}
