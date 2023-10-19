package com.lynn.billsapp.DataBase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lynn.billsapp.models.Bill

@Dao
interface BillDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveBill(bill: Bill)
    @Query("SELECT * FROM Bills ORDER BY dueDate")
    fun getAllBills(): LiveData<List<Bill>>
    @Query("SELECT * FROM BILLS WHERE frequency=:freq")
    fun getRecurringBills(freq:String):List<Bill>
    @Query("SELECT  * FROM Bills WHERE synced = 0")
    fun getUnSyncedBills():List<Bill>
//    @Query("SELECT * FROM Bills WHERE  billId=:billId")
//    fun getBillById(billId:Int):LiveData<Bill>
//
//
//    @Delete
//    suspend fun deleteBillById(bill: Bill)
}