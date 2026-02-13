package com.gcv.civicissue.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.gcv.civicissue.R
import com.gcv.civicissue.data.model.Issue
import com.gcv.civicissue.data.repository.FirebaseRepository
import com.gcv.civicissue.databinding.ActivityResolverBinding
import com.gcv.civicissue.ui.auth.LoginActivity
import com.google.firebase.firestore.FirebaseFirestore

class ResolverActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResolverBinding
    private val firestore = FirebaseFirestore.getInstance()
    private val repo = FirebaseRepository()

    private val issueList = mutableListOf<Issue>()
    private lateinit var adapter: IssueAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResolverBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // RecyclerView Setup
        binding.recyclerViewResolver.layoutManager = LinearLayoutManager(this)
        adapter = IssueAdapter(issueList, true)
        binding.recyclerViewResolver.adapter = adapter

        // Toolbar Logout (ONLY ONCE)
        binding.toolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.menuLogout) {
                repo.logout()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
                true
            } else false
        }

        loadAllIssues()
    }

    private fun loadAllIssues() {

        firestore.collection("issues")
            .addSnapshotListener { snapshot, error ->

                if (error != null) {
                    Toast.makeText(this, error.message, Toast.LENGTH_LONG).show()
                    return@addSnapshotListener
                }

                issueList.clear()

                snapshot?.documents?.forEach { doc ->
                    val issue = doc.toObject(Issue::class.java)
                    if (issue != null) {
                        issueList.add(issue)
                    }
                }

                adapter.notifyDataSetChanged()

                // ✅ Stats MUST be inside snapshot
                val total = issueList.size
                val pending = issueList.count { it.status == "Pending" }

                binding.tvTotalIssues.text = total.toString()
                binding.tvPendingIssues.text = pending.toString()
            }
    }
}
