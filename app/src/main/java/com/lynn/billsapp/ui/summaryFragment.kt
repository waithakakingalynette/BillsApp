package com.lynn.billsapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lynn.billsapp.R
import com.lynn.billsapp.databinding.FragmentSummaryBinding
import com.lynn.billsapp.viewModels.BillsViewModel

class summaryFragment : Fragment() {
    var binding:FragmentSummaryBinding?=null
    private lateinit var billsViewModel: BillsViewModel
    private lateinit var adapter: BillsAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        billsViewModel=viewModelProvider(re)
        adapter= BillsAdapter(requireContext(), R.layout.item_bill, mutableListOf())
        binding?.listViewModels?.adapter=adapter
        billsViewModel.getAllBills().observe()

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentSummaryBinding.inflate(layoutInflater,container,false)
        return binding?.root
    }

    override fun onResume() {
        super.onResume()
        binding?.fbnButtonAdd?.setOnClickListener {
            startActivity(Intent(requireContext(),BillsActivity::class.java))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding=null
    }


}