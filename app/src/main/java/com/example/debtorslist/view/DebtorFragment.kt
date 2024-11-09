package com.example.debtorslist.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.debtorslist.model.entity.Debtors
import com.example.debtorslist.model.entity.Debtors.Companion.DEBTOR_UNPAID
import com.example.debtorslist.model.entity.Debtors.Companion.DEBTOR_PAID
import com.example.debtorslist.R
import com.example.debtorslist.databinding.FragmentDebtorBinding
import com.example.debtorslist.view.MainFragment.Companion.EXTRA_DEBTOR
import com.example.debtorslist.view.MainFragment.Companion.DEBTOR_FRAGMENT_REQUEST_KEY

class DebtorFragment : Fragment() {
    private lateinit var ftb: FragmentDebtorBinding
    private val navigationArgs: DebtorFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle =
            getString(R.string.debtor_details)

        ftb = FragmentDebtorBinding.inflate(inflater, container, false)

        val receivedDebtor = navigationArgs.debtor
        receivedDebtor?.also { debtor ->
            with(ftb) {
                nameEt.setText(debtor.name)
                descriptionEt.setText(debtor.description)
                valueEt.setText(debtor.value.toString())
                doneCb.isChecked = debtor.done == DEBTOR_PAID
                navigationArgs.editDebtor.also { editDebtor ->
                    nameEt.isEnabled = editDebtor
                    valueEt.isEnabled = editDebtor
                    descriptionEt.isEnabled = editDebtor
                    doneCb.isEnabled = editDebtor
                    saveBt.visibility = if (editDebtor) VISIBLE else GONE
                }
            }
        }

        ftb.run {
            saveBt.setOnClickListener {
                setFragmentResult(DEBTOR_FRAGMENT_REQUEST_KEY, Bundle().apply {
                    putParcelable(
                        EXTRA_DEBTOR, Debtors(
                            receivedDebtor?.time ?: System.currentTimeMillis(),
                            nameEt.text.toString(),
                            valueEt.text.toString().toDouble(),
                            descriptionEt.text.toString(),
                            if (doneCb.isChecked) DEBTOR_PAID else DEBTOR_UNPAID
                        )
                    )
                })
                findNavController().navigateUp()
            }
        }

        return ftb.root
    }
}