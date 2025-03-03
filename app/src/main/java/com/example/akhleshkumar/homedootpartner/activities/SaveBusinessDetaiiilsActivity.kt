package com.example.akhleshkumar.homedootpartner.activities

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.akhleshkumar.homedootpartner.databinding.FragmentSaveBusinessDetailsBinding
import com.example.akhleshkumar.homedoot.api.RetrofitClient
import com.example.akhleshkumar.homedootpartner.models.UploadAndUpdateResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class SaveBusinessDetaiiilsActivity : AppCompatActivity() {
    lateinit var binding: FragmentSaveBusinessDetailsBinding
    lateinit var vendorId: String
    lateinit var sharedpref: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    private val REQUEST_CODE_PICK_IMAGE = 100
     var adharImage: String? = null
     var panImage: String? = null
     var tanImage: String? = null
     var addressImage: String? = null
    var imageType = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentSaveBusinessDetailsBinding.inflate(layoutInflater)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(binding.root)
        sharedpref = getSharedPreferences("HomeDoot", MODE_PRIVATE)
        editor = sharedpref.edit()
        vendorId = sharedpref.getString("vendor_id", "").toString()
        binding.btnUpdeateBusinessDeltail.setOnClickListener {
            if (checkValidation()){
             updateBusiness()
            }
        }

        binding.btnUploadAdharFile.setOnClickListener {
            imageType = 1
            openGallery()
        }
        binding.btnUplaodPAN.setOnClickListener {
            imageType = 2
            openGallery()
        }
        binding.btnGstFile.setOnClickListener {
            imageType = 3
            openGallery()
        }
        binding.btnAddressFile.setOnClickListener {
            imageType = 4
            openGallery()
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

        var filePartPan : MultipartBody.Part? = null
        if (panImage!=null) {
            val filePan = File(panImage) // Replace with the actual file path
            val requestBodyPan = filePan.asRequestBody("image/png".toMediaTypeOrNull())
            filePartPan =
                MultipartBody.Part.createFormData("hid_pan_file", filePan.name, requestBodyPan)
                    ?: null
        }
        else{
            filePartPan = null
        }

        var filePartAdhar : MultipartBody.Part? = null
        if (adharImage!=null) {
            val fileAdhar = File(adharImage) // Replace with the actual file path
            val requestBodyAdhar = fileAdhar.asRequestBody("image/png".toMediaTypeOrNull())
            filePartAdhar = MultipartBody.Part.createFormData(
                "hid_aadhar_proof",
                fileAdhar.name,
                requestBodyAdhar
            ) ?: null
        } else{
            filePartAdhar = null
        }

        var filePartTan : MultipartBody.Part? = null
        if (tanImage!=null) {
            val fileTan = File(tanImage) // Replace with the actual file path = File(bankImage) // Replace with the actual file path
            val requestBodyTan = fileTan.asRequestBody("image/png".toMediaTypeOrNull())
            filePartTan = MultipartBody.Part.createFormData("hid_tan_file", fileTan.name, requestBodyTan) ?: null

        }
        else{
            filePartTan = null
        }

        var filePartAddress : MultipartBody.Part? = null

        if (addressImage!=null) {

            val fileAddress = File(addressImage) // Replace with the actual file path
            val requestBodyAddress = fileAddress.asRequestBody("image/png".toMediaTypeOrNull())
            filePartAddress = MultipartBody.Part.createFormData(
                "hid_address_proof",
                fileAddress.name,
                requestBodyAddress
            ) ?: null
        }
        else{
            filePartAddress = null
        }

//

        // Call API
        val call = RetrofitClient.instance.uploadVendorDetailsNullImages(
            businessName,
            contactPerson,
            contactMobile,
            businessAddress,
            panDetails,
            aadharDetails,
            vendorId,
            filePartAddress.toString().toRequestBody("text/plain".toMediaTypeOrNull())?:null,
            filePartTan.toString().toRequestBody("text/plain".toMediaTypeOrNull())?:null,
            filePartPan.toString().toRequestBody("text/plain".toMediaTypeOrNull())?:null,
            filePartAdhar.toString().toRequestBody("text/plain".toMediaTypeOrNull())?:null
        )

        call.enqueue(object : Callback<UploadAndUpdateResponse> {
            override fun onResponse(call: Call<UploadAndUpdateResponse>, response: Response<UploadAndUpdateResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.success) {
                        Toast.makeText(this@SaveBusinessDetaiiilsActivity, response.body()!!.message, Toast.LENGTH_SHORT).show()
                        Log.d("Success", response.body()!!.message)
                        startActivity(
                            Intent(
                                this@SaveBusinessDetaiiilsActivity,
                                SaveBankDetailsActivity::class.java
                            )
                        )
                        finish()
                    }
                } else {
                    Log.e("Error", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<UploadAndUpdateResponse>, t: Throwable) {
                Log.e("Failure", "Request failed: ${t.message}")
            }
        })
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            val imageUri: Uri? = data?.data
            imageUri?.let {
                when(imageType){
                    1-> {
                        val filePath = getRealPathFromUri(it)
                        if (filePath != null) {
                            adharImage = filePath
                        } else {
                            Log.d("error","error")
                        }
                    }
                    2-> {
                        val filePath = getRealPathFromUri(it)
                        if (filePath != null) {
                            panImage = filePath
                        } else {
                            Log.d("error","error")
                        }
                    }
                    3-> {
                        val filePath = getRealPathFromUri(it)
                        if (filePath != null) {
                            tanImage = filePath
                        } else {
                            Log.d("error","error")
                        }
                    }
                    4-> {
                        val filePath = getRealPathFromUri(it)
                        if (filePath != null) {
                            addressImage = filePath
                        } else {
                            Log.d("error","error")
                        }

                    }
                    else -> Log.d("error","error")
                }
            }
        }
    }
    private fun getRealPathFromUri(uri: Uri): String? {
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.moveToFirst()
        val idx = cursor?.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
        val filePath = cursor?.getString(idx ?: 0)
        cursor?.close()
        return filePath
    }

}