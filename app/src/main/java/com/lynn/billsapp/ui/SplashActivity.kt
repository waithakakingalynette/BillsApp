package com.lynn.billsapp.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lynn.billsapp.databinding.ActivitySplashBinding
import com.lynn.billsapp.utils.Constants

class SplashActivity : AppCompatActivity() {
    lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        redirectUser()
    }
    override fun onResume() {
        super.onResume()
        binding.button.setOnClickListener {
            val intent=Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
    fun redirectUser(){
        val sharedPrefs=getSharedPreferences(Constants.PREFS,Context.MODE_PRIVATE)
        val userId=sharedPrefs.getString(Constants.USER_ID,Constants.EMPTY_STRING)
        if (userId.isNullOrBlank()){
            startActivity(Intent(this,Login::class.java))
        }
        else{
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }
}


