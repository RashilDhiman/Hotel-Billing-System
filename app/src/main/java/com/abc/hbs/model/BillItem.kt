package com.abc.hbs.models

data class BillItem(
    val name: String = "",
    val quantity: Int = 0,
    val price: Double = 0.0,
    val userId: String = ""
) {
    fun getTotal(): Double = quantity * price
}
