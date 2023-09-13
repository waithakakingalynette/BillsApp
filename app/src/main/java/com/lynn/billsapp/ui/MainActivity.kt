package com.lynn.billsapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lynn.billsapp.R
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.lynn.billsapp.databinding.ActivityMainBinding
import com.lynn.billsapp.models.RegisterRequest
import com.lynn.billsapp.utils.Constants
import com.lynn.billsapp.viewModels.UserViewModel

import javax.xml.transform.sax.TemplatesHandler

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    val userViewModel: UserViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding =ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
    override fun onResume() {
        super.onResume()
        setContentView(binding.root)

        binding.tvLogIn.setOnClickListener {
            startActivity(Intent(this,Login::class.java))
        }

        binding.btnSignUp.setOnClickListener {
            validateRegistration()
            startActivity(Intent(this,Login::class.java))
            clearErrors()
            redirectUser()
            binding.pbprogressBar.visibility=View.GONE
            userViewModel.registerLiveData.observe(this, Observer { registerResponse->
                Toast.makeText(this, registerResponse.message, Toast.LENGTH_LONG).show()
            })
            userViewModel.errorLiveData.observe(this, Observer { error->
                Toast.makeText(this, error, Toast.LENGTH_LONG).show()
            })
        }
    }
    fun validateRegistration() {
        val firstName= binding.etUserName.text.toString()
        val lastname=binding.etlastname.text.toString()
        val phoneNumber =binding.etphonenumber.text.toString()
        val email=binding.etemail .text.toString()
        val password =binding.etpassword .text.toString()
        val ConfirmPassword=binding.etConfirmPassword.text.toString()
        var error = false
        if (firstName.isBlank()) {
            binding.tilUserName.error = "first Name required"
            error=true
        }
        if (lastname.isBlank()) {
            binding.tillastname.error = "last Name required"
            error=true
        }
        if (phoneNumber.isBlank()) {
            binding.tilphonenumber.error = "Phone number required"
            error=true
        }
        if (email.isBlank()) {
            binding.tilemail.error = "Email required"
            error=true
        }
        if (password.isBlank()) {
            binding.tilpassword.error = "Password required"
            error=true
        }
        if (ConfirmPassword.isBlank()) {
            binding.tilConfirmPassword.error = "confirm Password required"
            error=true
        }
        if (!error){
            val registerRequest= RegisterRequest(
                firstName=firstName,
                lastName = lastname,
                email=email,
                password=password,
                phoneNumber=phoneNumber,
                ConfirmPassword=ConfirmPassword
            )
            binding.pbprogressBar.visibility=View.VISIBLE
            userViewModel.registerUser(registerRequest)
        }
    }
    fun clearErrors(){
        binding.tillastname.error=null
        binding.tilUserName.error=null
        binding.tilpassword.error=null
        binding.tilemail.error=null
        binding.tilConfirmPassword.error=null
    }
    fun redirectUser(){
        val preferences=getSharedPreferences(Constants.PREFS,Context.MODE_PRIVATE)
        val  userId=preferences.getString(Constants.USER_ID,Constants.EMPTY_STRING)!!
        if (userId.isNotBlank()) {
            startActivity(Intent(this, HomeActivity::class.java))
        }

    }
}