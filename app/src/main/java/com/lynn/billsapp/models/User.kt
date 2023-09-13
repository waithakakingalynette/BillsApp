package com.lynn.billsapp.models

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("phone_number") val phoneNumber:String,
    @SerializedName("first_name") val firstName:String,
    @SerializedName( "user_id") val userId:String,
    @SerializedName("last_name") val lastName:String,
    val email:String
)