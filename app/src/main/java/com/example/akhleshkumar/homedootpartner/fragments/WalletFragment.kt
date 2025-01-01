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
import com.example.akhleshkumar.homedoot.api.RetrofitClient
import com.example.akhleshkumar.homedootpartner.adaters.TransactionAdapter
import com.example.akhleshkumar.homedootpartner.models.Transaction
import com.example.akhleshkumar.homedootpartner.models.VendorDashboardResponse
import com.example.akhleshkumar.homedootpartner.models.WalletHistoryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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
        sharedpref = requireActivity().getSharedPreferences("HomeDoot", AppCompatActivity.MODE_PRIVATE)
        editor = sharedpref.edit()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWalletBinding.inflate(layoutInflater)

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up RecyclerView

        binding.rvTransactions.layoutManager = LinearLayoutManager(requireContext())
        binding.rvDebitTransactions.layoutManager = LinearLayoutManager(requireContext())


        // Add Money Button Click
        binding.btnAddMoney.setOnClickListener {
            showAddMoneyDialog()
        }

        setWalletBalance()
        fetchDebitTransaction()
        fetchTransactions()
        // Initialize Wallet Balance

    }
fun setWalletBalance() {
    RetrofitClient.instance.getVendorDashboard(sharedpref.getInt("vendor_id",0).toString())
        .enqueue(object : Callback<VendorDashboardResponse> {
            override fun onResponse(
                call: Call<VendorDashboardResponse>,
                response: Response<VendorDashboardResponse>
            ) {
                if (response.isSuccessful){
                    val data = response.body()?.data
                    if (data != null) {
                        walletBalance = data.vendorDetails.wallet.toDouble()
                        updateWalletBalance()
                    }
                }
            }

            override fun onFailure(call: Call<VendorDashboardResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "error", Toast.LENGTH_SHORT).show()
            }

        })
}

    fun fetchTransactions() {
        RetrofitClient.instance.walletTransaction(sharedpref.getInt("vendor_id",0).toString(),"credit").enqueue(object : Callback<WalletHistoryResponse> {
            override fun onResponse(
                call: Call<WalletHistoryResponse>,
                response: Response<WalletHistoryResponse>
            ) {
                if (response.isSuccessful) {
                    if (response.body()!!.success) {
                        if (response.body()!!.data.walletHistory !=null) {
                            binding.rvTransactions.adapter =
                                TransactionAdapter(response.body()!!.data.walletHistory, "Credit")

                        }
                    }
                }
            }

            override fun onFailure(call: Call<WalletHistoryResponse>, t: Throwable) {

            }

        })
    }

    fun fetchDebitTransaction(){
        RetrofitClient.instance.walletTransaction(sharedpref.getInt("vendor_id",0).toString(),"debit").enqueue(object : Callback<WalletHistoryResponse> {
            override fun onResponse(
                call: Call<WalletHistoryResponse>,
                response: Response<WalletHistoryResponse>
            ) {
                if (response.isSuccessful) {
                    if (response.body()!!.success) {
                        if (response.body()!!.data.walletHistory != null) {
                            binding.rvDebitTransactions.adapter =
                                TransactionAdapter(response.body()!!.data.walletHistory, "Debit")

                        }
                    }
                }
            }

            override fun onFailure(call: Call<WalletHistoryResponse>, t: Throwable) {

            }

        })
    }


    private fun showAddMoneyDialog() {
        val dialogBinding = DialogAddMoneyBinding.inflate(LayoutInflater.from(requireContext()))

        AlertDialog.Builder(requireContext())
            .setTitle("Add Money")
            .setView(dialogBinding.root)
            .setPositiveButton("Add") { _, _ ->
                val amount = dialogBinding.etAmount.text.toString().toDoubleOrNull()
                if (amount != null && amount > 0) {

                } else {
                    Toast.makeText(requireContext(), "Invalid Amount", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }




    private fun updateWalletBalance() {
        binding.tvWalletBalance.text = "₹ $walletBalance"
    }


}