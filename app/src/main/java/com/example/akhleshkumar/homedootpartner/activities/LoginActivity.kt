package com.example.akhleshkumar.homedootpartner.activities

import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.akhleshkumar.homedoot.api.RetrofitClient
import com.example.akhleshkumar.homedootpartner.databinding.ActivityLoginBinding
import com.example.akhleshkumar.homedootpartner.models.user.LoginUserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    lateinit var progressDialog: ProgressDialog
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editorSP : SharedPreferences.Editor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences("HomeDoot", MODE_PRIVATE)
        editorSP = sharedPreferences.edit()
        progressDialog = ProgressDialog(this).apply {
            setMessage("Loading...")
            setCancelable(false)
        }
        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }

        binding.buttonLogin.setOnClickListener {
            if (checkValidation()){
                login(binding.editTextEmail.text.toString(),binding.editTextPassword.text.toString())
            }
        }

    }
    fun login(userName:String, password:String){
        progressDialog.show()
        RetrofitClient.instance.userLogin(userName,"vendor", password).enqueue(
            object : Callback<LoginUserResponse> {
                override fun onResponse(
                    call: Call<LoginUserResponse>,
                    response: Response<LoginUserResponse>
                ) {
                    if (response.isSuccessful){
                        if (response.body()!!.success){
                            val data = response.body()!!.data
                            editorSP.putInt("userId",data.id)
                            editorSP.putString("userName",response.body()!!.data.email)
                            editorSP.putString("password",binding.editTextPassword.text.toString())
                            editorSP.putString("mobile",response.body()!!.data.mobile)
                            editorSP.putString("name",data.name)
                            editorSP.putString("cityS",data.city.toString())
                            editorSP.putString("stateS",data.state.toString())
                            editorSP.putString("addressS",data.address)
                            editorSP.putString("pincodeS",data.pincode.toString())
                            editorSP.putBoolean("isLogin", true)
                            editorSP.commit()
                                progressDialog.dismiss()
                                startActivity(Intent(this@LoginActivity, MainActivity::class.java)

                                    .putExtra("fragment","h"))
                                finish()

                        }
                        else{
                            Toast.makeText(this@LoginActivity, response.body()!!.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }

                override fun onFailure(call: Call<LoginUserResponse>, t: Throwable) {
                    Log.d("TAG", "onFailure: ${t.localizedMessage}")
                    progressDialog.dismiss()
                    Toast.makeText(this@LoginActivity, "Invalid credentials", Toast.LENGTH_SHORT).show()
                }

            })
    }


    fun checkValidation(): Boolean{
        if (binding.editTextEmail.text.toString().isEmpty()){
            binding.editTextEmail.error = "Enter username"
            return false
        }
        if(binding.editTextPassword.text.toString().isEmpty()){
            binding.editTextPassword.error = "Enter password"
            return false
        }

        return true
    }
}