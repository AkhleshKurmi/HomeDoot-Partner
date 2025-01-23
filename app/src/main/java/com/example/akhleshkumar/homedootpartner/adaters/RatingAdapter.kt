package com.example.akhleshkumar.homedootpartner.adaters

import android.media.Rating
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akhleshkumar.homedootpartner.databinding.ReviewItemBinding
import com.example.akhleshkumar.homedootpartner.models.Review

class RatingAdapter(val listRating: List<Review>) : RecyclerView.Adapter<RatingAdapter.RatingViewHolder>() {
    inner class RatingViewHolder(val binding: ReviewItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatingViewHolder {
        val binding = ReviewItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return RatingViewHolder(binding)
    }

    override fun getItemCount(): Int = listRating.size

    override fun onBindViewHolder(holder: RatingViewHolder, position: Int) {
        val itemList = listRating[position]
        holder.binding.ratingBar.rating = itemList.rating
        holder.binding.ratingCountTextView.text  = itemList.rating.toString()
        holder.binding.usernameTextView.text = itemList.customer.name
        holder.binding.reviewContentTextView.text = itemList.review
        holder.binding.orderTextView.text = "Order No.: "+itemList.orderNo
        holder.binding.dateRating.text = "Date: "+itemList.createdAt


    }
}