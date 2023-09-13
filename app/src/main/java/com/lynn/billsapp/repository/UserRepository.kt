package com.lynn.billsapp.repository

import com.lynn.billsapp.api.ApiClient
import com.lynn.billsapp.api.ApiInterface
import com.lynn.billsapp.models.RegisterRequest
import com.lynn.billsapp.models.RegisterResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class UserRepository {
    var apiClient= ApiClient.buildClient(ApiInterface::class.java)
    suspend fun registerUser(registerRequest: RegisterRequest): Response<RegisterResponse> {
        return  withContext(Dispatchers.IO){
            apiClient.registerUser(registerRequest)
        }
    }
}