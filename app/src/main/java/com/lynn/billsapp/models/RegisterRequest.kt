package com.lynn.billsapp.models

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    val email: String,
    val password: String,
    @SerializedName("phone_number") val phoneNumber: String,
    @SerializedName("confirm_password") val ConfirmPassword: String,

    )
