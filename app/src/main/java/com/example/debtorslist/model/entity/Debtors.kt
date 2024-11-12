package com.example.debtorslist.model.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Debtors (
    @PrimaryKey
    var name: String = "",
    var value: Double = 0.0,
    var description: String = "",
    var done: Int = DEBTOR_UNPAID
): Parcelable {
    companion object {
        const val DEBTOR_PAID = 1
        const val DEBTOR_UNPAID = 0
    }
}