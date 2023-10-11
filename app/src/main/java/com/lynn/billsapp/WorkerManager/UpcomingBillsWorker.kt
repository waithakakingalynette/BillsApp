package com.lynn.billsapp.WorkerManager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.lynn.billsapp.repository.BillsRepository

class UpcomingBillsWorker(context: Context,workerParams: WorkerParameters):CoroutineWorker(context,workerParams) {
    val billRepo=BillsRepository()
    override suspend fun doWork(): Result {
        billRepo.createRecurringWeeklyBills()
        billRepo.createRecurringMonthlyBills()
        billRepo.createRecurringAnnuallyBills()
        return Result.success()
    }

}

//package com.example.assessment.workmanager
//
//import android.content.Context
//import android.os.Build
//import androidx.annotation.RequiresApi
//import androidx.work.CoroutineWorker
//import androidx.work.WorkerParameters
//import com.example.assessment.repository.BillsRepository
//
//class UpcomingBillsWorker(context: Context,workerParams: WorkerParameters):
//    CoroutineWorker(context, workerParams) {
//    val billsRepo = BillsRepository()
//    @RequiresApi(Build.VERSION_CODES.O)
//    override suspend fun doWork(): Result {
//        billsRepo.createRecurringWeeklyBills()
//        billsRepo.createRecurringAnnuallyBills()
//        billsRepo.createRecurringQuarterlyBills()
//        billsRepo.createRecurringMonthlyBills()
//
//        return Result.success()
//    }
//}