package com.lynn.billsapp.models

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("first_name") val userName: String,
    val password: String,
    @SerializedName("confirm_password") val ConfirmPassword: String
)