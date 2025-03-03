package com.example.akhleshkumar.homedootpartner.fragments

import android.app.ProgressDialog
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import com.akhleshkumar.homedootpartner.R
import com.akhleshkumar.homedootpartner.databinding.ActivityOrdersBinding
import com.example.akhleshkumar.homedoot.api.RetrofitClient
import com.example.akhleshkumar.homedootpartner.adaters.OrdersAdapter
import com.example.akhleshkumar.homedootpartner.models.VendorOrderRes
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JobHistoryFragment : Fragment() {
    lateinit var binding : ActivityOrdersBinding
    lateinit var ordersAdapter: OrdersAdapter
    lateinit var progressDialog: ProgressDialog
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editorSP: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActivityOrdersBinding.inflate(layoutInflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = requireActivity().getSharedPreferences("HomeDoot", MODE_PRIVATE)
        editorSP = sharedPreferences.edit()
        progressDialog = ProgressDialog(requireContext()).apply {
            setMessage("Loading...")
            setCancelable(false)
        }
        binding.rvOrders.layoutManager = LinearLayoutManager(requireContext())


        getOrders("pending")

    }
    private fun getOrders(status:String){
        progressDialog.show()
        RetrofitClient.instance.vendorOrders(sharedPreferences.getInt("vendor_id",0).toString(),status)
            .enqueue(object : Callback<VendorOrderRes> {
                override fun onResponse(
                    call: Call<VendorOrderRes>,
                    response: Response<VendorOrderRes>
                ) {
                    progressDialog.dismiss()
                    if (response.isSuccessful){
                        if (response.body()!!.success){
                            val list = response.body()!!.data.data
                            ordersAdapter = OrdersAdapter(requireContext(),list,status)
                            binding.rvOrders.adapter = ordersAdapter
                        }
                    }
                }
                override fun onFailure(call: Call<VendorOrderRes>, t: Throwable) {
                    Toast.makeText(requireContext(), t.message, Toast.LENGTH_SHORT).show()
                    progressDialog.dismiss()
                }
            })
    }
}