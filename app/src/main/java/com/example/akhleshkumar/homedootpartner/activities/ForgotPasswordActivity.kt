package com.example.akhleshkumar.homedootpartner.activities

import android.app.Dialog
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.chaos.view.PinView
import com.example.akhleshkumar.homedoot.api.RetrofitClient
import com.example.akhleshkumar.homedootpartner.R
import com.example.akhleshkumar.homedootpartner.databinding.ActivityForgotPasswordBinding
import com.example.akhleshkumar.homedootpartner.models.user.ForgotPasswordResponse
import com.example.akhleshkumar.homedootpartner.models.user.UpdatePasswordResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPasswordActivity : AppCompatActivity() {

    lateinit var binding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnSendOtp.setOnClickListener {
            if (validation()){
                RetrofitClient.instance.forgotPassword(binding.usernameInput.text.toString(), "vendor").enqueue(object : Callback<ForgotPasswordResponse>{
                    override fun onResponse(
                        call: Call<ForgotPasswordResponse>,
                        response: Response<ForgotPasswordResponse>
                    ) {
                        if (response.isSuccessful){
                            if (response.body()!!.success){
                               validateOtp(response.body()!!.data.verificationCode.toString())
                            }
                            else{
                                Toast.makeText(this@ForgotPasswordActivity, response.body()!!.message, Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }

                    override fun onFailure(call: Call<ForgotPasswordResponse>, t: Throwable) {
                        Toast.makeText(this@ForgotPasswordActivity, t.localizedMessage, Toast.LENGTH_SHORT).show()
                    }

                })
            }
        }

    }
    fun validation() : Boolean{
        if (binding.usernameInput.text.toString().isEmpty()){
            binding.usernameInput.error= "Enter Confirm Password"
            return false
        }
        return true
    }


    fun validateOtp(verificationCode:String){
        val dialog = Dialog(this@ForgotPasswordActivity)
        dialog.setContentView(R.layout.dialog_otp)
        val window = dialog.window
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog.setCancelable(false)
        val etOtp = dialog.findViewById<PinView>(R.id.pinview)

        val btnValidate= dialog.findViewById<Button>(R.id.btnSubmitOtp)
        btnValidate.setOnClickListener {
            if(etOtp.text.toString().isEmpty()){
                Toast.makeText(this@ForgotPasswordActivity, "Enter full otp", Toast.LENGTH_SHORT).show()
            }
            else {
                if (etOtp.text.toString()== verificationCode) {
                   updatePassword()
                    dialog.dismiss()
                }else{
                    Toast.makeText(this@ForgotPasswordActivity, "Enter Correct OTP", Toast.LENGTH_SHORT).show()
                }
            }
        }

        dialog.show()

    }

    fun updatePassword(){
        val dialog = Dialog(this@ForgotPasswordActivity)
        dialog.setContentView(R.layout.dialog_forgot_password)
        val window = dialog.window
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        val password = dialog.findViewById<EditText>(R.id.etFpassword)
        val cnfPassword = dialog.findViewById<EditText>(R.id.etFCpassword)
        val userName = dialog.findViewById<EditText>(R.id.etEmail)
        val subButton = dialog.findViewById<Button>(R.id.btnSendResetLink)
        userName.setText(binding.usernameInput.text.toString())
        subButton.setOnClickListener {
            if (password.text.toString().isEmpty() && cnfPassword.text.toString().isEmpty()) {
                Toast.makeText(this@ForgotPasswordActivity, "Enter Password", Toast.LENGTH_SHORT).show()
            } else {
                RetrofitClient.instance.updatePassword(
                    binding.usernameInput.text.toString(),
                    "vendor",
                    password.text.toString(),
                    cnfPassword.text.toString()
                )
                    .enqueue(object : Callback<UpdatePasswordResponse> {
                        override fun onResponse(
                            call: Call<UpdatePasswordResponse>,
                            response: Response<UpdatePasswordResponse>

                        ) {
                            if (response.isSuccessful) {
                                if (response.body()!!.success) {
                                    Toast.makeText(
                                        this@ForgotPasswordActivity,
                                        response.body()!!.message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    finish()
                                } else{
                                    Toast.makeText(
                                        this@ForgotPasswordActivity,
                                        response.body()!!.message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }

                        override fun onFailure(call: Call<UpdatePasswordResponse>, t: Throwable) {
                            Toast.makeText(
                                this@ForgotPasswordActivity,
                                t.localizedMessage,
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    })
            }
        }
        dialog.show()
    }
}