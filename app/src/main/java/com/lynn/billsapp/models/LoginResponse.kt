package com.lynn.billsapp.models

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    val message:String,
    @SerializedName("access_token")val accessToken: String,
    @SerializedName("user_id") val userId: String
)
