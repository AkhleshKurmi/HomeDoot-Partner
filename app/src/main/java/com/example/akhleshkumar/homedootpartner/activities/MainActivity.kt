package com.example.akhleshkumar.homedootpartner.activities

import android.app.ProgressDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.akhleshkumar.homedoot.api.RetrofitClient
import com.example.akhleshkumar.homedootpartner.databinding.ActivityMainBinding
import com.example.akhleshkumar.homedootpartner.models.VendorDashboardResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var progressDialog: ProgressDialog
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editorSP : SharedPreferences.Editor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences("HomeDoot", MODE_PRIVATE)
        editorSP = sharedPreferences.edit()
        progressDialog = ProgressDialog(this).apply {
            setMessage("Loading...")
            setCancelable(false)
        }
        getVendorDashboard(sharedPreferences.getString("userId","").toString())
    }

    private fun getVendorDashboard(userId:String) {
        RetrofitClient.instance.getVendorDashboard(userId).enqueue(object : Callback<VendorDashboardResponse> {
            override fun onResponse(
                call: Call<VendorDashboardResponse>,
                response: Response<VendorDashboardResponse>
            ) {
                if (response.isSuccessful){

                    Toast.makeText(this@MainActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<VendorDashboardResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "something went wrong", Toast.LENGTH_SHORT).show()
            }

        })
    }
}