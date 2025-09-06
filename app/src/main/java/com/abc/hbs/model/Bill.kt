package com.abc.hbs.models

data class Bill(
    val billId: String = "",
    val customerName: String = "",
    val date: Long = System.currentTimeMillis(),
    val items: List<BillItem> = emptyList(),
    val totalAmount: Double = 0.0,
    val userId: String = ""
)
