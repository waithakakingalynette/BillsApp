package com.lynn.billsapp.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RegisterRequest(
   @Expose @SerializedName("first_name") val firstName: String,
   @Expose @SerializedName("last_name") val lastName: String,
   @Expose @SerializedName("email")val email: String,
   @Expose @SerializedName("password")val password: String,
   @Expose @SerializedName("phone_number") val phoneNumber: String,
   @Expose @SerializedName("confirm_password") val ConfirmPassword: String,

    )
