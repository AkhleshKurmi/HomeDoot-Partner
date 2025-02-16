package com.example.akhleshkumar.homedootpartner.fragments

import android.app.DatePickerDialog
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.akhleshkumar.homedootpartner.R
import com.akhleshkumar.homedootpartner.databinding.FragmentCommisionBinding
import com.example.akhleshkumar.homedoot.api.RetrofitClient
import com.example.akhleshkumar.homedootpartner.adaters.CommissionAdapter
import com.example.akhleshkumar.homedootpartner.models.VendorCommissionResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class CommisionFragment : Fragment() {
    lateinit var binding: FragmentCommisionBinding
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
     var dateStart:String = ""
    var dateEnd:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
     sharedPreferences = requireActivity().getSharedPreferences("HomeDoot", MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCommisionBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
       binding.btnStartDate.setOnClickListener {
         showDatePickerDialog(binding.btnStartDate)
       }
       binding.btnEndDate.setOnClickListener {
            showDatePickerDialog(binding.btnEndDate)
       }

        binding.btnSearch.setOnClickListener {
            if (dateStart.isNotEmpty() && dateEnd.isNotEmpty()){
                RetrofitClient.instance.getVendorCommission(sharedPreferences.getInt("vendor_id",0),dateStart,dateEnd,"search").enqueue(object :
                    Callback<VendorCommissionResponse> {
                    override fun onResponse(
                        call: Call<VendorCommissionResponse>,
                        response: Response<VendorCommissionResponse>
                    ) {
                        if (response.isSuccessful){
                            if (response.body()?.success!!){
                                val data = response.body()?.data
                                if (data != null){
                                    if (data.invoices.isNullOrEmpty()){
                                        Toast.makeText(requireContext(),"No invoices found", Toast.LENGTH_SHORT).show()
                                    }
                                    else{
                                        binding.recyclerView.adapter = CommissionAdapter(requireContext(),data.invoices)
                                    }
                                    if(data.orderItems.isNullOrEmpty()){
                                        Toast.makeText(requireContext(),"No order items found", Toast.LENGTH_SHORT).show()
                                    }
                                    else{

                                    }
                                }
                            }
                            else{
                                Toast.makeText(requireContext(),response.body()?.message, Toast.LENGTH_SHORT).show()
                            }

                        }
                        else{
                            Toast.makeText(requireContext(),response.message(), Toast.LENGTH_SHORT).show()

                        }
                    }

                    override fun onFailure(call: Call<VendorCommissionResponse>, t: Throwable) {
                        Toast.makeText(requireContext(),t.message, Toast.LENGTH_SHORT).show()
                    }

                })
            }else {
                Toast.makeText(requireContext(),"Please select date", Toast.LENGTH_SHORT).show()

            }

        }
    }

    private fun showDatePickerDialog(txtSelectedDate: Button) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        var formattedDate = ""
        val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            // Format the selected date
            val selectedCalendar = Calendar.getInstance()
            selectedCalendar.set(selectedYear, selectedMonth, selectedDay)

            val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            formattedDate = dateFormat.format(selectedCalendar.time)

            // Display the selected date in TextView
            txtSelectedDate.text = formattedDate
            when(txtSelectedDate.id) {
                R.id.btnStartDate -> {
                    dateStart = formattedDate
                }

                R.id.btnEndDate -> {
                    dateEnd = formattedDate
                }
            }
        }, year, month, day)

        datePickerDialog.show()
    }
}