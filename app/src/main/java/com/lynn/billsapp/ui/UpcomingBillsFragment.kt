package com.lynn.billsapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.lynn.billsapp.databinding.FragmentUpcomingBillsBinding
import com.lynn.billsapp.models.UpcomingBill
import com.lynn.billsapp.utils.Constants
import com.lynn.billsapp.viewModels.BillsViewModel


class UpcomingBillsFragment : Fragment(),OnClickBill {
    private var binding: FragmentUpcomingBillsBinding?=null
    val billsViewModel: BillsViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentUpcomingBillsBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        return binding?.root
    }

    override fun onResume() {
        super.onResume()
        getUpcomingBills()

    }
    fun getUpcomingBills(){
        billsViewModel.getUpcomingBillsByFrequency(Constants.WEEKLY).observe(this){ weeklyBills->
            val adapter=UpcomingBillsAdapter(weeklyBills,this)
            binding?.rvWeekly?.layoutManager=LinearLayoutManager(requireContext())
            binding?.rvWeekly?.adapter=adapter

        }
        billsViewModel.getUpcomingBillsByFrequency(Constants.MONTHLY).observe(this){monthlyBills->
            val adapter=UpcomingBillsAdapter(monthlyBills,this)
            binding?.rvMonthly?.layoutManager=LinearLayoutManager(requireContext())
            binding?.rvMonthly?.adapter=adapter

        }
        billsViewModel.getUpcomingBillsByFrequency(Constants.YEARLY).observe(this){annualBills->
            val adapter=UpcomingBillsAdapter(annualBills,this)
            binding?.rvAnnualy?.layoutManager=LinearLayoutManager(requireContext())
            binding?.rvAnnualy?.adapter=adapter

        }
    }
    override fun onDestroy(){
        super.onDestroy()
        binding=null
    }

    override fun onCheckBoxMarked(upcomingBill: UpcomingBill) {
        upcomingBill.paid = upcomingBill.paid
        upcomingBill.paid = true
        upcomingBill.paid = false
        billsViewModel.updateUpcomingBill(upcomingBill)
    }
}