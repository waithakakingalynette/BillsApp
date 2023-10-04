package com.lynn.billsapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import com.lynn.billsapp.databinding.ActivityBillsBinding
import com.lynn.billsapp.models.Bill
import com.lynn.billsapp.utils.Constants
import com.lynn.billsapp.viewModels.BillsViewModel
import java.util.Calendar
import java.util.UUID

class BillsActivity : AppCompatActivity() {
    lateinit var binding: ActivityBillsBinding
    val billsViewModel: BillsViewModel by viewModels()
    var selectedMonth=0
    var selectedDate=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityBillsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        setupFreqSpinner()
        setUpDueDate()
        binding.btnSave.setOnClickListener {
            startActivity(Intent(this,BillsActivity::class.java))
            validateBill()
        }
    }
    fun setupFreqSpinner(){
        val frequencies= arrayOf("Weekly","Monthly","Yearly")
        val frequencyAdapter=ArrayAdapter(this,android.R.layout.simple_spinner_item,frequencies)
        frequencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        binding.spFrequency.adapter=frequencyAdapter

        binding.spFrequency.onItemSelectedListener=object :OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                when(binding.spFrequency.selectedItem.toString()){
                    Constants.WEEKLY->{
                        showSpinner()
                        setDueDateSpinner(Array(7){it+1})
                    }
                    Constants.MONTHLY->{
                        showSpinner()
                        setDueDateSpinner(Array(31){it+1})
                    }
                    Constants.YEARLY->{
                        setUpDueDate()
                        showSpinner()
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }
    fun  showSpinner(){
        binding.dpDueDate.hide()
        binding.spDueDate.show()

    }
    fun setDueDateSpinner(dates:Array<Int>){
        val dueDateSpinner=ArrayAdapter(this,android.R.layout.simple_spinner_item,dates)
        dueDateSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spDueDate.adapter=dueDateSpinner
    }

    fun setUpDueDate() {
        val calendar = Calendar.getInstance()
        binding.dpDueDate.init(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH),
        ) { view, year,month,date->
            selectedMonth = month + 1
            selectedDate = date
        }
    }


    fun validateBill() {
        val billName = binding.etBillName.text.toString()
        val amount = binding.etAmount.text.toString()
        val frequency = binding.spFrequency.selectedItem.toString()
        var dueDate: String
        if (frequency == Constants.YEARLY) {
            dueDate= "$selectedDate/$selectedMonth"
        } else {
            dueDate=binding.spDueDate.selectedItem.toString()
        }

        var error = false
        if (billName.isBlank()) {
            error = true
            binding.etBillName.error = "Bill name required"
        }
        if (amount <= 0.toString()) {
            error = true
            binding.etAmount.error = "Amount must be greater than zero"
        }
        if (!error) {
            val prefs = getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE)
            val userId = prefs.getString(Constants.USER_ID, Constants.EMPTY_STRING)
            val bill = Bill(
                billId = UUID.randomUUID().toString(),
                name = billName,
                amount = amount.toDouble(),
                userId = userId.toString(),
                frequency = frequency,
                dueDate = dueDate,
                synced = Boolean
            )
            billsViewModel.saveBill(bill)
            clearForm()
        }
    }
    fun clearForm() {
        binding.etBillName.setText(Constants.EMPTY_STRING)
        binding.etAmount.setText(Constants.EMPTY_STRING)
        binding.spFrequency.setSelection(0)
        showSpinner()
        binding.spDueDate.setSelection(0)
    }


}
fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}