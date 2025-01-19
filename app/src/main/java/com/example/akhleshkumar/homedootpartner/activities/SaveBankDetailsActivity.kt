package com.example.akhleshkumar.homedootpartner.activities

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.akhleshkumar.homedootpartner.databinding.FragmentSaveBankDetailBinding
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

class SaveBankDetailsActivity : AppCompatActivity() {

    lateinit var binding: FragmentSaveBankDetailBinding
    lateinit var sharedpref: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    lateinit var bankImage: String
    private val REQUEST_CODE_PICK_IMAGE = 100
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentSaveBankDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedpref = getSharedPreferences("HomeDoot", MODE_PRIVATE)
        editor = sharedpref.edit()

        binding.chooseFileCheque.setOnClickListener {
            openGallery()
        }

        binding.btnSaveBNDetail.setOnClickListener {
            if (checkValidation()){
            uploadBankDetails()
            }
        }
    }
    private fun checkValidation(): Boolean {
        if (binding.etAccountNo.text.toString().isEmpty()) {
            binding.etAccountNo.error = "Enter Account Number"
            return false
        }
        if (binding.etBankName.text.toString().isEmpty()) {
            binding.etBankName.error = "Enter Bank Name"
            return false
        }
        if (binding.etBranchName.text.toString().isEmpty()) {
            binding.etBranchName.error = "Enter Branch Name"
            return false
        }

        if (binding.etIFSC.text.toString().isEmpty()) {
            binding.etIFSC.error = "Enter IFSC Code"
            return false
        }
        return true

    }

    private fun uploadBankDetails() {
        // Prepare RequestBody and File
        val vendorId = sharedpref.getInt("vendor_id",0).toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val accountNumber = binding.etAccountNo.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val bankName = binding.etBankName.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val branchName = binding.etBranchName.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val ifscCode = binding.etIFSC.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())

        var filePart : MultipartBody.Part? = null
        if (bankImage != null) {
            val file = File(bankImage) // Replace with the actual file path
            val requestBody = file.asRequestBody("image/png".toMediaTypeOrNull())
            filePart =
                MultipartBody.Part.createFormData("hid_cheque_file", file.name, requestBody) ?: null
        } else{
            filePart = null
        }
        // Make the API call
        val call = RetrofitClient.instance.uploadBankDetails(
            vendorId,
            accountNumber,
            bankName,
            branchName,
            ifscCode,
            filePart.toString().toRequestBody("text/plain".toMediaTypeOrNull()) ?:null
        )

        call.enqueue(object : Callback<UploadAndUpdateResponse> {
            override fun onResponse(call: Call<UploadAndUpdateResponse>, response: Response<UploadAndUpdateResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.success) {
                        Toast.makeText(
                            this@SaveBankDetailsActivity,
                            response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()

                        startActivity(
                            Intent(
                                this@SaveBankDetailsActivity,
                                LoginActivity::class.java
                            )
                        )
                        finish()

                    }
                } else {
                    Toast.makeText(
                        this@SaveBankDetailsActivity,
                        "Error: ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<UploadAndUpdateResponse>, t: Throwable) {
                Toast.makeText(this@SaveBankDetailsActivity, "Failure: ${t.message}", Toast.LENGTH_SHORT).show()
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
                val filePath = getRealPathFromUri(it)
                if (filePath != null) {
                    bankImage = filePath
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