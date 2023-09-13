package com.lynn.billsapp.api


import com.lynn.billsapp.models.LoginRequest
import com.lynn.billsapp.models.LoginResponse
import com.lynn.billsapp.models.RegisterRequest
import com.lynn.billsapp.models.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiInterface {
    @POST("/users/register")
    suspend fun registerUser(@Body registerRequest: RegisterRequest):Response<RegisterResponse>
    @POST("/users/login")
    suspend fun loginUser(@Body loginRequest: LoginRequest):Response<LoginResponse>
}