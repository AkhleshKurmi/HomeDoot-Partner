package com.example.akhleshkumar.homedootpartner.activities

import android.app.ProgressDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.telecom.Call
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.akhleshkumar.homedoot.api.RetrofitClient
import com.example.akhleshkumar.homedootpartner.R
import com.example.akhleshkumar.homedootpartner.databinding.ActivityMainBinding
import com.example.akhleshkumar.homedootpartner.models.VendorDashboardResponse
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    lateinit var progressDialog: ProgressDialog
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editorSP : SharedPreferences.Editor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        // Set up ActionBarDrawerToggle
        val toggle = ActionBarDrawerToggle(
            this, binding.drawerLayout, binding.toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()


        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            // Close Drawer
//            binding.drawerLayout.closeDrawer(GravityCompat.START)

            // Show Toast based on clicked item
            when (menuItem.itemId) {
                R.id.menu_calendar -> showToast("Calendar Clicked")
                R.id.menu_job_history -> showToast("Job History Clicked")
                R.id.menu_profile -> showToast("My Profile Clicked")
                R.id.menu_business_detail -> showToast("Update Business Detail Clicked")
                R.id.menu_bank_detail -> showToast("Update Bank Detail Clicked")
                R.id.menu_rating -> showToast("Rating Clicked")
                R.id.menu_commission -> showToast("Commission Clicked")
                else -> showToast("Unknown Item Clicked")
            }
            true
        }


        sharedPreferences = getSharedPreferences("HomeDoot", MODE_PRIVATE)
        editorSP = sharedPreferences.edit()
        progressDialog = ProgressDialog(this).apply {
            setMessage("Loading...")
            setCancelable(false)
        }
       // getVendorDashboard(sharedPreferences.getString("userId","").toString())
    }

//    private fun getVendorDashboard(userId:String) {
//        RetrofitClient.instance.getVendorDashboard(userId).enqueue(object : Callback<VendorDashboardResponse> {
//            override fun onResponse(
//                call: Call<VendorDashboardResponse>,
//                response: Response<VendorDashboardResponse>
//            ) {
//                if (response.isSuccessful){
//
//                    Toast.makeText(this@MainActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onFailure(call: Call<VendorDashboardResponse>, t: Throwable) {
//                Toast.makeText(this@MainActivity, "something went wrong", Toast.LENGTH_SHORT).show()
//            }
//
//        })
//    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}