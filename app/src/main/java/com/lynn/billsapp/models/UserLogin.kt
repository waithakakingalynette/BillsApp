package com.lynn.billsapp.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserLogin(
    @Expose @SerializedName(" first_name") val userName:String,
    val password:String,
    @Expose @SerializedName("confirm_password") val confirmPassword:String
)