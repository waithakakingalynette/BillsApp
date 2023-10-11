package com.lynn.billsapp.ui

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import com.lynn.billsapp.R
import com.lynn.billsapp.models.Bill
import kotlinx.coroutines.NonCancellable.parent

class BillsAdapter(context: Context, resource: Int, bills: List<Bill>, position: Int): ArrayAdapter<Bill>(context, resource, bills) {
    val bill = getItem(position)
    val itemView = convertView ?: LayoutInflater.from(context)
        .inflate(R.layout.item_bill, parent, false)
    val viewHolder = ViewHolder(itemView)
    viewHolder.bind(bill)
    return itemView
}
class ViewHolder(itemView: View) {
    private val tvName: TextView = itemView.findViewById(R.id.etBillName)
    private val tvAmount: TextView = itemView.findViewById(R.id.tvAmount)
    private val tvDueDate: TextView = itemView.findViewById(R.id.tvDueDate)
    private val tvFrequency: TextView = itemView.findViewById(R.id.tvFrequency)
    private val tvBillId: TextView = itemView.findViewById(R.id.tvBillId)
    private val tvUserId: TextView = itemView.findViewById(R.id.tvUserId)
    @SuppressLint("SetTextI18n")
    fun bind(bill: Bill?) {
        if (bill != null) {
            tvName.text = "Name: ${bill.name}"
            tvAmount.text = "Amount: ${bill.amount}"
            tvDueDate.text = "Due Date: ${bill.dueDate}"
            tvFrequency.text = "Frequency: ${bill.frequency}"
            tvBillId.text = "BillId: ${bill.billId}"
            tvUserId.text = "UserId: ${bill.userId}"
        }
    }
}
