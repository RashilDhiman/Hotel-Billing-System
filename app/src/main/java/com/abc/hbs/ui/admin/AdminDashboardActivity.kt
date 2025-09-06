package com.abc.hbs.ui.admin

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abc.hbs.R
import com.abc.hbs.ui.auth.LoginActivity
import com.abc.hbs.ui.branch.BillActivity
import com.abc.hbs.ui.customer.AddCustomerActivity
import com.abc.hbs.ui.customer.CustomerListActivity
import com.abc.hbs.ui.invoice.InvoiceActivity
import com.abc.hbs.utils.SharedPref
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class AdminDashboardActivity : AppCompatActivity() {

    private lateinit var tvBillsCount: TextView
    private lateinit var tvCustomersCount: TextView
    private lateinit var rvMenuGrid: RecyclerView
    private lateinit var ivNotification: ImageView
    private lateinit var sharedPref: SharedPref

    // Firebase
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    // Firestore listeners
    private var billsListener: ListenerRegistration? = null
    private var customersListener: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)

        initViews()
        setupMenuGrid()
        loadStatistics()
        setupClickListeners()
    }

    private fun initViews() {
        tvBillsCount = findViewById(R.id.tvBillsCount)
        tvCustomersCount = findViewById(R.id.tvCustomersCount)
        rvMenuGrid = findViewById(R.id.rvMenuGrid)
        ivNotification = findViewById(R.id.ivNotification)
        sharedPref = SharedPref(this)
    }

    private fun setupMenuGrid() {
        val menuItems = listOf(
            DashboardMenuItem("Manage Branches", R.drawable.ic_business, R.color.primary_color),
            DashboardMenuItem("Invoice", R.drawable.ic_invoice, R.color.secondary_color),
            DashboardMenuItem("Add Customer", R.drawable.ic_person_add, R.color.success_color),
            DashboardMenuItem("Customer List", R.drawable.ic_list, R.color.info_color),
            DashboardMenuItem("Create Bill", R.drawable.ic_receipt_long, R.color.warning_color),
            DashboardMenuItem("Logout", R.drawable.ic_logout, R.color.error_color)
        )

        val adapter = DashboardMenuAdapter(menuItems) { menuItem ->
            handleMenuClick(menuItem.title)
        }

        rvMenuGrid.layoutManager = GridLayoutManager(this, 2)
        rvMenuGrid.adapter = adapter
    }

    private fun loadStatistics() {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        // ✅ Real-time listener for Bills
        billsListener = db.collection("bills")
            .whereEqualTo("createdBy", uid)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    return@addSnapshotListener
                }
                val billCount = snapshot?.size() ?: 0
                animateCount(tvBillsCount, billCount)
            }

        // ✅ Real-time listener for Customers
        customersListener = db.collection("customers")
            .whereEqualTo("createdBy", uid)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    return@addSnapshotListener
                }
                val customerCount = snapshot?.size() ?: 0
                animateCount(tvCustomersCount, customerCount)
            }

        // ✅ Fetch user info (welcome message, role, etc.)
        db.collection("users").document(uid).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val role = document.getString("role") ?: "admin"
                    val email = document.getString("email") ?: ""
                }
            }
    }

    private fun animateCount(textView: TextView, targetValue: Int) {
        val currentValue = textView.text.toString().toIntOrNull() ?: 0
        val animator = android.animation.ValueAnimator.ofInt(currentValue, targetValue)
        animator.duration = 500
        animator.addUpdateListener { animation ->
            textView.text = animation.animatedValue.toString()
        }
        animator.start()
    }

    private fun setupClickListeners() {
        ivNotification.setOnClickListener {
        }
    }

    private fun handleMenuClick(menuTitle: String) {
        when (menuTitle) {
            "Manage Branches" -> {
                startActivity(Intent(this, ManageBranchesActivity::class.java))
            }
            "Invoice" -> {
                startActivity(Intent(this, InvoiceActivity::class.java))
            }
            "Add Customer" -> {
                startActivity(Intent(this, AddCustomerActivity::class.java))
            }
            "Customer List" -> {
                startActivity(Intent(this, CustomerListActivity::class.java))
            }
            "Create Bill" -> {
                startActivity(Intent(this, BillActivity::class.java))
            }
            "Logout" -> {
                showLogoutConfirmation()
            }
        }
    }

    private fun showLogoutConfirmation() {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { _, _ ->
                handleLogout()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun handleLogout() {
        sharedPref.clear()
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    override fun onResume() {
        super.onResume()
        loadStatistics()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Remove Firestore listeners to avoid memory leaks
        billsListener?.remove()
        customersListener?.remove()
    }
}

// Data class for menu items
data class DashboardMenuItem(
    val title: String,
    val iconRes: Int,
    val colorRes: Int
)
