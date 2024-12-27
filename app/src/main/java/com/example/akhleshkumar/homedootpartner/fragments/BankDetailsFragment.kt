package com.example.akhleshkumar.homedootpartner.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.akhleshkumar.homedootpartner.R
import com.akhleshkumar.homedootpartner.databinding.FragmentBankDetailsBinding
import com.example.akhleshkumar.homedoot.api.RetrofitClient
import com.example.akhleshkumar.homedootpartner.models.VendorDashboardResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BankDetailsFragment : Fragment() {
    lateinit var sharedPreferences: android.content.SharedPreferences
    lateinit var editorSP : android.content.SharedPreferences.Editor
    lateinit var progressDialog: android.app.ProgressDialog
    lateinit var binding: FragmentBankDetailsBinding
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
        progressDialog.show()
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
                    }
                }
            }

            override fun onFailure(call: Call<VendorDashboardResponse>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(requireContext(), t.message, Toast.LENGTH_SHORT).show()
            }

        })

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

        val call = RetrofitClient.instance.uploadBankDetails(vendorId,accountNumber,bankName,branchName,ifscCode,null)
        call.enqueue(object : Callback<Any>{
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful){
                    progressDialog.dismiss()
                    Toast.makeText(requireContext(), "Bank Details Updated", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<Any>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(requireContext(), t.message, Toast.LENGTH_SHORT).show()
            }

        })


    }


}