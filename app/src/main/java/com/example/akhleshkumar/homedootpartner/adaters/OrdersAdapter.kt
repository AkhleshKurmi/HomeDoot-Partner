package com.example.akhleshkumar.homedootpartner.adaters

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.akhleshkumar.homedootpartner.databinding.ItemViewOrPendingJobBinding
import com.example.akhleshkumar.homedoot.api.RetrofitClient
import com.example.akhleshkumar.homedootpartner.models.CancelOrderResponse
import com.example.akhleshkumar.homedootpartner.models.OrderResponse
import retrofit2.Call
import retrofit2.Response

class OrdersAdapter(val context: Context, val list:ArrayList<OrderResponse>) : RecyclerView.Adapter<OrdersAdapter.ItemViewHolder>(){
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

           holder.binding.btnAccept.setOnClickListener {
               val dialog = AlertDialog.Builder(context)
               dialog.setTitle("Accept Order")
               dialog.setMessage("Are you sure you want to accept this order?")
               dialog.setPositiveButton("Accept Order"){_ , _ ->


                   RetrofitClient.instance.acceptOrder(sharedPreferences.getInt("vendor_id",0).toString(),data.orderNo.toString(),data.grandTotal).enqueue(object : retrofit2.Callback<CancelOrderResponse>{
                       override fun onResponse(
                           call: Call<CancelOrderResponse>,
                           response: Response<CancelOrderResponse>
                       ) {
                           if (response.isSuccessful){
                               if (response.body()?.success!!){
                                  Toast.makeText(context,response.body()?.message, Toast.LENGTH_SHORT).show()
                               }
                               else{
                                   Toast.makeText(context,response.body()?.message ,Toast.LENGTH_SHORT).show()
                               }
                           }else{
                               Toast.makeText(context,"Something went wrong", Toast.LENGTH_SHORT).show()
                           }

                       }

                       override fun onFailure(call: Call<CancelOrderResponse>, t: Throwable) {
                           Toast.makeText(context,"Something went wrong", Toast.LENGTH_SHORT).show()
                       }

                   })
               }
               dialog.setNegativeButton("Close"){_ , _ ->
               }

               dialog.show()
           }

    }

}
