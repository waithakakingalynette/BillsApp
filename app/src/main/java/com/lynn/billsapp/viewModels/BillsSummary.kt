package com.lynn.billsapp.viewModels

data class BillsSummary(
    var paid:Double,
    var upcoming:Double,
    var overdue:Double,
    var total:Double
)
