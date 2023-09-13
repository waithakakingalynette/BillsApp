package com.lynn.billsapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lynn.billsapp.databinding.FragmentSummaryBinding

class summaryFragment : Fragment() {
    var binding:FragmentSummaryBinding?=null


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