package com.example.akhleshkumar.homedootpartner.fragments

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.checkPermission
import androidx.fragment.app.Fragment
import com.akhleshkumar.homedootpartner.databinding.FragmentBussinessDetailBinding
import com.example.akhleshkumar.homedoot.api.RetrofitClient
import com.example.akhleshkumar.homedootpartner.models.UploadAndUpdateResponse
import com.example.akhleshkumar.homedootpartner.models.VendorDashboardResponse
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class BusinessDetailFragment : Fragment() {

    lateinit var binding: FragmentBussinessDetailBinding
    lateinit var progressDialog: android.app.ProgressDialog
    lateinit var sharedPreferences: android.content.SharedPreferences
    lateinit var editorSP : android.content.SharedPreferences.Editor
    private val REQUEST_CODE_PICK_IMAGE = 100
    var adharImage: String? = null
    var panImage: String? = null
    var tanImage: String? = null
    var addressImage: String? = null
    var imageType = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = requireActivity().getSharedPreferences("HomeDoot", android.content.Context.MODE_PRIVATE)
        editorSP = sharedPreferences.edit()
        progressDialog = android.app.ProgressDialog(requireContext()).apply {
            setMessage("Loading...")
            setCancelable(false)
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBussinessDetailBinding.inflate(layoutInflater,container,false)
        //        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog.show()
        RetrofitClient.instance.getVendorDashboard(sharedPreferences.getInt("vendor_id",0).toString()).enqueue(object : Callback<VendorDashboardResponse>{
            override fun onResponse(
                call: Call<VendorDashboardResponse>,
                response: Response<VendorDashboardResponse>
            ) {
                if (response.isSuccessful){
                    progressDialog.dismiss()
                    if (response.body()!!.success){
                        val data = response.body()!!.data
                       binding.etGstDetail.setText(data.vendorDetails.businessDetails.gstDetails)
                        binding.etBusinessName.setText(data.vendorDetails.businessDetails.businessName)
                        binding.etBusinessAddress.setText(data.vendorDetails.businessDetails.businessAddress)
                        binding.etMobileNo.setText(data.vendorDetails.businessDetails.contactPerson)
                        binding.etConPerName.setText(data.vendorDetails.businessDetails.contactMobile)
                        binding.etPANDetail.setText(data.vendorDetails.businessDetails.panDetails)
                        binding.etAAdharDetail.setText(data.vendorDetails.businessDetails.aadharDetails)
                    }
                }
            }

            override fun onFailure(call: Call<VendorDashboardResponse>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(requireContext(), t.message, Toast.LENGTH_SHORT).show()
            }

        })
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
        progressDialog.show()
        val businessName = binding.etBusinessName.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val contactPerson = binding.etConPerName.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val contactMobile = binding.etMobileNo.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val businessAddress =  binding.etBusinessAddress.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val panDetails = binding.etPANDetail.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val aadharDetails = binding.etAAdharDetail.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val vendorId = sharedPreferences.getInt("vendor_id",0).toString().toRequestBody("text/plain".toMediaTypeOrNull())

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
            filePartAddress?:null,
            filePartTan?:null,
            filePartPan?:null,
            filePartAdhar?:null
        )

        call.enqueue(object : Callback<UploadAndUpdateResponse> {
            override fun onResponse(call: Call<UploadAndUpdateResponse>, response: Response<UploadAndUpdateResponse>) {
                if (response.isSuccessful) {
                    progressDialog.dismiss()
                    if (response.body()!!.success) {
                        Toast.makeText(requireContext(), response.body()!!.message, Toast.LENGTH_SHORT).show()



                        Log.d("Success", "Upload successful")
                    }
                } else {
                    progressDialog.dismiss()
                    Log.e("Error", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<UploadAndUpdateResponse>, t: Throwable) {
                progressDialog.dismiss()
                Log.e("Failure", "Request failed: ${t.message}")
            }
        })
    }
    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(requireContext(), permission) ==
                android.content.pm.PackageManager.PERMISSION_GRANTED
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
        val cursor = requireContext().contentResolver.query(uri, null, null, null, null)
        cursor?.moveToFirst()
        val idx = cursor?.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
        val filePath = cursor?.getString(idx ?: 0)
        cursor?.close()
        return filePath
    }

}