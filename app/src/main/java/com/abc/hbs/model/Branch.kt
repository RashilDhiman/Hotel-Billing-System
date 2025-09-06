package com.abc.hbs.model

data class Branch(
    val branchId: String = "",
    val name: String = "",
    val address: String = "",
    val phone: String = "",
    val email: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val id: String = ""
)
