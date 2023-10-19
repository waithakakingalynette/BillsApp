package com.lynn.billsapp.utils

import java.text.DecimalFormat

class Utils {
    companion object{
        fun formatCurrency(amount:Double):String{
            val fail=DecimalFormat("KES # ###")

            return fail.format(amount)
        }
    }
}
