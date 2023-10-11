package com.lynn.billsapp.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class User(
    @Expose @SerializedName("phone_number") val phoneNumber:String,
    @Expose @SerializedName("first_name") val firstName:String,
    @Expose @SerializedName( "user_id") val userId:String,
    @Expose @SerializedName("last_name") val lastName:String,
    val email:String
)