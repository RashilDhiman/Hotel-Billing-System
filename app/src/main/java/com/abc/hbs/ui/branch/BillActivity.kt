package com.abc.hbs.ui.branch

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abc.hbs.R
import com.abc.hbs.adapters.BillItemsAdapter
import com.abc.hbs.models.Bill
import com.abc.hbs.models.BillItem
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class BillActivity : AppCompatActivity() {

    private lateinit var etCustomerName: EditText
    private lateinit var etItemName: EditText
    private lateinit var etItemQty: EditText
    private lateinit var etItemPrice: EditText
    private lateinit var btnAddItem: Button
    private lateinit var btnSaveBill: Button
    private lateinit var tvTotalAmount: TextView
    private lateinit var rvItems: RecyclerView

    private lateinit var adapter: BillItemsAdapter
    private val items = mutableListOf<BillItem>()

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_bill)

        val toolbar: MaterialToolbar = findViewById(R.id.topAppBarBill)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener {
            finish()
        }


        etCustomerName = findViewById(R.id.etCustomerName)
        etItemName = findViewById(R.id.etItemName)
        etItemQty = findViewById(R.id.etItemQty)
        etItemPrice = findViewById(R.id.etItemPrice)
        btnAddItem = findViewById(R.id.btnAddItem)
        btnSaveBill = findViewById(R.id.btnSaveBill)
        tvTotalAmount = findViewById(R.id.tvTotalAmount)
        rvItems = findViewById(R.id.rvItems)

        adapter = BillItemsAdapter(items)
        rvItems.layoutManager = LinearLayoutManager(this)
        rvItems.adapter = adapter

        btnAddItem.setOnClickListener {
            val name = etItemName.text.toString()
            val qty = etItemQty.text.toString().toIntOrNull() ?: 0
            val price = etItemPrice.text.toString().toDoubleOrNull() ?: 0.0
            val userId = auth.currentUser?.uid ?: ""

            if (name.isNotEmpty() && qty > 0 && price > 0) {
                val item = BillItem(name, qty, price, userId)
                adapter.addItem(item)
                updateTotal()
                etItemName.text.clear()
                etItemQty.text.clear()
                etItemPrice.text.clear()
            } else {
                Toast.makeText(this, "Enter valid item details", Toast.LENGTH_SHORT).show()
            }
        }

        btnSaveBill.setOnClickListener {
            saveBillToFirestore()
        }
    }

    private fun updateTotal() {
        val total = items.sumOf { it.getTotal() }
        tvTotalAmount.text = "Total: ₹$total"
    }

    private fun saveBillToFirestore() {
        val customerName = etCustomerName.text.toString()
        val userId = auth.currentUser?.uid ?: ""
        val total = items.sumOf { it.getTotal() }

        if (customerName.isEmpty() || items.isEmpty()) {
            Toast.makeText(this, "Enter customer name and add items", Toast.LENGTH_SHORT).show()
            return
        }

        val billId = UUID.randomUUID().toString()
        val bill = Bill(billId, customerName, System.currentTimeMillis(), items, total, userId)

        db.collection("bills").document(billId)
            .set(bill)
            .addOnSuccessListener {
                Toast.makeText(this, "Bill saved successfully", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
