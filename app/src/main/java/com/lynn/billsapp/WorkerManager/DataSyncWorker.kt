package com.lynn.billsapp.WorkerManager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.lynn.billsapp.repository.BillsRepository

class DataSyncWorker(context: Context,workerParams:WorkerParameters):CoroutineWorker(context,workerParams) {

    val billsRepository=BillsRepository()
    override suspend fun doWork(): Result {
        billsRepository.getSyncedBills()
        billsRepository.syncUpcomingBills()
        return Result.success()
    }
}