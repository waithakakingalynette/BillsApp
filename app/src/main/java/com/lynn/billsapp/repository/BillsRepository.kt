package com.lynn.billsapp.repository


import android.content.Context
import androidx.lifecycle.LiveData
import com.lynn.billsapp.BillsApp
import com.lynn.billsapp.DataBase.BillsDB
import com.lynn.billsapp.api.ApiClient
import com.lynn.billsapp.api.ApiInterface
import com.lynn.billsapp.models.Bill
import com.lynn.billsapp.models.UpcomingBill
import com.lynn.billsapp.utils.Constants
import com.lynn.billsapp.utils.dateTimeUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

class BillsRepository {
    private val database= BillsDB.getDataBase(BillsApp.appContext)
    val BillsDao=database.billsDao()
    val upcomingBillsDao=database.upcomingBillsDao()
    val apiClient=ApiClient.buildClient(ApiInterface::class.java)

    fun getAllBills():LiveData<List<Bill>>{
        return BillsDao.getAllBills()
    }

    suspend fun saveBill(bill: Bill){
        withContext(Dispatchers.IO){
            BillsDao.saveBill(bill)
        }
    }
    suspend fun insertUpcomingBill(upcomingBill: UpcomingBill){
        withContext(Dispatchers.IO){
            upcomingBillsDao.insertUpcomingBill(upcomingBill)
        }
    }
    suspend fun createRecurringMonthlyBills(){
        withContext(Dispatchers.IO){
            val monthlyBills=BillsDao.getRecurringBills(Constants.MONTHLY)
            val startDate= dateTimeUtils.getFirstDayOfMonth()
            val endDate= dateTimeUtils.getLastDayOfMonth()
            val year=dateTimeUtils.getCurrentYear()
            val month=dateTimeUtils.getCurrentMonth()
            monthlyBills.forEach { bill->
                val existing=upcomingBillsDao.queryExistingBill(bill.billId,startDate, endDate)
                if (existing.isEmpty()){
                    val newUpcomingBill=UpcomingBill(
                        upcomingBillId = UUID.randomUUID().toString(),
                        billId=bill.billId,
                        name = bill.name,
                        amount = bill.amount,
                        frequency = bill.frequency,
                        dueDate = "${bill.dueDate}/$month/$year",
                        userId = bill.userId,
                        paid = false,
                        synced = Boolean
                    )
                    upcomingBillsDao.insertUpcomingBill(newUpcomingBill)

                }
            }
        }
    }
    suspend fun createRecurringWeeklyBills(){
        withContext(Dispatchers.IO){
            val weeklyBills=BillsDao.getRecurringBills(Constants.WEEKLY)
            val startDate=dateTimeUtils.getFirstDateOfWeek()
            val endDate=dateTimeUtils.getLastDateOfWeek()
            weeklyBills.forEach { bill->
                val existingBill=upcomingBillsDao.queryExistingBill(bill.billId,startDate, endDate)
                if (existingBill.isEmpty()){
                    val newWeeklyBills=UpcomingBill(
                        upcomingBillId = UUID.randomUUID().toString(),
                        billId =bill.billId,
                        name = bill.name,
                        amount = bill.amount,
                        frequency = bill.frequency,
                        dueDate = dateTimeUtils.getDateOfWeekDay(bill.dueDate),
                        userId = bill.userId,
                        paid = false,
                        synced = Boolean
                    )
                    upcomingBillsDao.insertUpcomingBill(newWeeklyBills)
                }
            }


        }
    }
    fun getUpcomingBillsByFrequency(freq:String):LiveData<List<UpcomingBill>>{
    return upcomingBillsDao.getUpcomingBillsByFrequency(freq,false)
    }

    suspend fun createRecurringAnnuallyBills() {
        withContext(Dispatchers.IO) {
            val annualBills = BillsDao.getRecurringBills(Constants.YEARLY)
            val currentYear = dateTimeUtils.getCurrentYear()
            val startDate = "$currentYear-01-01"
            val endDate = "$currentYear-12-31"
            annualBills.forEach { bill ->
                val existingBill =
                    upcomingBillsDao.queryExistingBill(bill.billId, startDate, endDate)
                if (existingBill.isEmpty()) {
                    val newAnnualBills = UpcomingBill(
                        upcomingBillId = UUID.randomUUID().toString(),
                        billId = bill.billId,
                        name = bill.name,
                        amount = bill.amount,
                        frequency = bill.frequency,
                        dueDate = "$currentYear-${bill.dueDate}",
                        userId = bill.userId,
                        paid = false,
                        synced = Boolean
                    )
                    upcomingBillsDao.insertUpcomingBill(newAnnualBills)
                }
            }


        }
    }

    suspend fun updateUpcomingBill(upcomingBill: UpcomingBill) {
        withContext(Dispatchers.IO) {
            upcomingBillsDao.updateUpcomingBill(upcomingBill)

        }

    }
    fun getPaidBills(): LiveData<List<UpcomingBill>> {
        return upcomingBillsDao.getPaidBills()
    }
    suspend fun getSyncBills(){
        withContext(Dispatchers.IO){
            var token=getAuthToken()
            val UpcomingBill= com.lynn.billsapp.DataBase.BillDao.getUnSyncedBills()
            unsyncedBills.forEach{bill ->
                val response=apiClient.postBill(bill)
                bill.synced=true
                BillsDao.saveBill(bill)

            }
        }
    }

    fun getAuthToken():String{
        val prefs=BillsApp.appContext.getSharedPreferences(Constants.PREFS,Context.MODE_PRIVATE)
        var token=prefs.getString(Constants.ACCESS_TOKEN,Context.MODE_PRIVATE)
        token="Bearer $token"
        return  token
    }
suspend fun syncUpcomingBills(){
    withContext(Dispatchers.IO){
        var token=getAuthToken()
        upcomingBillsDao.getUnsyncedUpcomingBills().forEach {upcomingBill ->
            val response=apiClient.postUpcomingBill(upcomingBill)
            if (response.isSuccessful){
                upcomingBill.synced=true
                upcomingBillsDao.updateUpcomingBill(upcomingBill)
            }
        }
    }
}
     suspend fun fetchUpcomingBills(){
        withContext(Dispatchers.IO){
            val response=apiClient.fetchRemoteBills(getAuthToken())
            if (response.isSuccessful){
                response.body()?.forEach { bill->
                bill.synced=true
                    BillsDao.saveBill(bill) }
            }
        }
    }

     suspend fun fetchRemoteUpcomingBills(){
        withContext(Dispatchers.IO){
            val response=apiClient.fetchRemoteUpcomingBills(getAuthToken())
            if (response.isSuccessful){
                response.body()?.forEach { upcomingBill->
                    upcomingBill.synced=true
                    upcomingBillsDao.insertUpcomingBill(upcomingBill) }
            }
        }
    }
}
