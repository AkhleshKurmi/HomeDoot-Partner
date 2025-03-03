package com.example.akhleshkumar.homedootpartner.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.akhleshkumar.homedootpartner.databinding.ActivityLoginBinding
import com.example.akhleshkumar.homedoot.api.RetrofitClient
import com.example.akhleshkumar.homedootpartner.models.user.LoginUserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    lateinit var progressDialog: ProgressDialog
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editorSP : SharedPreferences.Editor
    private val STORAGE_PERMISSION_CODE = 101
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences("HomeDoot", MODE_PRIVATE)
        editorSP = sharedPreferences.edit()
        checkAndRequestPermissions()
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
        binding.tvFpassword.setOnClickListener {
            startActivity(Intent(this,ForgotPasswordActivity::class.java))
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CAMERA),
                100)
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
                        progressDialog.dismiss()
                        if (response.body()!!.success){
                            val data = response.body()!!.data
                            if (data != null) {
                                editorSP.putInt("vendor_id", data.id)
                                editorSP.putString("userName", data.email)
                                editorSP.putString(
                                    "password",
                                    binding.editTextPassword.text.toString()
                                )
                                editorSP.putString("mobile", data.mobile)
                                editorSP.putString("name", data.name)
                                editorSP.putString("cityS", data.city.toString())
                                editorSP.putString("stateS", data.state.toString())
                                editorSP.putString("addressS", data.address)
                                editorSP.putString("pincodeS", data.pincode.toString())
                                editorSP.putBoolean("isLogin", true)
                                editorSP.commit()
                                progressDialog.dismiss()
                                startActivity(
                                    Intent(this@LoginActivity, MainActivity::class.java)

                                        .putExtra("fragment", "h")
                                )
                                finish()
                            }
                        }
                        else{
                            progressDialog.dismiss()
//                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))

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
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with file access

            } else {
                // Permission denied, show a message to the user
               checkAndRequestPermissions()
            }
        }
    }

    @SuppressLint("InlinedApi")
    private fun checkAndRequestPermissions() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_MEDIA_IMAGES
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES),
                STORAGE_PERMISSION_CODE
            )
        }
    }

}