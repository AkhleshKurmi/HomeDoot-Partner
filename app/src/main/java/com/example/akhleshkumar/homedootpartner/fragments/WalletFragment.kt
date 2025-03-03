package com.example.akhleshkumar.homedootpartner.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.akhleshkumar.homedootpartner.databinding.DialogAddMoneyBinding
import com.akhleshkumar.homedootpartner.databinding.FragmentWalletBinding
import com.example.akhleshkumar.homedoot.api.RetrofitClient
import com.example.akhleshkumar.homedootpartner.adaters.TransactionAdapter
import com.example.akhleshkumar.homedootpartner.models.Transaction
import com.example.akhleshkumar.homedootpartner.models.VendorDashboardResponse
import com.example.akhleshkumar.homedootpartner.models.WalletHistoryResponse
import com.example.akhleshkumar.homedootpartner.models.WalletResponse
import com.razorpay.Checkout
import com.razorpay.PayloadHelper
import com.razorpay.PaymentData
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WalletFragment : Fragment() {
    lateinit var binding: FragmentWalletBinding
    lateinit var sharedpref: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    private var walletBalance = 0.0
    private var price = 0.0
    private val transactions = mutableListOf<Transaction>()
    private lateinit var adapter: TransactionAdapter
    lateinit var co :Checkout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedpref = requireActivity().getSharedPreferences("HomeDoot", AppCompatActivity.MODE_PRIVATE)
        editor = sharedpref.edit()
        co = Checkout()
//        co.setKeyID("rzp_test_vNW8R8FeHAqIzA")
        co.setKeyID("rzp_live_HeICphb9DMsZH5")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWalletBinding.inflate(layoutInflater)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
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
        RetrofitClient.instance.walletTransaction(sharedpref.getInt("vendor_id",0),"credit").enqueue(object : Callback<WalletHistoryResponse> {
            override fun onResponse(
                call: Call<WalletHistoryResponse>,
                response: Response<WalletHistoryResponse>
            ) {
                if (response.isSuccessful) {
                    if (response.body()!!.success) {
                        if (response.body()!!.data.wallet_history !=null) {
                            binding.rvTransactions.adapter =
                                TransactionAdapter(response.body()!!.data.wallet_history, "Credit")

                        }
                    }
                }
            }

            override fun onFailure(call: Call<WalletHistoryResponse>, t: Throwable) {

            }

        })
    }

    fun fetchDebitTransaction(){
        RetrofitClient.instance.walletTransaction(sharedpref.getInt("vendor_id",0),"debit").enqueue(object : Callback<WalletHistoryResponse> {
            override fun onResponse(
                call: Call<WalletHistoryResponse>,
                response: Response<WalletHistoryResponse>
            ) {
                if (response.isSuccessful) {
                    if (response.body()!!.success) {
                        if (response.body()!!.data.wallet_history != null) {
                            binding.rvDebitTransactions.adapter =
                                TransactionAdapter(response.body()!!.data.wallet_history, "Debit")
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
                    price = amount
                paymentInit(amount)
                } else {
                    Toast.makeText(requireContext(), "Invalid Amount", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    fun paymentInit(amount: Double){
        RetrofitClient.instance.fillWallet(sharedpref.getInt("vendor_id",0),amount.toString()).enqueue(object : Callback<WalletResponse>{
            override fun onResponse(
                call: Call<WalletResponse>,
                response: Response<WalletResponse>
            ) {
                if (response.isSuccessful){
                    if (response.body()!!.success){
                        initializePayment(response.body()!!.data.total.toDouble(),response.body()!!.data.razorOrderId)

                    }
                }

                }
            override fun onFailure(call: Call<WalletResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "error: "+t.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }
    fun initializePayment(amount: Double, orderId:String){

        setWalletGateway(amount,orderId)

    }
    private fun setWalletGateway(amount: Double,orderId:String) {

//
        val activity = requireActivity()
        try {
            val options = JSONObject()
            options.put("name","HomeDoot Partner")
            options.put("description","Wallet Add Payment")
            options.put("order_id",orderId.toString())
            options.put("currency","INR");
            options.put("amount",amount*100)//pass amount in currency subunits
            val retryObj = JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 3);
            options.put("retry", retryObj);

            val prefill = JSONObject()
            prefill.put("email",sharedpref.getString("userName","").toString())
            prefill.put("contact",sharedpref.getString("mobile","").toString())
            options.put("prefill",prefill)
            co.open(requireActivity(),options)
        }catch (e: Exception){
            Toast.makeText(activity,"Error in payment: "+ e.message,Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }



    private fun updateWalletBalance() {
        binding.tvWalletBalance.text = "₹ $walletBalance"
    }


    private fun successPayment(amount: String,p1: PaymentData?) {
        RetrofitClient.instance.paymentSuccess(sharedpref.getInt("vendor_id",0),amount.toString(),0.toString(),amount.toString(),p1!!.paymentId.toString(),
            p1.orderId,p1.signature.toString()).enqueue(object :
            Callback<WalletResponse> {
            override fun onResponse(
                call: Call<WalletResponse>,
                response: Response<WalletResponse>
            ) {
                if (response.isSuccessful){
                    if (response.body()!!.success){
                        Toast.makeText(requireContext(), response.body()!!.message, Toast.LENGTH_SHORT).show()
                        setWalletBalance()
                        fetchTransactions()
                        fetchDebitTransaction()
                    }
                }
            }
            override fun onFailure(call: Call<WalletResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "error: "+t.localizedMessage, Toast.LENGTH_SHORT).show()
            }

        })

    }

     fun onPaymentSuccess(p0: String?, p1: PaymentData?) {
        successPayment(price.toString(),p1)
    }

     fun onPaymentError(p0: Int, p1: String?, p2: PaymentData?) {
        Toast.makeText(requireContext(), "Payment Failed", Toast.LENGTH_SHORT).show()
    }

}