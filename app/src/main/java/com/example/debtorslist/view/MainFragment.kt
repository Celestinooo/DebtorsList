package com.example.debtorslist.view

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodManager.HIDE_NOT_ALWAYS
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.debtorslist.R
import com.example.debtorslist.viewmodel.DebtorViewModel
import com.example.debtorslist.model.entity.Debtors
import com.example.debtorslist.model.entity.Debtors.Companion.DEBTOR_UNPAID
import com.example.debtorslist.model.entity.Debtors.Companion.DEBTOR_PAID
import com.example.debtorslist.view.adapter.OnDebtorClickListener
import com.example.debtorslist.view.adapter.DebtorAdapter
import com.example.debtorslist.databinding.FragmentMainBinding

class MainFragment : Fragment(), OnDebtorClickListener {
    private lateinit var fmb: FragmentMainBinding

    // Data source
    private val debtorsList: MutableList<Debtors> = mutableListOf()

    // Adapter
    private val debtorsAdapter: DebtorAdapter by lazy {
        DebtorAdapter(debtorsList, this)
    }

    // Navigation controller
    private val navController: NavController by lazy {
        findNavController()
    }

    // Communication constants
    companion object {
        const val EXTRA_DEBTOR = "EXTRA_DEBTOR"
        const val DEBTOR_FRAGMENT_REQUEST_KEY = "DEBTOR_FRAGMENT_REQUEST_KEY"
    }
    // ViewModel
    private val debtorViewModel : DebtorViewModel by viewModels {
        DebtorViewModel.DebtorViewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener(DEBTOR_FRAGMENT_REQUEST_KEY) { requestKey, bundle ->
            if (requestKey == DEBTOR_FRAGMENT_REQUEST_KEY) {
                val debtors = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    bundle.getParcelable(EXTRA_DEBTOR, Debtors::class.java)
                } else {
                    bundle.getParcelable(EXTRA_DEBTOR)
                }
                debtors?.also { receivedDebtor ->
                    debtorsList.indexOfFirst { it.name == receivedDebtor.name }.also { position ->
                        if (position != -1) {
                            debtorViewModel.editDebtor(receivedDebtor)
                            debtorsList[position] = receivedDebtor
                            debtorsAdapter.notifyItemChanged(position)
                        } else {
                            debtorViewModel.insertDebtor(receivedDebtor)
                            debtorsList.add(receivedDebtor)
                            debtorsAdapter.notifyItemInserted(debtorsList.lastIndex)
                        }
                    }
                }

                // Hiding soft keyboard
                (context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                    fmb.root.windowToken,
                    HIDE_NOT_ALWAYS
                )
            }
        }

        debtorViewModel.debtorsMld.observe(requireActivity()) { debtors ->
            debtorsList.clear()
            debtors.forEachIndexed { index, debtor ->
                debtorsList.add(debtor)
                debtorsAdapter.notifyItemChanged(index)
            }
        }

        debtorViewModel.getDebtors()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = getString(R.string.debtor_list)

        fmb = FragmentMainBinding.inflate(inflater, container, false).apply {
            debtorsRv.layoutManager = LinearLayoutManager(context)
            debtorsRv.adapter = debtorsAdapter

            addDebtorFab.setOnClickListener {
                navController.navigate(
                    MainFragmentDirections.actionMainFragmentToDebtorFragment(null, editDebtor = false)
                )
            }
        }

        return fmb.root
    }

    override fun onDebtorClick(position: Int) = navigateToDebtorFragment(position, false)

    override fun onRemoveDebtorMenuItemClick(position: Int) {
        debtorViewModel.removeDebtor(debtorsList[position])
        debtorsList.removeAt(position)
        debtorsAdapter.notifyItemRemoved(position)
    }

    override fun onEditDebtorMenuItemClick(position: Int) = navigateToDebtorFragment(position, true)

    override fun onDoneCheckBoxClick(position: Int, checked: Boolean) {
        debtorsList[position].apply {
            done = if (checked) DEBTOR_PAID else DEBTOR_UNPAID
            debtorViewModel.editDebtor(this)
        }
    }

    private fun navigateToDebtorFragment(position: Int, editDebtor: Boolean) {
        debtorsList[position].also {
            navController.navigate(
                MainFragmentDirections.actionMainFragmentToDebtorFragment(it, editDebtor)
            )
        }
    }
}