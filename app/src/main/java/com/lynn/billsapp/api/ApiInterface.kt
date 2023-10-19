package com.lynn.billsapp.api


import com.lynn.billsapp.models.Bill
import com.lynn.billsapp.models.LoginRequest
import com.lynn.billsapp.models.LoginResponse
import com.lynn.billsapp.models.RegisterRequest
import com.lynn.billsapp.models.RegisterResponse
import com.lynn.billsapp.models.UpcomingBill
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiInterface {
    @POST("/users/register")
    suspend fun registerUser(@Body registerRequest: RegisterRequest):Response<RegisterResponse>
    @POST("/users/login")
    suspend fun loginUser(@Body loginRequest: LoginRequest):Response<LoginResponse>
    @POST("/bills")
    suspend fun postBill(@Header("Authorization")token: String, @Body bill: Bill):Response<Bill>

    @POST("/upcoming-bills")
    suspend fun postUpcomingBill(@Header("Authorization")token: String,@Body upcomingBill: UpcomingBill):Response<UpcomingBill>

    @GET("/bills")
    suspend fun fetchRemoteBills(@Header("Authorization")token: String):Response<List<Bill>>

    @GET("/upcoming-bills")
    suspend fun fetchRemoteUpcomingBills(@Header("Authorization") token: String):Response<List<UpcomingBill>>
}
