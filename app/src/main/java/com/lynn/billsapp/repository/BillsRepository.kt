package com.lynn.billsapp.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lynn.billsapp.DataBase.BillsDB
import com.lynn.billsapp.WorkerManager.BillsApp
import com.lynn.billsapp.api.ApiClient
import com.lynn.billsapp.api.ApiInterface
import com.lynn.billsapp.models.Bill
import com.lynn.billsapp.models.UpcomingBill
import com.lynn.billsapp.utils.Constants
import com.lynn.billsapp.utils.dateTimeUtils
import com.lynn.billsapp.viewModels.BillsSummary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

class BillsRepository {
    val database = BillsDB.getDataBase(BillsApp.appContext)
    private val billsDao = database.billsDao()
    private val upcomingBillsDao = database.upcomingBillsDao()
    val apiClient = ApiClient.buildClient(ApiInterface::class.java)

    fun getAllBills(): LiveData<List<Bill>> {
        return billsDao.getAllBills()
    }

    suspend fun saveBill(bill: Bill) {
        withContext(Dispatchers.IO) {
            billsDao.saveBill(bill)
        }
    }

    suspend fun insertUpcomingBill(upcomingBill: UpcomingBill) {
        withContext(Dispatchers.IO) {
            upcomingBillsDao.insertUpcomingBill(upcomingBill)
        }
    }

    suspend fun createRecurringMonthlyBills() {
        withContext(Dispatchers.IO) {
            val monthlyBills = billsDao.getRecurringBills(Constants.MONTHLY)
            val startDate = dateTimeUtils.getFirstDayOfMonth()
            val endDate = dateTimeUtils.getLastDayOfMonth()

            monthlyBills.forEach { bill ->
                val existing = upcomingBillsDao.queryExistingBill(bill.billId, startDate, endDate)
                if (existing.isEmpty()) {
                    val newUpcomingBill = UpcomingBill(
                        upcomingBillId = UUID.randomUUID().toString(),
                        name = bill.name,
                        amount = bill.amount,
                        frequency = bill.frequency,
                        dueDate = dateTimeUtils.createDateFromDay(bill.dueDate),
                        userId = bill.userId,
                        paid = false,
                        billId = bill.billId,
                        synced = false
                    )
                    upcomingBillsDao.insertUpcomingBill(newUpcomingBill)
                }
            }
        }
    }

    suspend fun createRecurringWeeklyBills() {
        withContext(Dispatchers.IO) {
            val weeklyBills = billsDao.getRecurringBills(Constants.WEEKLY)
            val startDate = dateTimeUtils.getFirstDateOfWeek()
            val endDate = dateTimeUtils.getLastDateOfWeek()
            weeklyBills.forEach { bill ->
                val existingBill = upcomingBillsDao.queryExistingBill(bill.billId, startDate, endDate)
                if (existingBill.isEmpty()) {
                    val newWeeklyBills = UpcomingBill(
                        upcomingBillId = UUID.randomUUID().toString(),
                        name = bill.name,
                        amount = bill.amount,
                        frequency = bill.frequency,
                        dueDate = dateTimeUtils.getDateOfWeekDay(bill.dueDate),
                        userId = bill.userId,
                        paid = false,
                        billId = bill.billId,
                        synced = false
                    )
                    upcomingBillsDao.insertUpcomingBill(newWeeklyBills)
                }
            }
        }
    }

    fun getUpcomingBillsByFrequency(freq: String): LiveData<List<UpcomingBill>> {
        return upcomingBillsDao.getUpcomingBillsByFrequency(freq, false)
    }

    suspend fun createRecurringAnnuallyBills() {
        withContext(Dispatchers.IO) {
            val annualBills = billsDao.getRecurringBills(Constants.YEARLY)
            val currentYear = dateTimeUtils.getCurrentYear()
            val startDate = "$currentYear-01-01"
            val endDate = "$currentYear-12-31"
            annualBills.forEach { bill ->
                val existingBill = upcomingBillsDao.queryExistingBill(bill.billId, startDate, endDate)
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
                        synced = false
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
    fun getAccessToken(): String {
        val sharedPreferences = BillsApp.appContext.getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE)
        var token = sharedPreferences.getString(Constants.ACCESS_TOKEN, "") ?: ""
        token = "Bearer $token"
        return token
    }

    suspend fun syncBills() {
        val accessToken = getAccessToken()
        withContext(Dispatchers.IO) {
            val unsyncedBills = billsDao.getUnSyncedBills()
            unsyncedBills.forEach { bill ->
                val response = apiClient.postBill(accessToken, bill)
                if (response.isSuccessful) {
                    bill.synced = true
                    billsDao.saveBill(bill)
                }
            }
        }
    }

    suspend fun syncUpcomingBills() {
        val accessToken = getAccessToken()
        withContext(Dispatchers.IO) {
            upcomingBillsDao.getUnsyncedUpcomingBills().forEach { upcomingBill ->
                val response = apiClient.postUpcomingBill(accessToken, upcomingBill)
                if (response.isSuccessful) {
                    upcomingBill.synced = true
                    upcomingBillsDao.updateUpcomingBill(upcomingBill)
                }
            }
        }
    }

    suspend fun fetchRemoteBills() {
        withContext(Dispatchers.IO) {
            val token = getAccessToken()
            val response = apiClient.fetchRemoteBills(token)
            if (response.isSuccessful) {
                response.body()?.forEach { bill ->
                    bill.synced = true
                    billsDao.saveBill(bill)
                }
            }
        }
    }

    suspend fun fetchRemoteUpcomingBills() {
        withContext(Dispatchers.IO) {
            val token = getAccessToken()
            val response = apiClient.fetchRemoteUpcomingBills(token)
            if (response.isSuccessful) {
                response.body()?.forEach { upcomingBill ->
                    upcomingBill.synced = true
                    upcomingBillsDao.insertUpcomingBill(upcomingBill)
                }
            }
        }
    }

    suspend fun getMonthlySummary(): LiveData<BillsSummary> {
        return withContext(Dispatchers.IO) {
            val startDate = dateTimeUtils.getFirstDayOfMonth()
            val endDate = dateTimeUtils.getLastDayOfMonth()
            val today = dateTimeUtils.getDateToday()
            val paid = upcomingBillsDao.getPaidMonthlyBillsSum(startDate, endDate)
            val upcoming = upcomingBillsDao.getUpcomingBillsThisMonth(startDate, endDate, today)
            val total = upcomingBillsDao.getTotalMonthlyBills(startDate, endDate)
            val overdue = upcomingBillsDao.getOverdueBillsThisMonth(startDate, endDate, today)
            val summary = BillsSummary(paid = paid, overdue = overdue, upcoming = upcoming, total = total)
            MutableLiveData(summary)
        }
    }
}
