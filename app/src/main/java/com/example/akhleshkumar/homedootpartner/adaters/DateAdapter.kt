package com.example.akhleshkumar.homedootpartner.adaters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akhleshkumar.homedootpartner.databinding.ItemViewJobAvailableBinding

class DateAdapter(private val dates: List<String>) : RecyclerView.Adapter<DateAdapter.DateItemViewHolder>() {
    inner  class DateItemViewHolder(val binding: ItemViewJobAvailableBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateItemViewHolder {
        val binding = ItemViewJobAvailableBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DateItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dates.size
    }

    override fun onBindViewHolder(holder: DateItemViewHolder, position: Int) {
        val date = dates[position]
        holder.binding.tvDate.text = date

    }


}