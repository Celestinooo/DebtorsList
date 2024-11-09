package com.example.debtorslist.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.debtorslist.R
import com.example.debtorslist.databinding.TileDebtorBinding
import com.example.debtorslist.model.entity.Debtors
import com.example.debtorslist.model.entity.Debtors.Companion.DEBTOR_PAID

class DebtorAdapter(
    private val debtorsList: List<Debtors>,
    private val onDebtorClickListener: OnDebtorClickListener
) : RecyclerView.Adapter<DebtorAdapter.DebtorTileViewHolder>() {
    inner class DebtorTileViewHolder(tileDebtorBinding: TileDebtorBinding) :
        RecyclerView.ViewHolder(tileDebtorBinding.root) {
        val nameTv: TextView = tileDebtorBinding.nameTv
        val valueTv: TextView = tileDebtorBinding.valueTv
        val doneCb: CheckBox = tileDebtorBinding.doneCb

        init {
            tileDebtorBinding.apply {
                root.run {
                    setOnCreateContextMenuListener { menu, _, _ ->
                        (onDebtorClickListener as? Fragment)?.activity?.menuInflater?.inflate(
                            R.menu.context_menu_debtor,
                            menu
                        )
                        menu?.findItem(R.id.removeDebtorMi)?.setOnMenuItemClickListener {
                            onDebtorClickListener.onRemoveDebtorMenuItemClick(adapterPosition)
                            true
                        }
                        menu?.findItem(R.id.editDebtorMi)?.setOnMenuItemClickListener {
                            onDebtorClickListener.onEditDebtorMenuItemClick(adapterPosition)
                            true
                        }
                    }
                    setOnClickListener {
                        onDebtorClickListener.onDebtorClick(adapterPosition)
                    }
                }
                doneCb.run {
                    setOnClickListener {
                        onDebtorClickListener.onDoneCheckBoxClick(adapterPosition, isChecked)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TileDebtorBinding.inflate(
        LayoutInflater.from(parent.context), parent, false
    ).run { DebtorTileViewHolder(this) }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: DebtorTileViewHolder, position: Int) {
        debtorsList[position].let { debtor ->
            with(holder) {
                nameTv.text = debtor.name
                valueTv.text = "R$ ${debtor.value}"
                doneCb.isChecked = debtor.done == DEBTOR_PAID
            }
        }
    }

    override fun getItemCount() = debtorsList.size
}