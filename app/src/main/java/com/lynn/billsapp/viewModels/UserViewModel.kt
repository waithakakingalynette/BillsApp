package com.lynn.billsapp.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lynn.billsapp.models.RegisterRequest
import com.lynn.billsapp.models.RegisterResponse
import com.lynn.billsapp.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel:ViewModel() {
    val registerLiveData=MutableLiveData<RegisterResponse>()
    val errorLiveData=MutableLiveData<String>()
    val userRepository= UserRepository()
    fun registerUser(registerRequest: RegisterRequest){
        viewModelScope.launch {
            val response=userRepository.registerUser(registerRequest)
            registerLiveData.postValue(response.body())
            if (response.isSuccessful){
                registerLiveData.postValue(response.body())
            }
            else{
                errorLiveData.postValue(response.errorBody()?.string())
            }
        }
    }
}