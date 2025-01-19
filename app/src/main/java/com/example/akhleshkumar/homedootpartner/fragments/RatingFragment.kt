package com.example.akhleshkumar.homedootpartner.fragments

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.akhleshkumar.homedootpartner.databinding.FragmentRatingListBinding
import com.example.akhleshkumar.homedoot.api.RetrofitClient
import com.example.akhleshkumar.homedootpartner.adaters.RatingAdapter
import com.example.akhleshkumar.homedootpartner.models.ReviewResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RatingFragment : Fragment() {

    lateinit var binding : FragmentRatingListBinding
    val sharedPreferences by lazy {
        requireActivity().getSharedPreferences("HomeDoot", MODE_PRIVATE)
    }
    val editor by lazy {
        sharedPreferences.edit()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = FragmentRatingListBinding.inflate(layoutInflater,container,false)

        // Set the adapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvListRating.layoutManager = LinearLayoutManager(requireContext())

        RetrofitClient.instance.vendorReview(sharedPreferences.getInt("vendor_id",0).toString()).enqueue(
            object : Callback<ReviewResponse> {
                override fun onResponse(
                    call: Call<ReviewResponse>,
                    response: Response<ReviewResponse>
                ) {
                    if (response.isSuccessful){
                        if (response.body()!!.success){
                            binding.rvListRating.adapter = RatingAdapter(response.body()!!.data.reviews)
                        }
                    }
                }

                override fun onFailure(call: Call<ReviewResponse>, t: Throwable) {
                    Toast.makeText(requireContext(), "failed", Toast.LENGTH_SHORT).show()
                }

            })


    }

}