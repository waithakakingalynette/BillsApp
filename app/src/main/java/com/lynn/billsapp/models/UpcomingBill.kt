package com.lynn.billsapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("UpcomingBills")
data class UpcomingBill(
    @PrimaryKey val upcomingBillId:String,
    var billId:String,
    var name:String,
    var amount:Double,
    var frequency:String,
    var dueDate:String,
    var userId:String,
    var paid:Boolean,
)