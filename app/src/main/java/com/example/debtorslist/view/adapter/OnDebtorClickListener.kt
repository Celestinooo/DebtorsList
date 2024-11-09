package com.example.debtorslist.view.adapter

interface OnDebtorClickListener {
    fun onDebtorClick(position: Int)
    fun onRemoveDebtorMenuItemClick(position: Int)
    fun onEditDebtorMenuItemClick(position: Int)
    fun onDoneCheckBoxClick(position: Int, checked: Boolean)
}