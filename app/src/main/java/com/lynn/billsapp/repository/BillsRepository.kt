package com.lynn.billsapp.repository


import com.lynn.billsapp.BillsApp
import com.lynn.billsapp.DataBase.BillsDB
import com.lynn.billsapp.models.Bill
import com.lynn.billsapp.models.UpcomingBill
import com.lynn.billsapp.utils.Constants
import com.lynn.billsapp.utils.dateTimeUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

class BillsRepository {
    val database= BillsDB.getDataBase(BillsApp.appContext)
    val BillsDao=database.billsDao()
    val upcomingBillsDao=database.upcomingBillsDao()

    suspend fun saveBill(bill: Bill){
        withContext(Dispatchers.IO){
            BillsDao.insertBill(bill)
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
                        paid = false
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
                        billId=bill.billId,
                        name = bill.name,
                        amount = bill.amount,
                        frequency = bill.frequency,
                        dueDate = dateTimeUtils.getDateOfWeekDay(bill.dueDate),
                        userId = bill.userId,
                        paid = false
                    )
                    upcomingBillsDao.insertUpcomingBill(newWeeklyBills)
                }
            }


        }
    }
}