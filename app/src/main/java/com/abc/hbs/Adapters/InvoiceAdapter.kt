package com.abc.hbs.ui.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.abc.hbs.R
import com.abc.hbs.models.Bill
import java.text.SimpleDateFormat
import java.util.*

class InvoiceAdapter(private val bills: List<Bill>) :
    RecyclerView.Adapter<InvoiceAdapter.InvoiceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InvoiceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_invoice, parent, false)
        return InvoiceViewHolder(view)
    }

    override fun onBindViewHolder(holder: InvoiceViewHolder, position: Int) {
        val bill = bills[position]

        holder.tvInvoiceNumber.text = "Invoice #${bill.billId}"
        holder.tvCustomerName.text = "Customer: ${bill.customerName ?: "N/A"}"
        holder.tvInvoiceAmount.text = "Total: ₹${String.format("%.2f", bill.totalAmount)}"
        holder.tvInvoiceDate.text = "Date: ${formatDate(bill.date)}"
    }

    override fun getItemCount(): Int = bills.size

    class InvoiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvInvoiceNumber: TextView = itemView.findViewById(R.id.tvInvoiceNumber)
        val tvCustomerName: TextView = itemView.findViewById(R.id.tvCustomerName)
        val tvInvoiceAmount: TextView = itemView.findViewById(R.id.tvInvoiceAmount)
        val tvInvoiceDate: TextView = itemView.findViewById(R.id.tvInvoiceDate)
    }

    private fun formatDate(timestamp: Long?): String {
        return try {
            if (timestamp == null) return "N/A"
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            sdf.format(Date(timestamp))
        } catch (e: Exception) {
            timestamp.toString()
        }
    }
}
