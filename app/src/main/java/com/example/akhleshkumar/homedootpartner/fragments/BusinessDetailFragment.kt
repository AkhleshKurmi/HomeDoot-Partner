package com.example.akhleshkumar.homedootpartner.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.akhleshkumar.homedootpartner.R
import com.example.akhleshkumar.homedoot.api.RetrofitClient
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class BusinessDetailFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bussiness_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val businessName = RequestBody.create(MediaType.parse("text/plain"), "tset")
        val contactPerson = RequestBody.create(MediaType.parse("text/plain"), "pqpqpq")
        val contactMobile = RequestBody.create(MediaType.parse("text/plain"), "1234567898")
        val businessAddress = RequestBody.create(MediaType.parse("text/plain"), "Lorem ipsim")
        val panDetails = RequestBody.create(MediaType.parse("text/plain"), "121121212")
        val aadharDetails = RequestBody.create(MediaType.parse("text/plain"), "12121212")
        val vendorId = RequestBody.create(MediaType.parse("text/plain"), "10")

        // Prepare image files
        val hidAddressProof = createImageFilePart("hid_address_proof", "path/to/61b286b4d8.png")
        val hidTanFile = createImageFilePart("hid_tan_file", "path/to/254316052c.png")
        val hidAadharProof = createImageFilePart("hid_aadhar_proof", "path/to/666666666.png")

        // Call the API
        val call = RetrofitClient.instance.uploadVendorDetails(
            businessName,
            contactPerson,
            contactMobile,
            businessAddress,
            panDetails,
            aadharDetails,
            vendorId,
            hidAddressProof,
            hidTanFile,
            hidAadharProof
        )

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("Success", "Upload successful")
                } else {
                    Log.e("Error", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("Failure", "Request failed: ${t.message}")
            }
        })
    }

    private fun createImageFilePart(paramName: String, filePath: String): MultipartBody.Part {
        val file = File(filePath)
        val requestFile = RequestBody.create(MediaType.parse("image/png"), file)
        return MultipartBody.Part.createFormData(paramName, file.name, requestFile)
    }
}