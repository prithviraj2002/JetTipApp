package com.example.jettipapp.util

fun calculateTip(TotalBill: Double, tipPercent: Int): Double {
    return if (TotalBill >1 && TotalBill.toString().isNotEmpty())
        (TotalBill * tipPercent)/100 else 0.0
}

fun calculateTotalPerPerson(
        TotalBill: Double,
        tipPercent: Int,
        splitBy: Int,
): Double{
        val bill = calculateTip(TotalBill = TotalBill, tipPercent = tipPercent) + TotalBill
    return (bill/splitBy)

}