package com.example.akhleshkumar.homedootpartner.fragments

import android.Manifest
import android.app.AlertDialog
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
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
    private lateinit var cameraLauncher: ActivityResultLauncher<Void?>
    private lateinit var galleryLauncher: ActivityResultLauncher<String>
    private var imageUri: Uri? = null
    private var selectedImageFile: File? = null
    private var hidPanUri: Uri? = null
    private var hidAddressUri: Uri? = null
    private var hidTanUri: Uri? = null
    private var hidAadharUri: Uri? = null
    lateinit var progressDialog: android.app.ProgressDialog
    lateinit var sharedPreferences: android.content.SharedPreferences
    lateinit var editorSP : android.content.SharedPreferences.Editor

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




        cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            if (bitmap != null) {
                imageUri = saveBitmapToFile(bitmap)
            }
        }

        // Gallery launcher
        galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                imageUri = uri
                selectedImageFile = uriToFile(uri)
            }
        }

//        binding.btnUplaodPAN.setOnClickListener {
//            showImageSourceDialog()
//        }
//        binding.btnUplaodPAN.setOnClickListener { selectFile("hid_pan_file") }
//        binding.btnAddressFile.setOnClickListener { selectFile("hid_address_proof") }
//        binding.etGstDetail.setOnClickListener { selectFile("hid_tan_file") }
//        binding.btnUploadAdharFile.setOnClickListener { selectFile("hid_aadhar_proof") }


        binding.btnUpdeateBusinessDeltail.setOnClickListener {
            if (true){
                updateBusiness()
            }
        }


    }

    private fun selectFile(paramName: String) {
        val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            when (paramName) {
                "hid_pan_file" -> hidPanUri = uri
                "hid_address_proof" -> hidAddressUri = uri
                "hid_tan_file" -> hidTanUri = uri
                "hid_aadhar_proof" -> hidAadharUri = uri
            }
            Toast.makeText(requireContext(), "$paramName selected", Toast.LENGTH_SHORT).show()
        }
        galleryLauncher.launch("image/*")
    }
    private fun createImageFilePart(paramName: String, filePath: String): MultipartBody.Part {
        val file = File(filePath)
        val requestFile = RequestBody.create("image/png".toMediaTypeOrNull(), file)
        return MultipartBody.Part.createFormData(paramName, file.name, requestFile)
    }
    private fun showImageSourceDialog() {
        val options = arrayOf("Camera", "Gallery")
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Choose Image Source")
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> {
                    if (checkPermission(Manifest.permission.CAMERA)) {
                        cameraLauncher.launch(null)
                    }
                }
                1 -> {
                    galleryLauncher.launch("image/*")
                }
            }
        }
        builder.show()
    }

    private fun saveBitmapToFile(bitmap: Bitmap): Uri? {
        // Implement logic to save the Bitmap to a file and return its Uri
        return null // Replace with your logic
    }

    private fun uriToFile(uri: Uri): File? {
        // Convert Uri to File object
        return null // Replace with your logic
    }
    fun checkValidation () : Boolean{

        return true
    }
    private fun uriToMultipart(fileUri: Uri?, paramName: String): MultipartBody.Part? {
        return if (fileUri != null) {
            val file = uriToFile(fileUri) ?: return null
            val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData(paramName, file.name, requestBody)
        } else {
            null
        }
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

        val panPart = uriToMultipart(hidPanUri, "hid_pan_file")
        val addressProofPart = uriToMultipart(hidAddressUri, "hid_address_proof")
        val tanPart = uriToMultipart(hidTanUri, "hid_tan_file")
        val aadharPart = uriToMultipart(hidAadharUri, "hid_aadhar_proof")

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
                    progressDialog.dismiss()
                    Log.d("Success", "Upload successful")
                } else {
                    progressDialog.dismiss()
                    Log.e("Error", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                progressDialog.dismiss()
                Log.e("Failure", "Request failed: ${t.message}")
            }
        })
    }
    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(requireContext(), permission) ==
                android.content.pm.PackageManager.PERMISSION_GRANTED
    }

}