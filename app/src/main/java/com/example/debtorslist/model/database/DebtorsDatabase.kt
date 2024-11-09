package com.example.debtorslist.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.debtorslist.model.dao.DebtorsDao
import com.example.debtorslist.model.entity.Debtors

@Database(entities = [Debtors::class], version = 1)
abstract class DebtorsDatabase: RoomDatabase() {
    companion object {
        const val DEBTORS_DATABASE = "debtorsListDatabase"
    }
    abstract fun getDebtorsDao(): DebtorsDao
}