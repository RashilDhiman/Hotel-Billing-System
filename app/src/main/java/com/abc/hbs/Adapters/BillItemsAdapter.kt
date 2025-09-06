package com.abc.hbs.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.abc.hbs.R
import com.abc.hbs.models.BillItem

class BillItemsAdapter(private val items: MutableList<BillItem>) :
    RecyclerView.Adapter<BillItemsAdapter.BillItemViewHolder>() {

    inner class BillItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameText: TextView = itemView.findViewById(R.id.tvItemName)
        val qtyText: TextView = itemView.findViewById(R.id.tvItemQty)
        val priceText: TextView = itemView.findViewById(R.id.tvItemPrice)
        val totalText: TextView = itemView.findViewById(R.id.tvItemTotal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_bill, parent, false)
        return BillItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: BillItemViewHolder, position: Int) {
        val item = items[position]
        holder.nameText.text = item.name
        holder.qtyText.text = "Qty: ${item.quantity}"
        holder.priceText.text = "₹${item.price}"
        holder.totalText.text = "₹${item.getTotal()}"
    }

    override fun getItemCount(): Int = items.size

    fun addItem(item: BillItem) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    fun getItems(): List<BillItem> = items
}
