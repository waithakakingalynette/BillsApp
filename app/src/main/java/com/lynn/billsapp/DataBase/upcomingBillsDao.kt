package com.lynn.billsapp.DataBase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lynn.billsapp.models.UpcomingBill

@Dao
interface upcomingBillsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUpcomingBill(upcomingBill: UpcomingBill)

    @Query("SELECT * FROM UpcomingBills WHERE billId=:billId AND dueDate BETWEEN :startDate AND :endDate")
    fun queryExistingBill(billId:String,startDate:String,endDate:String):List<UpcomingBill>
}