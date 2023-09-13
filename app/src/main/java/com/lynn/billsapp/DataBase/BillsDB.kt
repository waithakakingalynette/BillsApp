package com.lynn.billsapp.DataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lynn.billsapp.models.Bill
import com.lynn.billsapp.models.UpcomingBill


@Database(entities = [Bill::class,UpcomingBill::class], version = 1)

abstract class BillsDB :RoomDatabase(){
    abstract fun billsDao():BillsDao
    abstract fun upcomingBillsDao():upcomingBillsDao

    companion object{
        var database:BillsDB?=null
        fun getDataBase(context: Context):BillsDB{
            if (database==null){
                database=Room
                    .databaseBuilder(context,BillsDB::class.java,"BillsDB")
                    .fallbackToDestructiveMigration()
                    .build()

            }
            return database as BillsDB
        }

    }

}