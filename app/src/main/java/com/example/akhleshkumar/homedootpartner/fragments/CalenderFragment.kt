package com.example.akhleshkumar.homedootpartner.fragments

import android.app.Activity
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.akhleshkumar.homedootpartner.R
import com.akhleshkumar.homedootpartner.databinding.FragmentCalenderBinding
import com.example.akhleshkumar.homedoot.api.RetrofitClient
import com.example.akhleshkumar.homedootpartner.models.CancelOrderResponse
import com.example.akhleshkumar.homedootpartner.models.VendorCommissionResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CalenderFragment : Fragment() {

    lateinit var binding : FragmentCalenderBinding
    lateinit var progressDialog: ProgressDialog
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editorSP : SharedPreferences.Editor
    var dateStart:String = ""
    var dateEnd:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = requireActivity().getSharedPreferences("HomeDoot", Activity.MODE_PRIVATE)
        editorSP = sharedPreferences.edit()
        progressDialog = ProgressDialog(requireContext()).apply {
            setMessage("Loading...")
            setCancelable(false)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
          binding = FragmentCalenderBinding.inflate(layoutInflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnStartDate.setOnClickListener {
            showDatePickerDialog(binding.btnStartDate)
        }
        binding.btnEndDate.setOnClickListener {
            showDatePickerDialog(binding.btnEndDate)
        }

        binding.btnUnavalable.setOnClickListener {
            if (dateStart.isNotEmpty() && dateEnd.isNotEmpty()){
                RetrofitClient.instance.vendorAvailability(sharedPreferences.getInt("vendor_id",0),"$dateStart-$dateEnd").enqueue(object :
                    Callback<CancelOrderResponse> {
                    override fun onResponse(
                        call: Call<CancelOrderResponse>,
                        response: Response<CancelOrderResponse>
                    ) {
                        if (response.isSuccessful){
                            if (response.body()?.success!!){
                                   Toast.makeText(requireContext(),response.body()?.message, Toast.LENGTH_SHORT).show()

                            }
                            else{
                                Toast.makeText(requireContext(),response.body()?.message, Toast.LENGTH_SHORT).show()
                            }

                        }
                        else{
                            Toast.makeText(requireContext(),response.message(), Toast.LENGTH_SHORT).show()

                        }
                    }

                    override fun onFailure(call: Call<CancelOrderResponse>, t: Throwable) {
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

            val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
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