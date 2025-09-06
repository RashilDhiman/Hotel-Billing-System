package com.abc.hbs.ui.customer

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.abc.hbs.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.card.MaterialCardView
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CustomerListActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private var listener: ListenerRegistration? = null
    private val TAG = "CustomerListDebug"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_list)

        Log.d(TAG, "Activity created")

        val toolbar = findViewById<MaterialToolbar>(R.id.topAppBar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Customer List"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        // Check current user
        val currentUser = auth.currentUser
        if (currentUser == null) {
            Log.e(TAG, "User is null - not authenticated")
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        Log.d(TAG, "Current user UID: ${currentUser.uid}")
        Log.d(TAG, "Expected userId in Firestore: ${currentUser.uid}")

        // start listening for changes
        listenForCustomers()
    }

    private fun listenForCustomers() {
        val container = findViewById<LinearLayout>(R.id.containerCustomers)
        container.removeAllViews()

        val uid = auth.currentUser?.uid
        if (uid == null) {
            Log.e(TAG, "UID is null")
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d(TAG, "Querying customers for UID: $uid")

        // ❌ Removed orderBy("createdAt") to support both Number and Timestamp
        listener = db.collection("customers")
            .whereEqualTo("userId", uid)
            .addSnapshotListener { snapshot, error ->
                Log.d(TAG, "Snapshot received. Error: $error, Snapshot: $snapshot")

                container.removeAllViews()

                if (error != null) {
                    Log.e(TAG, "Firestore error: ${error.message}", error)
                    Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_LONG).show()

                    val errorText = TextView(this).apply {
                        text = "Error: ${error.message}"
                        textSize = 16f
                    }
                    container.addView(errorText)
                    return@addSnapshotListener
                }

                if (snapshot == null) {
                    Log.w(TAG, "Snapshot is null")
                    val nullText = TextView(this).apply {
                        text = "No data received from server"
                        textSize = 16f
                    }
                    container.addView(nullText)
                    return@addSnapshotListener
                }

                Log.d(TAG, "Snapshot size: ${snapshot.size()}, isEmpty: ${snapshot.isEmpty}")

                if (snapshot.isEmpty) {
                    Log.d(TAG, "No customers found in snapshot")
                    val tvEmpty = TextView(this).apply {
                        text = "No customers found. Add your first customer!"
                        textSize = 16f
                    }
                    container.addView(tvEmpty)
                    return@addSnapshotListener
                }

                Log.d(TAG, "Processing ${snapshot.size()} documents")

                for (doc in snapshot.documents) {
                    Log.d(TAG, "Document ID: ${doc.id}")
                    Log.d(TAG, "Document data: ${doc.data}")

                    val name = doc.getString("name") ?: "Unknown"
                    val email = doc.getString("email") ?: "No Email"
                    val phone = doc.getString("phone") ?: "No Phone"
                    val docUserId = doc.getString("userId") ?: "No UserID"

                    // ✅ Handle createdAt as Number or Timestamp
                    val createdAtField = doc.get("createdAt")
                    val createdAtStr = when (createdAtField) {
                        is Timestamp -> {
                            val date = createdAtField.toDate()
                            SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(date)
                        }
                        is Number -> {
                            val date = Date(createdAtField.toLong())
                            SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(date)
                        }
                        else -> "N/A"
                    }

                    Log.d(TAG, "Customer found: $name, UserID in doc: $docUserId, createdAt: $createdAtStr")

                    val card = MaterialCardView(this).apply {
                        layoutParams = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        ).apply {
                            setMargins(0, 16, 0, 0)
                        }
                        radius = 16f
                        cardElevation = 4f
                        setContentPadding(24, 24, 24, 24)
                    }

                    val innerLayout = LinearLayout(this).apply {
                        orientation = LinearLayout.VERTICAL
                    }

                    val tvName = TextView(this).apply {
                        text = name
                        textSize = 16f
                        setTypeface(typeface, android.graphics.Typeface.BOLD)
                    }

                    val tvEmail = TextView(this).apply {
                        text = email
                        textSize = 14f
                    }

                    val tvPhone = TextView(this).apply {
                        text = phone
                        textSize = 14f
                    }

                    val tvCreatedAt = TextView(this).apply {
                        text = "Added: $createdAtStr"
                        textSize = 12f
                    }

                    innerLayout.addView(tvName)
                    innerLayout.addView(tvEmail)
                    innerLayout.addView(tvPhone)
                    innerLayout.addView(tvCreatedAt)
                    card.addView(innerLayout)
                    container.addView(card)
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        listener?.remove()
        Log.d(TAG, "Activity destroyed")
    }
}
