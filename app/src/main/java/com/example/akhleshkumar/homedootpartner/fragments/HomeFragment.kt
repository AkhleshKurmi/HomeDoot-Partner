package com.example.akhleshkumar.homedootpartner.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.akhleshkumar.homedootpartner.databinding.FragmentHomeBinding
import com.example.akhleshkumar.homedoot.api.RetrofitClient
import com.example.akhleshkumar.homedootpartner.activities.OrdersActivity
import com.example.akhleshkumar.homedootpartner.models.VendorOrderRes
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {
    lateinit var binding : FragmentHomeBinding
    lateinit var progressDialog: ProgressDialog
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editorSP : SharedPreferences.Editor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = requireActivity().getSharedPreferences("HomeDoot", Activity.MODE_PRIVATE)
        editorSP = sharedPreferences.edit()
        progressDialog = ProgressDialog(requireContext()).apply {
            setMessage("Loading...")
            setCancelable(false)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       binding = FragmentHomeBinding.inflate(layoutInflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        binding.forwordPending.setOnClickListener {
//            val intent = Intent(requireContext(), OrdersActivity::class.java)
//            intent.putExtra("from","pending")
//            startActivity(intent)
//        }
//        binding.forwordCancelled.setOnClickListener {
//            val intent = Intent(requireContext(), OrdersActivity::class.java)
//            intent.putExtra("from","cancelled")
//            startActivity(intent)
//        }


        RetrofitClient.instance.vendorOrders(sharedPreferences.getInt("vendor_id",0).toString(), "pending").enqueue(object : Callback<VendorOrderRes>{
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<VendorOrderRes>,
                response: Response<VendorOrderRes>
            ) {
                if (response.isSuccessful){
                    if (response.body()!!.success){
                        val data = response.body()!!.data
                      //  binding.count.text = data.data.size.toString()
                    }
                }
            }

            override fun onFailure(call: Call<VendorOrderRes>, t: Throwable) {
                Toast.makeText(requireContext(), t.message, Toast.LENGTH_SHORT).show()
            }

        })
        RetrofitClient.instance.vendorOrders(sharedPreferences.getInt("vendor_id",0).toString(), "cancelled").enqueue(object : Callback<VendorOrderRes>{
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<VendorOrderRes>,
                response: Response<VendorOrderRes>
            ) {
                if (response.isSuccessful){
                    if (response.body()!!.success){
                        val data = response.body()!!.data
                      //  binding.count1.text = data.data.size.toString()

                    }
                }
            }

            override fun onFailure(call: Call<VendorOrderRes>, t: Throwable) {
                Toast.makeText(requireContext(), t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            // Do something with the image bitmap
        }
    }
}