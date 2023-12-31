package com.lynn.billsapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lynn.billsapp.models.Bill
import com.lynn.billsapp.models.UpcomingBill
import com.lynn.billsapp.repository.BillsRepository
import kotlinx.coroutines.launch

class BillsViewModel:ViewModel() {
    val billsRepository= BillsRepository()
    val summaryLiveData=MutableLiveData<BillsSummary>()

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

    fun getUpcomingBillsByFrequency(freq:String):LiveData<List<UpcomingBill>>{
        return  billsRepository.getUpcomingBillsByFrequency(freq)
    }

    fun updateUpcomingBill(upcomingBill: UpcomingBill){
        viewModelScope.launch{
            billsRepository.updateUpcomingBill(upcomingBill)
        }
    }

    fun getPaidBills():LiveData<List<UpcomingBill>>{
        return billsRepository.getPaidBills()
    }

    fun getAllBill(): LiveData<List<Bill>> {
        return billsRepository.getAllBills()
    }

    fun fetchRemoteBills(){
        viewModelScope.launch {
            billsRepository.fetchRemoteBills()
            billsRepository.fetchRemoteUpcomingBills()

        }
    }

    fun getMonthlySummary(){
        viewModelScope.launch {
            summaryLiveData.postValue(billsRepository.getMonthlySummary().value)
        }
    }
}