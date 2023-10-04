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