package com.abc.hbs.ui.invoice

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abc.hbs.R
import com.abc.hbs.models.Bill
import com.abc.hbs.ui.Adapters.InvoiceAdapter
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class InvoiceActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: InvoiceAdapter
    private val billList = mutableListOf<Bill>()   // ✅ Use Bill instead of Invoice

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invoice)

        val toolbar: MaterialToolbar = findViewById(R.id.topAppBarInvoice)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener {
            finish() // Navigate back
        }


        // Firebase
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // RecyclerView setup
        recyclerView = findViewById(R.id.recyclerViewInvoices)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = InvoiceAdapter(billList)   // ✅ Pass Bill list
        recyclerView.adapter = adapter

        // Load bills
        loadBills()
    }

    private fun loadBills() {
        val userId = auth.currentUser?.uid ?: return

        db.collection("bills")
            .whereEqualTo("userId", userId)   // ✅ match your rules
            .get()
            .addOnSuccessListener { result ->
                billList.clear()
                for (doc in result) {
                    val bill = doc.toObject(Bill::class.java).copy(billId = doc.id)
                    billList.add(bill)
                }
                adapter.notifyDataSetChanged()
                if (billList.isEmpty()) {
                    Toast.makeText(this, "No bills found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error loading bills", Toast.LENGTH_SHORT).show()
            }
    }
}
