package com.lynn.billsapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.lynn.billsapp.databinding.UpcomingBillsListItemBinding
import com.lynn.billsapp.models.UpcomingBill


class UpcomingBillsAdapter(
    var upcomingBill: List<UpcomingBill>, private  val onClickBill: OnClickBill):Adapter<UpcomingBillsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpcomingBillsViewHolder {
        val binding=UpcomingBillsListItemBinding.inflate(LayoutInflater.from(parent.context))
        return  UpcomingBillsViewHolder(binding)
    }
    override fun onBindViewHolder(holder: UpcomingBillsViewHolder, position: Int) {
        val upcomingBill=upcomingBill.get(position)
        holder.binding.apply {
            cbUpcoming.text=upcomingBill.name
            tvAmount.text=upcomingBill.amount.toString()
            tvDueDate.text=upcomingBill.dueDate
        }
        holder.binding.cbUpcoming.setOnClickListener {
            onClickBill.onCheckBoxMarked(upcomingBill)
        }
    }

    override fun getItemCount(): Int {
        return upcomingBill.size
    }
}
class UpcomingBillsViewHolder(var binding: UpcomingBillsListItemBinding):ViewHolder(binding.root){

}interface OnClickBill{
    fun onCheckBoxMarked(upcomingBill: UpcomingBill)
}

