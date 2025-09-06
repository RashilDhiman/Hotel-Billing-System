package com.abc.hbs.ui.branch

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.abc.hbs.R
import com.abc.hbs.model.Branch
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore

class AddBranchActivity : AppCompatActivity() {

    private lateinit var etName: TextInputEditText
    private lateinit var etAddress: TextInputEditText
    private lateinit var etPhone: TextInputEditText
    private lateinit var etEmail: TextInputEditText
    private lateinit var btnSave: MaterialButton
    private lateinit var btnCancel: MaterialButton

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_branch)

        // Toolbar back navigation
        val toolbar = findViewById<MaterialToolbar>(R.id.topAppBar)
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        // Init views
        etName = findViewById(R.id.etBranchName)
        etAddress = findViewById(R.id.etBranchAddress)
        etPhone = findViewById(R.id.etBranchPhone)
        etEmail = findViewById(R.id.etBranchEmail)
        btnSave = findViewById(R.id.btnSaveBranch)
        btnCancel = findViewById(R.id.btnCancelBranch)

        btnSave.setOnClickListener { saveBranch() }
        btnCancel.setOnClickListener { finish() }
    }

    private fun saveBranch() {
        val name = etName.text.toString().trim()
        val address = etAddress.text.toString().trim()
        val phone = etPhone.text.toString().trim()
        val email = etEmail.text.toString().trim()

        if (name.isEmpty() || address.isEmpty() || phone.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val branchId = db.collection("branches").document().id
        val branch = Branch(branchId, name, address, phone, email,)

        db.collection("branches").document(branchId)
            .set(branch)
            .addOnSuccessListener {
                Toast.makeText(this, "Branch added!", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
