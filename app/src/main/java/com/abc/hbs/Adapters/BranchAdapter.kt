package com.abc.hbs.ui.branch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.abc.hbs.R
import com.abc.hbs.model.Branch

class BranchAdapter(private val branches: List<Branch>) :
    RecyclerView.Adapter<BranchAdapter.BranchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BranchViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_branch, parent, false)
        return BranchViewHolder(view)
    }

    override fun onBindViewHolder(holder: BranchViewHolder, position: Int) {
        val branch = branches[position]
        holder.name.text = branch.name
        holder.address.text = branch.address
        holder.phone.text = branch.phone
        holder.email.text = branch.email
    }

    override fun getItemCount(): Int = branches.size

    class BranchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tvBranchName)
        val address: TextView = itemView.findViewById(R.id.tvBranchAddress)
        val phone: TextView = itemView.findViewById(R.id.tvBranchPhone)
        val email: TextView = itemView.findViewById(R.id.tvBranchEmail)
    }
}
