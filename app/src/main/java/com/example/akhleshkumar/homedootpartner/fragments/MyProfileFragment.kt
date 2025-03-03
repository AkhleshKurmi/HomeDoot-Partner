package com.example.akhleshkumar.homedootpartner.fragments

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.akhleshkumar.homedootpartner.databinding.FragmentMyProfileBinding
import com.example.akhleshkumar.homedoot.api.RetrofitClient
import com.example.akhleshkumar.homedootpartner.activities.UpdatefrofileActivity
import com.example.akhleshkumar.homedootpartner.models.VendorDashboardResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyProfileFragment : Fragment() {

    lateinit var binding : FragmentMyProfileBinding
    val sharedPreferences by lazy {
        requireActivity().getSharedPreferences("HomeDoot",MODE_PRIVATE)
    }
    val editor by lazy {
        sharedPreferences.edit()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyProfileBinding.inflate(layoutInflater,container,false)


        binding.updateProfileButton.setOnClickListener {
            startActivity(Intent(requireContext(), UpdatefrofileActivity::class.java))
        }
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        RetrofitClient.instance.getVendorDashboard(sharedPreferences.getInt("vendor_id",0).toString()).enqueue(object :
            Callback<VendorDashboardResponse> {
            override fun onResponse(
                call: Call<VendorDashboardResponse>,
                response: Response<VendorDashboardResponse>
            ) {
                if (response.isSuccessful){
                    binding.usernameTextView.text = response.body()!!.data.vendorDetails.name
                    binding.emailTextView.text = response.body()!!.data.vendorDetails.email
                    binding.mobileNoTextView.text = response.body()!!.data.vendorDetails.mobile
                    binding.addressTextView.text = response.body()!!.data.vendorDetails.address
                    binding.cityTextView.text = response.body()!!.data.vendorDetails.city.toString()
                    binding.pincodeTextView.text = response.body()!!.data.vendorDetails.pincode
                    binding.stateTextView.text = response.body()!!.data.vendorDetails.state.toString()


                }
            }

            override fun onFailure(call: Call<VendorDashboardResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
            }

        })
    }

}