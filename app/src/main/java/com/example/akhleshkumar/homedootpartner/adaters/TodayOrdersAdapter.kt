package com.example.akhleshkumar.homedootpartner.adaters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akhleshkumar.homedootpartner.databinding.ItemViewJobCardBinding
import com.example.akhleshkumar.homedootpartner.models.OrderResponse

class TodayOrdersAdapter(val list: List<OrderResponse>) : RecyclerView.Adapter<TodayOrdersAdapter.TodayOrdersViewHolder>() {
    inner class TodayOrdersViewHolder(val binding: ItemViewJobCardBinding ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodayOrdersViewHolder {
        val binding = ItemViewJobCardBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return TodayOrdersViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: TodayOrdersViewHolder, position: Int) {
        val data = list[position]
        holder.binding.jobTime.text = data.serviceTime
        holder.binding.jobStatus.text = data.orderStatus
        holder.binding.jobPerson.text = data.customers.name


    }
}