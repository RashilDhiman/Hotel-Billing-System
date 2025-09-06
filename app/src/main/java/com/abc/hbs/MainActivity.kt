package com.abc.hbs

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.abc.hbs.ui.auth.LoginActivity
import com.abc.hbs.ui.admin.AdminDashboardActivity
import com.abc.hbs.utils.SharedPref
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var sharedPref: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // splash layout

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        sharedPref = SharedPref(this)

        Handler(Looper.getMainLooper()).postDelayed({
            val currentUser = auth.currentUser

            if (currentUser == null) {
                // 🔹 Not logged in → go straight to LoginActivity
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                // 🔹 Already logged in → check Firestore role
                val uid = currentUser.uid
                db.collection("users").document(uid).get()
                    .addOnSuccessListener { doc ->
                        if (doc.exists()) {
                            val role = doc.getString("role") ?: "admin"
                            sharedPref.saveUser("firebase_token", role)

                            if (role == "admin") {
                                startActivity(Intent(this, AdminDashboardActivity::class.java))
                            } else {
                                startActivity(Intent(this, LoginActivity::class.java)) // fallback
                            }
                        } else {
                            // No Firestore record → force login
                            startActivity(Intent(this, LoginActivity::class.java))
                        }
                        finish()
                    }
                    .addOnFailureListener {
                        // If Firestore fails → go to login
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    }
            }
        }, 2000) // 2 sec splash delay
    }
}
