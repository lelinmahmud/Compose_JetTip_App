package com.bd.kaz.composejettipapp.util

fun calculateTotalTip(totalBill: Double,tipPercentage: Int) : Double {
    return if (totalBill > 1 && totalBill.toString().isNotEmpty()) (totalBill*tipPercentage)/100
    else 0.0

}

fun calculateTotalPerPerson(
    totalBill: Double,
    spiltBy : Int,
    tipPercentage: Int
) : Double{
    val bill = calculateTotalTip(totalBill,tipPercentage) + totalBill
    return bill/spiltBy
}