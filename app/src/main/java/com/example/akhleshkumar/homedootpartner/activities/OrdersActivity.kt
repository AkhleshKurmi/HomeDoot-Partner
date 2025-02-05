package com.example.akhleshkumar.homedootpartner.activities

import android.app.ProgressDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.akhleshkumar.homedootpartner.databinding.ActivityOrdersBinding
import com.example.akhleshkumar.homedoot.api.RetrofitClient
import com.example.akhleshkumar.homedootpartner.adaters.OrdersAdapter
import com.example.akhleshkumar.homedootpartner.models.VendorOrderRes
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrdersActivity : AppCompatActivity() {
    lateinit var binding : ActivityOrdersBinding
    lateinit var ordersAdapter: OrdersAdapter
    lateinit var progressDialog: ProgressDialog
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editorSP: SharedPreferences.Editor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding = ActivityOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences("HomeDoot", MODE_PRIVATE)
        editorSP = sharedPreferences.edit()
        progressDialog = ProgressDialog(this).apply {
            setMessage("Loading...")
            setCancelable(false)
        }
        binding.rvOrders.layoutManager = LinearLayoutManager(this)

       val fromActivity = intent.getStringExtra("from")?: " "
        getOrders(fromActivity)

    }
    private fun getOrders(status:String){
        progressDialog.show()
        RetrofitClient.instance.vendorOrders(sharedPreferences.getInt("vendor_id",0).toString(),status)
            .enqueue(object : Callback<VendorOrderRes>{
                override fun onResponse(
                    call: Call<VendorOrderRes>,
                    response: Response<VendorOrderRes>
                ) {
                    progressDialog.dismiss()
                    if (response.isSuccessful){
                        if (response.body()!!.success){
                            val list = response.body()!!.data.data
                            ordersAdapter = OrdersAdapter(this@OrdersActivity,list,status)
                            binding.rvOrders.adapter = ordersAdapter
                        }
                    }
                }
                override fun onFailure(call: Call<VendorOrderRes>, t: Throwable) {
                    Toast.makeText(this@OrdersActivity, t.message, Toast.LENGTH_SHORT).show()
                    progressDialog.dismiss()
                }
            })
    }
}