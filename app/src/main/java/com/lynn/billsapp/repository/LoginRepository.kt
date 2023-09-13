package com.lynn.billsapp.repository

import com.lynn.billsapp.api.ApiClient
import com.lynn.billsapp.api.ApiInterface
import com.lynn.billsapp.models.LoginRequest
import com.lynn.billsapp.models.LoginResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class LoginRepository {
    val apiClient= ApiClient.buildClient(ApiInterface::class.java)
    suspend fun loginUser(loginRequest: LoginRequest):Response<LoginResponse>{
        return withContext(Dispatchers.IO){
            apiClient.loginUser(loginRequest)
        }
    }
}