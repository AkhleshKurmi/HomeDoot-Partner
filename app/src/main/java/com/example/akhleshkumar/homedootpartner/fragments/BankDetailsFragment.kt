package com.example.akhleshkumar.homedootpartner.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.akhleshkumar.homedootpartner.databinding.FragmentBankDetailsBinding
import com.example.akhleshkumar.homedoot.api.RetrofitClient
import com.example.akhleshkumar.homedootpartner.models.UploadAndUpdateResponse
import com.example.akhleshkumar.homedootpartner.models.VendorDashboardResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class BankDetailsFragment : Fragment() {
    lateinit var sharedPreferences: android.content.SharedPreferences
    lateinit var editorSP : android.content.SharedPreferences.Editor
    lateinit var progressDialog: android.app.ProgressDialog
    lateinit var binding: FragmentBankDetailsBinding
    private var bankImage: String? = null
    private val REQUEST_CODE_PICK_IMAGE = 100
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
        binding = FragmentBankDetailsBinding.inflate(layoutInflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        RetrofitClient.instance.getVendorDashboard(
            sharedPreferences.getInt("vendor_id", 0).toString()
        ).enqueue(object :
            Callback<VendorDashboardResponse> {
            override fun onResponse(
                call: Call<VendorDashboardResponse>,
                response: Response<VendorDashboardResponse>
            ) {
                if (response.isSuccessful) {
                    progressDialog.dismiss()
                    if (response.body()!!.success) {
                        val data = response.body()!!.data
                        binding.etBankNameUpdate.setText(data.vendorDetails.bankDetails.bankName)
                        binding.etBranchNameUpdate.setText(data.vendorDetails.bankDetails.branchName)
                        binding.etAccountNoUpdate.setText(data.vendorDetails.bankDetails.accountNumber)
                        binding.etIFSCUpdate.setText(data.vendorDetails.bankDetails.ifscCode)
                        binding.approvedUpdate.text = data.vendorDetails.bankDetails.approval
                    }
                }
            }

            override fun onFailure(call: Call<VendorDashboardResponse>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(requireContext(), t.message, Toast.LENGTH_SHORT).show()
            }

        })


        binding.chooseFileChequeUpdate.setOnClickListener {
            openGallery()
        }
        binding.btnSaveBNDetailUpdate.setOnClickListener {
            if (checkValidation()){
                saveBankDetails()
            }

        }
    }
        fun checkValidation(): Boolean {
            if (binding.etBankNameUpdate.text.toString().isEmpty()) {
                binding.etBankNameUpdate.error = "Enter Bank Name"
                return false
            }

            if (binding.etBranchNameUpdate.text.toString().isEmpty()) {
                binding.etBranchNameUpdate.error = "Enter Branch Name"
                return false
            }
            if (binding.etAccountNoUpdate.text.toString().isEmpty()) {
                binding.etAccountNoUpdate.error = "Enter Account Number"
                return false
            }

            if (binding.etIFSCUpdate.text.toString().isEmpty()) {
                binding.etIFSCUpdate.error = "Enter IFSC Code"
                return false
            }
            return true

        }

    fun saveBankDetails(){
        progressDialog.show()
        val bankName = binding.etBankNameUpdate.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val branchName = binding.etBranchNameUpdate.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val accountNumber = binding.etAccountNoUpdate.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val ifscCode = binding.etIFSCUpdate.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val vendorId = sharedPreferences.getInt("vendor_id",0).toString().toRequestBody("text/plain".toMediaTypeOrNull())

        var filePart : MultipartBody.Part? = null
        if (bankImage!=null) {
            val file = File(bankImage) // Replace with the actual file path
            val requestBody = file.asRequestBody("image/png".toMediaTypeOrNull())
            filePart =
                MultipartBody.Part.createFormData("hid_cheque_file", file.name, requestBody) ?: null
        } else{
            filePart = null
        }
        val call = RetrofitClient.instance.uploadBankDetails(vendorId,accountNumber,bankName,branchName,ifscCode,
            filePart.toString().toRequestBody("text/plain".toMediaTypeOrNull())?:null)
        call.enqueue(object : Callback<UploadAndUpdateResponse>{
            override fun onResponse(call: Call<UploadAndUpdateResponse>, response: Response<UploadAndUpdateResponse>) {
                if (response.isSuccessful) {
                    progressDialog.dismiss()
                    if (response.body()!!.success) {
                        Toast.makeText(
                            requireContext(),
                            response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            override fun onFailure(call: Call<UploadAndUpdateResponse>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(requireContext(), t.message, Toast.LENGTH_SHORT).show()
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
        val cursor = requireContext().contentResolver.query(uri, null, null, null, null)
        cursor?.moveToFirst()
        val idx = cursor?.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
        val filePath = cursor?.getString(idx ?: 0)
        cursor?.close()
        return filePath
    }

}