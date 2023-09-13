package com.lynn.billsapp.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lynn.billsapp.models.Bill
import com.lynn.billsapp.repository.BillsRepository
import kotlinx.coroutines.launch

class BillsViewModel:ViewModel() {
    val billsRepository= BillsRepository()

    fun saveBill(bill: Bill){
        viewModelScope.launch {
            billsRepository.saveBill(bill)
        }
    }
    fun createRecurringBills(){
        viewModelScope.launch {
            billsRepository.createRecurringMonthlyBills()
            billsRepository.createRecurringWeeklyBills()
        }
    }
}