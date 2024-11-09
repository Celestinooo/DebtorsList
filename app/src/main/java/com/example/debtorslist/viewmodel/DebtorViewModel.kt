package com.example.debtorslist.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.room.Room
import com.example.debtorslist.model.database.DebtorsDatabase
import com.example.debtorslist.model.database.DebtorsDatabase.Companion.DEBTORS_DATABASE
import com.example.debtorslist.model.entity.Debtors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Suppress("UNCHECKED_CAST")
class DebtorViewModel(application: Application) : ViewModel() {

    private val debtorDaoImpl = Room.databaseBuilder(
        application.applicationContext,
        DebtorsDatabase::class.java,
        DEBTORS_DATABASE
    ).build().getDebtorsDao()

    val debtorsMld = MutableLiveData<List<Debtors>>()

    fun insertDebtor(debtors: Debtors) {
        CoroutineScope(Dispatchers.IO).launch {
            debtorDaoImpl.createDebtor(debtors)
        }
    }

    fun getDebtors() {
        CoroutineScope(Dispatchers.IO).launch {
            val debtors = debtorDaoImpl.retrieveDebtors()
            debtorsMld.postValue(debtors)
        }
    }

    fun editDebtor(debtors: Debtors) {
        CoroutineScope(Dispatchers.IO).launch {
            debtorDaoImpl.updateDebtors(debtors)
        }
    }

    fun removeDebtor(debtors: Debtors) {
        CoroutineScope(Dispatchers.IO).launch {
            debtorDaoImpl.deleteDebtors(debtors)
        }
    }

    companion object {
        val DebtorViewModelFactory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T = DebtorViewModel(
                checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])) as T
        }
    }

}