package com.example.akhleshkumar.homedootpartner.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.akhleshkumar.homedootpartner.databinding.FragmentSaveBankDetailBinding
import com.example.akhleshkumar.homedoot.api.RetrofitClient
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
    lateinit var vendorId: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentSaveBankDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedpref = getSharedPreferences("HomeDoot", MODE_PRIVATE)
        editor = sharedpref.edit()
        vendorId = sharedpref.getString("vendor_id", "").toString()
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
        val vendorId = this.vendorId.toRequestBody("text/plain".toMediaTypeOrNull())
        val accountNumber = binding.etAccountNo.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val bankName = binding.etBankName.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val branchName = binding.etBranchName.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val ifscCode = binding.etIFSC.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())

        val file = File("/path/to/your/file/12344353363.png") // Replace with the actual file path
        val requestBody = file.asRequestBody("image/png".toMediaTypeOrNull())
        val filePart = MultipartBody.Part.createFormData("hid_cheque_file", file.name, requestBody)

        // Make the API call
        val call = RetrofitClient.instance.uploadBankDetails(
            vendorId,
            accountNumber,
            bankName,
            branchName,
            ifscCode,
            null
        )

        call.enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful) {
                    startActivity(Intent(this@SaveBankDetailsActivity, LoginActivity::class.java))
                    Toast.makeText(this@SaveBankDetailsActivity, "Upload successful!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@SaveBankDetailsActivity, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                Toast.makeText(this@SaveBankDetailsActivity, "Failure: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}