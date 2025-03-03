package com.example.akhleshkumar.homedootpartner.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.akhleshkumar.homedootpartner.R

class SplashActivity : AppCompatActivity() {
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editorSP : SharedPreferences.Editor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        sharedPreferences = getSharedPreferences("HomeDoot", MODE_PRIVATE)
        editorSP = sharedPreferences.edit()

        if (sharedPreferences.getBoolean("isLogin",false)){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }else{
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}