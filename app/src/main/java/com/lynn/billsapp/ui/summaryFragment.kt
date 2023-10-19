package com.lynn.billsapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.graphics.vector.SolidFill
import com.lynn.billsapp.databinding.FragmentSummaryBinding
import com.lynn.billsapp.utils.Utils
import com.lynn.billsapp.viewModels.BillsSummary
import com.lynn.billsapp.viewModels.BillsViewModel

class summaryFragment : Fragment() {
    var binding: FragmentSummaryBinding? = null
    val billsViewModel:BillsViewModel by viewModels()
    lateinit var summaaryChartView: AnyChartView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSummaryBinding.inflate(inflater, container, false)
        return  binding?.root
    }

    override fun onResume() {
        super.onResume()
        binding?.fabAddBill?.setOnClickListener {
            startActivity(Intent(requireContext(), BillsActivity::class.java))
        }
        billsViewModel.getMonthlySummary()
        showMonthlySummary()
    }


    fun showMonthlySummary(){
            billsViewModel.summaryLiveData.observe(this){
                    summary->binding?.tvPaidAmt?.text= Utils.formatCurrency(summary.paid)
                binding?.tvPendingAmt?.text= Utils.formatCurrency(summary.upcoming)
                binding?.tvTotalAmt?.text=Utils.formatCurrency(summary.total)
                binding?.tvOverdueAmt?.text=Utils.formatCurrency(summary.overdue)
                showChartSummary(summary)
            }
        }

    fun showChartSummary(summary: BillsSummary){
        val entries= mutableListOf<DataEntry>()
        entries.add(ValueDataEntry("Paid",summary.paid))
        entries.add(ValueDataEntry("Upcoming",summary.upcoming))
        entries.add(ValueDataEntry("Overdue",summary.overdue))

        val pieChart=AnyChart.pie()
        pieChart.data(entries)
        pieChart.innerRadius(50)
        pieChart.palette().itemAt(0,SolidFill("",100))
        pieChart.palette().itemAt(1,SolidFill("",100))
        pieChart.palette().itemAt(2,SolidFill("",100))

        binding?.summaryChart?.setChart(pieChart)

    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
    }


