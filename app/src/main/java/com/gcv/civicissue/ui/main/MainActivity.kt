package com.gcv.civicissue.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.gcv.civicissue.R
import com.gcv.civicissue.data.model.Issue
import com.gcv.civicissue.data.repository.FirebaseRepository
import com.gcv.civicissue.databinding.ActivityMainBinding
import com.gcv.civicissue.ui.auth.LoginActivity
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val firestore = FirebaseFirestore.getInstance()
    private val repo = FirebaseRepository()

    private val issueList = mutableListOf<Issue>()
    private lateinit var adapter: IssueAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Toolbar Logout
        binding.toolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.menuLogout) {
                repo.logout()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
                true
            } else false
        }

        // FAB
        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, AddIssueActivity::class.java))
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.fade_out)
        }

        // RecyclerView
        binding.recyclerViewIssues.layoutManager = LinearLayoutManager(this)
        adapter = IssueAdapter(issueList, false)
        binding.recyclerViewIssues.adapter = adapter

        // FAB Scroll Animation
        binding.recyclerViewIssues.addOnScrollListener(object :
            androidx.recyclerview.widget.RecyclerView.OnScrollListener() {

            override fun onScrolled(
                recyclerView: androidx.recyclerview.widget.RecyclerView,
                dx: Int,
                dy: Int
            ) {
                if (dy > 10) {
                    binding.fabAdd.animate().scaleX(0f).scaleY(0f).alpha(0f).setDuration(200).start()
                } else if (dy < -10) {
                    binding.fabAdd.animate().scaleX(1f).scaleY(1f).alpha(1f).setDuration(200).start()
                }
            }
        })

        // Show shimmer before loading
        binding.shimmerContainer.visibility = View.VISIBLE
        binding.shimmerContainer.startShimmer()
        binding.recyclerViewIssues.visibility = View.GONE

        loadIssues()

        // Pull to refresh
        binding.swipeRefresh.setOnRefreshListener {
            loadIssues()
            binding.swipeRefresh.isRefreshing = false
        }

        // Filter chips
        binding.chipAll.setOnClickListener { filterIssues(null) }
        binding.chipPending.setOnClickListener { filterIssues("Pending") }
        binding.chipResolved.setOnClickListener { filterIssues("Resolved") }
    }

    private fun loadIssues() {

        firestore.collection("issues")
            .addSnapshotListener { snapshot, error ->

                if (error != null) return@addSnapshotListener

                issueList.clear()

                snapshot?.documents?.forEach { doc ->
                    val issue = doc.toObject(Issue::class.java)
                    if (issue != null) {
                        issueList.add(issue)
                    }
                }

                adapter.notifyDataSetChanged()

                // 🔥 Stop shimmer AFTER data loads
                binding.shimmerContainer.stopShimmer()
                binding.shimmerContainer.visibility = View.GONE
                binding.recyclerViewIssues.visibility = View.VISIBLE
            }
    }

    private fun filterIssues(status: String?) {
        val filteredList = if (status == null) {
            issueList
        } else {
            issueList.filter { it.status == status }
        }

        adapter = IssueAdapter(filteredList, false)
        binding.recyclerViewIssues.adapter = adapter
    }
}
