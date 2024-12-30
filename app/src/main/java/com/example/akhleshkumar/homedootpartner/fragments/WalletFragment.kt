package com.example.akhleshkumar.homedootpartner.fragments

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.akhleshkumar.homedootpartner.R
import com.akhleshkumar.homedootpartner.databinding.DialogAddMoneyBinding
import com.akhleshkumar.homedootpartner.databinding.FragmentWalletBinding
import com.example.akhleshkumar.homedootpartner.adaters.TransactionAdapter
import com.example.akhleshkumar.homedootpartner.models.Transaction
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class WalletFragment : Fragment() {
    lateinit var binding: FragmentWalletBinding
    lateinit var sharedpref: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    private var walletBalance = 0.0
    private val transactions = mutableListOf<Transaction>()
    private lateinit var adapter: TransactionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWalletBinding.inflate(layoutInflater)
        sharedpref = requireActivity().getSharedPreferences("HomeDoot", AppCompatActivity.MODE_PRIVATE)
        editor = sharedpref.edit()
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up RecyclerView

        binding.rvTransactions.layoutManager = LinearLayoutManager(requireContext())


        // Add Money Button Click
        binding.btnAddMoney.setOnClickListener {
            showAddMoneyDialog()
        }

        // Initialize Wallet Balance
        updateWalletBalance()
    }

    private fun showAddMoneyDialog() {
        val dialogBinding = DialogAddMoneyBinding.inflate(LayoutInflater.from(requireContext()))

        AlertDialog.Builder(requireContext())
            .setTitle("Add Money")
            .setView(dialogBinding.root)
            .setPositiveButton("Add") { _, _ ->
                val amount = dialogBinding.etAmount.text.toString().toDoubleOrNull()
                if (amount != null && amount > 0) {
                    addMoneyToWallet(amount)
                } else {
                    Toast.makeText(requireContext(), "Invalid Amount", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun addMoneyToWallet(amount: Double) {

        adapter.notifyItemInserted(transactions.size - 1)
    }

    private fun updateWalletBalance() {
        binding.tvWalletBalance.text = "₹ $walletBalance"
    }


}