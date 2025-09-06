package com.abc.hbs.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.abc.hbs.R
import com.abc.hbs.ui.admin.AdminDashboardActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Enter all fields", Toast.LENGTH_SHORT).show()
            } else {
                loginOrRegister(email, password)
            }
        }
    }

    private fun loginOrRegister(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid ?: return@addOnCompleteListener

                    // ✅ Ensure user exists in Firestore
                    val userRef = db.collection("users").document(uid)
                    userRef.get().addOnSuccessListener { document ->
                        if (!document.exists()) {
                            val user = hashMapOf(
                                "email" to email,
                                "role" to "admin",
                                "createdAt" to System.currentTimeMillis(),
                                "lastLoginAt" to System.currentTimeMillis()
                            )
                            userRef.set(user)
                        } else {
                            userRef.update("lastLoginAt", System.currentTimeMillis())
                        }

                        startActivity(Intent(this, AdminDashboardActivity::class.java))
                        finish()
                    }
                } else {
                    // ❌ Try to create new account
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { createTask ->
                            if (createTask.isSuccessful) {
                                val uid = auth.currentUser?.uid ?: ""
                                val user = hashMapOf(
                                    "email" to email,
                                    "role" to "admin",
                                    "createdAt" to System.currentTimeMillis(),
                                    "lastLoginAt" to System.currentTimeMillis()
                                )
                                db.collection("users").document(uid).set(user)

                                Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this, AdminDashboardActivity::class.java))
                                finish()
                            } else {
                                Toast.makeText(this, "Error: ${createTask.exception?.message}", Toast.LENGTH_LONG).show()
                            }
                        }
                }
            }
    }

}
