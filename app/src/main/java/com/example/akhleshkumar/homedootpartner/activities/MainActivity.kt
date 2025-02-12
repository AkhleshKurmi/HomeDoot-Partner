package com.example.akhleshkumar.homedootpartner.activities

import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.akhleshkumar.homedootpartner.R
import com.akhleshkumar.homedootpartner.databinding.ActivityMainBinding
import com.akhleshkumar.homedootpartner.databinding.NavHeaderBinding
import com.example.akhleshkumar.homedoot.api.RetrofitClient
import com.example.akhleshkumar.homedootpartner.fragments.BankDetailsFragment
import com.example.akhleshkumar.homedootpartner.fragments.BusinessDetailFragment
import com.example.akhleshkumar.homedootpartner.fragments.CalenderFragment
import com.example.akhleshkumar.homedootpartner.fragments.CommisionFragment
import com.example.akhleshkumar.homedootpartner.fragments.HomeFragment
import com.example.akhleshkumar.homedootpartner.fragments.JobHistoryFragment
import com.example.akhleshkumar.homedootpartner.fragments.MyProfileFragment
import com.example.akhleshkumar.homedootpartner.fragments.RatingFragment
import com.example.akhleshkumar.homedootpartner.fragments.WalletFragment
import com.example.akhleshkumar.homedootpartner.models.ReviewResponse
import com.example.akhleshkumar.homedootpartner.models.VendorDashboardResponse
import com.razorpay.PaymentData
import com.razorpay.PaymentResultListener
import com.razorpay.PaymentResultWithDataListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), PaymentResultWithDataListener {
    private lateinit var binding: ActivityMainBinding

    lateinit var progressDialog: ProgressDialog
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editorSP : SharedPreferences.Editor
    lateinit var  headerBinding : NavHeaderBinding
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

        loadFragment(HomeFragment())
        headerBinding = NavHeaderBinding.bind(binding.navigationView.getHeaderView(0))
        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            // Close Drawer
//            binding.drawerLayout.closeDrawer(GravityCompat.START)

            // Show Toast based on clicked item
            when (menuItem.itemId) {
                R.id.menu_calendar ->{ loadFragment(CalenderFragment())
                    binding.drawerLayout.closeDrawers()

                }
                R.id.menu_job_history -> { startActivity(Intent(this,OrdersActivity::class.java).putExtra("from","completed"))
                    binding.drawerLayout.closeDrawers()
                }
                R.id.menu_profile ->{ loadFragment(MyProfileFragment())
                    binding.drawerLayout.closeDrawers()

                }
                R.id.menu_business_detail ->{ loadFragment(BusinessDetailFragment())

                    binding.drawerLayout.closeDrawers()

                }
                R.id.menu_bank_detail ->{ loadFragment(BankDetailsFragment())
                    binding.drawerLayout.closeDrawers()

                }
                R.id.menu_rating ->{ loadFragment(RatingFragment())
                    binding.drawerLayout.closeDrawers()
                }
                R.id.menu_commission ->{ loadFragment(CommisionFragment())
                    binding.drawerLayout.closeDrawers()

                }
                R.id.log_out -> {
                    sharedPreferences.all.clear()
                    editorSP.clear()
                    editorSP.apply()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }

                else -> showToast("Unknown Item Clicked")
            }
            true
        }

        binding.bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when(menuItem.itemId){
                R.id.home -> loadFragment(HomeFragment())
                R.id.money -> loadFragment(WalletFragment())
                R.id.newJob -> loadFragment(JobHistoryFragment())
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
        getVendorRating(sharedPreferences.getInt("vendor_id",0).toString())
        getVendorDashboard(sharedPreferences.getInt("vendor_id",0).toString())
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    fun getVendorRating(userId: String){
        RetrofitClient.instance.vendorReview(userId).enqueue(object : Callback<ReviewResponse>{
            override fun onResponse(
                call: Call<ReviewResponse>,
                response: Response<ReviewResponse>
            ) {
                if (response.isSuccessful){
                    if (response.body()!!.success){
                        var rating = 0
                        for (rate in response.body()!!.data.ratingCount){
                            rating += rate.rating
                        }
                        rating /= response.body()!!.data.ratingCount.size
                        headerBinding.tvRating.text = "⭐ "+rating.toString()
                    }
                }
            }

            override fun onFailure(call: Call<ReviewResponse>, t: Throwable) {

            }
        })
    }


    private fun getVendorDashboard(userId:String) {
        RetrofitClient.instance.getVendorDashboard(userId).enqueue(object :
            Callback<VendorDashboardResponse> {
            override fun onResponse(
                call: Call<VendorDashboardResponse>,
                response: Response<VendorDashboardResponse>
            ) {
                if (response.isSuccessful) {
                    if (response.body()!!.success) {
                        headerBinding.tvName.text = response.body()!!.data.vendorDetails.name
                        Toast.makeText(
                            this@MainActivity,
                            response.body()?.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<VendorDashboardResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "something went wrong", Toast.LENGTH_SHORT).show()
            }

        })
    }
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


    override fun onPaymentSuccess(p0: String?, p1: PaymentData?) {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as? WalletFragment
        fragment?.onPaymentSuccess(p0,p1)
    }

    override fun onPaymentError(p0: Int, p1: String?, p2: PaymentData?) {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as? WalletFragment
        fragment?.onPaymentError(p0,p1,p2)
    }
}