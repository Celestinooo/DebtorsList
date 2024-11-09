package com.example.debtorslist.model.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.debtorslist.model.entity.Debtors

@Dao
interface DebtorsDao {
    companion object {
        const val DEBTORS_TABLE = "debtors"
    }
    @Insert
    fun createDebtor(debtors: Debtors)
    @Query("SELECT * FROM $DEBTORS_TABLE")
    fun retrieveDebtors(): List<Debtors>
    @Update
    fun updateDebtors(debtors: Debtors)
    @Delete
    fun deleteDebtors(debtors: Debtors)
}