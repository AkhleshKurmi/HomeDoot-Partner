package com.example.akhleshkumar.homedootpartner.adaters

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.akhleshkumar.homedootpartner.databinding.ItemViewOrPendingJobBinding
import com.example.akhleshkumar.homedoot.api.RetrofitClient
import com.example.akhleshkumar.homedootpartner.models.CancelOrderResponse
import com.example.akhleshkumar.homedootpartner.models.OrderResponse
import com.example.akhleshkumar.homedootpartner.models.OrderResponses
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrdersAdapter(val context: Context, val list:ArrayList<OrderResponse>, val status:String) : RecyclerView.Adapter<OrdersAdapter.ItemViewHolder>(){
    inner class ItemViewHolder(val binding: ItemViewOrPendingJobBinding) : RecyclerView.ViewHolder(binding.root)
    val sharedPreferences = context.getSharedPreferences("HomeDoot", Context.MODE_PRIVATE)
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
        if (status == "completed" || status == "cancelled") {
            holder.binding.btnAccept.visibility = View.GONE
            holder.binding.acceptCash.visibility = View.GONE
            holder.binding.updateStatus.visibility = View.GONE
        }
        if(data.orderStatus == "pending") {

            if(data.assignedOrder.vendorAccepted==0){

                holder.binding.btnAccept.visibility = View.VISIBLE
                holder.binding.acceptCash.visibility = View.GONE
                holder.binding.updateStatus.visibility = View.GONE

            } else {

                if (data.cashAccepted == 1){
                    holder.binding.acceptCash.isClickable = false
                    holder.binding.acceptCash.text = "Cash Accepted"
                    holder.binding.btnAccept.visibility = View.GONE
                    holder.binding.jobStarted.visibility = View.VISIBLE
                    holder.binding.acceptCash.visibility = View.VISIBLE
                    holder.binding.updateStatus.visibility = View.VISIBLE
                }
                else {
                    holder.binding.jobStarted.visibility = View.VISIBLE
                    holder.binding.btnAccept.visibility = View.GONE
                    holder.binding.acceptCash.visibility = View.VISIBLE
                    holder.binding.updateStatus.visibility = View.VISIBLE
                }
            }
        } else{
            holder.binding.btnAccept.visibility = View.GONE
            holder.binding.acceptCash.visibility = View.GONE
            holder.binding.updateStatus.visibility = View.GONE
            holder.binding.jobStarted.visibility = View.GONE
        }

//        if (data.cashAccepted == 1){
//            holder.binding.btnAccept.visibility = View.GONE
//            holder.binding.acceptCash.visibility = View.GONE
//            holder.binding.updateStatus.visibility = View.VISIBLE
//        }
//
//        if(data.assignedOrder.vendorAccepted==0){
//            holder.binding.btnAccept.visibility = View.VISIBLE
//            holder.binding.acceptCash.visibility = View.GONE
//            holder.binding.updateStatus.visibility = View.GONE
//        } else {
//
//            holder.binding.btnAccept.visibility = View.GONE
//            holder.binding.acceptCash.visibility = View.VISIBLE
//            holder.binding.updateStatus.visibility = View.GONE
//        }
        holder.binding.updateStatus.setOnClickListener {
            val dialog = AlertDialog.Builder(context)
            dialog.setTitle("Update Status")
            dialog.setMessage("Are you sure you want to update the status?")
            dialog.setPositiveButton("Completed") { _, _ ->
                RetrofitClient.instance.updateStatus(
                    sharedPreferences.getInt("vendor_id", 0).toString(),
                    data.orderNo,
                    "completed").enqueue(object : Callback<OrderResponses>{
                    override fun onResponse(
                        call: Call<OrderResponses>,
                        response: Response<OrderResponses>
                    ) {
                        if (response.isSuccessful) {
                            if (response.body()?.status!!) {
                                holder.binding.acceptCash.visibility = View.GONE
                                holder.binding.updateStatus.visibility = View.GONE
                                holder.binding.btnAccept.visibility = View.GONE
                                holder.binding.jobStarted.visibility = View.GONE

                                Toast.makeText(
                                    context,
                                    response.body()?.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            else{
                                Toast.makeText(
                                    context,
                                    response.body()?.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }

                    override fun onFailure(call: Call<OrderResponses>, t: Throwable) {
                        Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                    }

                })
            }
            dialog.setNegativeButton("Cancelled") { _, _ ->
                RetrofitClient.instance.updateStatus(
                    sharedPreferences.getInt("vendor_id", 0).toString(),
                    data.orderNo,
                    "cancelled").enqueue(object : Callback<OrderResponses>{
                    override fun onResponse(
                        call: Call<OrderResponses>,
                        response: Response<OrderResponses>
                    ) {
                        if (response.isSuccessful) {
                            if (response.body()?.status!!) {
                                holder.binding.acceptCash.visibility = View.GONE
                                holder.binding.updateStatus.visibility = View.GONE
                                holder.binding.btnAccept.visibility = View.GONE
                                holder.binding.jobStarted.visibility = View.GONE
                                cancelledOrder(data.razorOrderId)
                                Toast.makeText(
                                    context,
                                    response.body()?.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            else{
                                Toast.makeText(
                                    context,
                                    response.body()?.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }

                    override fun onFailure(call: Call<OrderResponses>, t: Throwable) {
                        Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                    }

                })

            }

            dialog.show()

        }

        holder.binding.acceptCash.setOnClickListener {
            val dialog = AlertDialog.Builder(context)
            dialog.setTitle("Cash Received")
            dialog.setMessage("Are you sure you have received the cash?")
            dialog.setPositiveButton("Received") { _, _ ->

                RetrofitClient.instance.acceptCash(data.orderNo)
                    .enqueue(object : Callback<CancelOrderResponse> {
                        override fun onResponse(
                            call: Call<CancelOrderResponse>,
                            response: Response<CancelOrderResponse>
                        ) {
                            if (response.isSuccessful) {
                                if (response.body()?.success!!) {
                                    holder.binding.acceptCash.isClickable = false
                                    holder.binding.acceptCash.text = "Cash Accepted"
                                    holder.binding.acceptCash.visibility = View.VISIBLE
                                    holder.binding.updateStatus.visibility = View.VISIBLE
                                    holder.binding.jobStarted.visibility = View.VISIBLE
                                    holder.binding.btnAccept.visibility = View.GONE
                                    Toast.makeText(
                                        context,
                                        response.body()?.message,
                                        Toast.LENGTH_SHORT
                                    )
                                }
                                else{
                                    Toast.makeText(
                                        context,
                                        response.body()?.message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                            }

                        }

                        override fun onFailure(call: Call<CancelOrderResponse>, t: Throwable) {
                            Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                        }
                    })

            }
            dialog.setNegativeButton("Close") { _, _ ->
            }
            dialog.show()


        }

        holder.binding.jobStarted.setOnClickListener {
            val dialog = AlertDialog.Builder(context)
            dialog.setTitle("Job Started")
            dialog.setMessage("Are you sure you have started the job?")
            dialog.setPositiveButton("Started") { _, _ ->
                RetrofitClient.instance.jobStarted(data.orderNo.toString()).enqueue(object : Callback<CancelOrderResponse>{
                    override fun onResponse(
                        call: Call<CancelOrderResponse>,
                        response: Response<CancelOrderResponse>
                    ) {
                        if (response.isSuccessful) {
                            if (response.body()?.success!!) {
                                Toast.makeText(context, response.body()?.message, Toast.LENGTH_SHORT).show()

                            }
                            else{
                                Toast.makeText(context, response.body()?.message, Toast.LENGTH_SHORT).show()
                            }

                        } else{
                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onFailure(call: Call<CancelOrderResponse>, t: Throwable) {
                        Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                    }
                })
            }
            dialog.setNegativeButton("Close") { _, _ ->
            }
        }

            holder.binding.btnAccept.setOnClickListener {
                val dialog = AlertDialog.Builder(context)
                dialog.setTitle("Accept Order")
                dialog.setMessage("Are you sure you want to accept this order?")
                dialog.setPositiveButton("Accept Order") { _, _ ->
                    RetrofitClient.instance.acceptOrder(
                        sharedPreferences.getInt("vendor_id", 0).toString(),
                        data.orderNo.toString(),
                        data.grandTotal
                    ).enqueue(object : retrofit2.Callback<OrderResponses> {
                        override fun onResponse(
                            call: Call<OrderResponses>,
                            response: Response<OrderResponses>
                        ) {
                            if (response.isSuccessful) {
                                if (response.body()?.status!!) {
                                    holder.binding.acceptCash.visibility = View.VISIBLE
                                    holder.binding.updateStatus.visibility = View.VISIBLE
                                    holder.binding.jobStarted.visibility = View.VISIBLE
                                    holder.binding.btnAccept.visibility = View.GONE
                                    Toast.makeText(
                                        context,
                                        response.body()?.message,
                                        Toast.LENGTH_SHORT
                                    ).show()

                                } else {
                                    Toast.makeText(
                                        context,
                                        response.body()?.message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT)
                                    .show()
                            }

                        }

                        override fun onFailure(call: Call<OrderResponses>, t: Throwable) {
                            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT)
                                .show()
                        }

                    })
                }
                dialog.setNegativeButton("Close") { _, _ ->
                }

                dialog.show()
            }

        }

    private fun cancelledOrder(razorOrderId: String) {
        RetrofitClient.instance.cancelOrderDelete(razorOrderId,0.toString()).enqueue(object : Callback<CancelOrderResponse>{
            override fun onResponse(
                call: Call<CancelOrderResponse>,
                response: Response<CancelOrderResponse>
            ) {
                if (response.isSuccessful){
                    if(response.body()?.success!!){
                        Toast.makeText(context, response.body()?.message, Toast.LENGTH_SHORT).show()
                    } else{
                        Toast.makeText(context, response.body()?.message, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CancelOrderResponse>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }

        })

    }


}
