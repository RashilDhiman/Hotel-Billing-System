package com.abc.hbs.ui.admin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abc.hbs.R
import com.abc.hbs.model.Branch
import com.abc.hbs.ui.branch.AddBranchActivity
import com.abc.hbs.ui.branch.BranchAdapter
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class ManageBranchesActivity : AppCompatActivity() {

    private lateinit var rvBranches: RecyclerView
    private lateinit var fabAddBranch: FloatingActionButton
    private lateinit var branchAdapter: BranchAdapter
    private val branchList = mutableListOf<Branch>()

    private val db = FirebaseFirestore.getInstance()
    private var branchListener: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_branches)

        val toolbar: MaterialToolbar = findViewById(R.id.topAppBar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener {
            // Go back when back arrow is clicked
            finish()
        }



        rvBranches = findViewById(R.id.rvBranches)
        fabAddBranch = findViewById(R.id.fabAddBranch)

        branchAdapter = BranchAdapter(branchList)
        rvBranches.layoutManager = LinearLayoutManager(this)
        rvBranches.adapter = branchAdapter

        fabAddBranch.setOnClickListener {
            startActivity(Intent(this, AddBranchActivity::class.java))
        }

        loadBranches()
    }

    private fun loadBranches() {
        branchListener = db.collection("branches")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Toast.makeText(this, "Error loading branches", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                branchList.clear()
                snapshot?.forEach { doc ->
                    val branch = doc.toObject(Branch::class.java).copy(id = doc.id)
                    branchList.add(branch)
                }
                branchAdapter.notifyDataSetChanged()
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        branchListener?.remove()
    }
}