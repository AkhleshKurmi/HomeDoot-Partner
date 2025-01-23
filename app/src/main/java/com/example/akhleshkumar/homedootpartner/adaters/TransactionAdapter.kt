package com.example.akhleshkumar.homedootpartner.adaters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akhleshkumar.homedootpartner.databinding.TransactionIitemBinding
import com.example.akhleshkumar.homedootpartner.models.Transaction

class TransactionAdapter(private val transactions: List<Transaction>, private val type :String) :
    RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    inner class TransactionViewHolder(val binding: TransactionIitemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding = TransactionIitemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactions[position]
        with(holder.binding) {
            tvTransactionType.text = type
            tvTransactionAmount.text = "₹ ${transaction.amount}"
            tvTransactionDate.text = transaction.updatedAt
            tvTransactionId.text = transaction.razorpayPaymentId
            tvTransactionType.setTextColor(
                if (type == "Credit") Color.GREEN else Color.RED
            )
        }
    }

    override fun getItemCount(): Int = transactions.size
}
