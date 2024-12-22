package com.example.akhleshkumar.homedootpartner.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.akhleshkumar.homedootpartner.databinding.FragmentSaveBusinessDetailsBinding
import com.example.akhleshkumar.homedoot.api.RetrofitClient
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SaveBusinessDetaiiilsActivity : AppCompatActivity() {
    lateinit var binding: FragmentSaveBusinessDetailsBinding
    lateinit var vendorId: String
    lateinit var sharedpref: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentSaveBusinessDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedpref = getSharedPreferences("HomeDoot", MODE_PRIVATE)
        editor = sharedpref.edit()
        vendorId = sharedpref.getString("vendor_id", "").toString()
        binding.btnUpdeateBusinessDeltail.setOnClickListener {
            if (checkValidation()){
             updateBusiness()
            }
        }
    }
    private fun checkValidation(): Boolean {
        if (binding.etBusinessName.text.toString().isEmpty()) {
            binding.etBusinessName.error = "Enter Business Name"
            return false
        }

        if (binding.etBusinessAddress.text.toString().isEmpty()) {
            binding.etBusinessAddress.error = "Enter Business Address"
            return false
        }
        if (binding.etMobileNo.text.toString().isEmpty()) {
            binding.etMobileNo.error = "Enter mobile number"
            return false
        }
        if (binding.etGstDetail.text.toString().isEmpty()) {
            binding.etGstDetail.error = "Enter GST Number"
            return false
        }
        if (binding.etPANDetail.text.toString().isEmpty()) {
            binding.etPANDetail.error = "Enter PAN Number"
            return false
        }
        if (binding.etAAdharDetail.text.toString().isEmpty()) {
            binding.etAAdharDetail.error = "Enter Aadhar Number"
            return false
        }

        if (binding.etConPerName.text.toString().isEmpty()) {
            binding.etConPerName.error = "Enter Business Description"
            return false
        }

        return true
    }

    fun updateBusiness(){
        val businessName = binding.etBusinessName.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val contactPerson = binding.etConPerName.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val contactMobile = binding.etMobileNo.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val businessAddress = binding.etBusinessAddress.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val panDetails = binding.etPANDetail.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val aadharDetails = binding.etAAdharDetail.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val vendorId = this.vendorId.toRequestBody("text/plain".toMediaTypeOrNull())

//        val panPart = uriToMultipart(hidPanUri, "hid_pan_file")
//        val addressProofPart = uriToMultipart(hidAddressUri, "hid_address_proof")
//        val tanPart = uriToMultipart(hidTanUri, "hid_tan_file")
//        val aadharPart = uriToMultipart(hidAadharUri, "hid_aadhar_proof")

        // Call API
        val call = RetrofitClient.instance.uploadVendorDetails(
            businessName,
            contactPerson,
            contactMobile,
            businessAddress,
            panDetails,
            aadharDetails,
            vendorId,
            null,
            null,
            null,
            null
        )

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("Success", "Upload successful")
                    startActivity(Intent(this@SaveBusinessDetaiiilsActivity, SaveBankDetailsActivity::class.java))
                    finish()
                } else {
                    Log.e("Error", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("Failure", "Request failed: ${t.message}")
            }
        })
    }

}