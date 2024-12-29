package com.example.akhleshkumar.homedootpartner.adaters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akhleshkumar.homedootpartner.databinding.ItemViewOrPendingJobBinding
import com.example.akhleshkumar.homedootpartner.models.OrderResponse

class OrdersAdapter(val context: Context, val list:ArrayList<OrderResponse>) : RecyclerView.Adapter<OrdersAdapter.ItemViewHolder>(){
    inner class ItemViewHolder(val binding: ItemViewOrPendingJobBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemViewOrPendingJobBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return  list.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {


                val data = list[position]
                holder.binding.tvOrderNo.text = data.orderNo
                holder.binding.tvServiceDate.text = data.serviceDate
                holder.binding.tvServiceTime.text = data.serviceTime
                holder.binding.tvSubTotal.text = data.subTotal.toString()
                holder.binding.tvDisc.text = data.discountTotal.toString()
                holder.binding.tvGrandTotal.text = data.grandTotal.toString()
                holder.binding.tvPaymentMode.text = data.paymentMethod
                holder.binding.tvAddress.text = data.address
                holder.binding.tvPlacedOn.text = data.updatedAt

    }
}
