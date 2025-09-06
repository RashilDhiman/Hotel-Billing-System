package com.abc.hbs.ui.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.abc.hbs.R

class DashboardMenuAdapter(
    private val menuItems: List<DashboardMenuItem>,
    private val onItemClick: (DashboardMenuItem) -> Unit
) : RecyclerView.Adapter<DashboardMenuAdapter.MenuViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_dashboard_menu, parent, false)
        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val menuItem = menuItems[position]
        holder.bind(menuItem)
    }

    override fun getItemCount(): Int = menuItems.size

    inner class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cardView: CardView = itemView.findViewById(R.id.cardMenu)
        private val ivIcon: ImageView = itemView.findViewById(R.id.ivMenuIcon)
        private val tvTitle: TextView = itemView.findViewById(R.id.tvMenuTitle)

        fun bind(menuItem: DashboardMenuItem) {
            ivIcon.setImageResource(menuItem.iconRes)
            tvTitle.text = menuItem.title

            // Set icon tint color
            ivIcon.setColorFilter(ContextCompat.getColor(itemView.context, menuItem.colorRes))

            // Add click listener with animation
            cardView.setOnClickListener {
                // Add subtle click animation
                cardView.animate()
                    .scaleX(0.95f)
                    .scaleY(0.95f)
                    .setDuration(100)
                    .withEndAction {
                        cardView.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(100)
                            .start()
                        onItemClick(menuItem)
                    }
                    .start()
            }

            // Add ripple effect
            cardView.isClickable = true
            cardView.isFocusable = true
        }
    }
}