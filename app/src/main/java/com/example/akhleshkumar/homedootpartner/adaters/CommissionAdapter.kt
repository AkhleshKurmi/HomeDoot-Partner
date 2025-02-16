package com.example.akhleshkumar.homedootpartner.adaters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akhleshkumar.homedootpartner.databinding.IttemInvoiceViewBinding
import com.example.akhleshkumar.homedootpartner.models.Invoice

class CommissionAdapter(val context: Context, val invoiceList: List<Invoice>)  :
    RecyclerView.Adapter<CommissionAdapter.InvoiceViewHolder>() {

        class InvoiceViewHolder(val binding: IttemInvoiceViewBinding) :
            RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InvoiceViewHolder {
            val binding = IttemInvoiceViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return InvoiceViewHolder(binding)
        }

        override fun onBindViewHolder(holder: InvoiceViewHolder, position: Int) {
            val invoice = invoiceList[position]
            holder.binding.tvOrderNo.text = "Order No: \n ${invoice.orderNo}"
            holder.binding.tvServiceName.text = "Service: \n ${invoice.serviceName}"
            holder.binding.tvPrice.text = "Price: \n ₹${invoice.price}"
            holder.binding.tvStatus.text = "Status: \n ${invoice.statusFromVendor}"
            holder.binding.tvCommission.text = "Commission: \n ₹${invoice.commission}"
        }

        override fun getItemCount(): Int = invoiceList.size

    }
