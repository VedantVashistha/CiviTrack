package com.gcv.civicissue.ui.main

import android.graphics.BitmapFactory
import android.graphics.Color
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.gcv.civicissue.data.model.Issue
import com.gcv.civicissue.databinding.ItemIssueBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.gcv.civicissue.R

class IssueAdapter(
    private val issues: List<Issue>,
    private val isResolver: Boolean
) : RecyclerView.Adapter<IssueAdapter.IssueViewHolder>() {

    private val firestore = FirebaseFirestore.getInstance()

    inner class IssueViewHolder(val binding: ItemIssueBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IssueViewHolder {
        val binding = ItemIssueBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return IssueViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IssueViewHolder, position: Int) {

        val issue = issues[position]

        holder.binding.tvTitle.text = issue.title
        holder.binding.tvDescription.text = issue.description
        holder.binding.tvType.text = "Type: ${issue.type}"
        holder.binding.tvStatus.text = "Status: ${issue.status}"

        // 🔥 Status Color Coding
        when (issue.status) {
            "Pending" -> {
                holder.binding.tvStatus.setBackgroundColor(android.graphics.Color.parseColor("#E53935"))
            }
            "Resolved" -> {
                holder.binding.tvStatus.setBackgroundColor(android.graphics.Color.parseColor("#43A047"))
            }
            "In Progress" -> {
                holder.binding.tvStatus.setBackgroundColor(android.graphics.Color.parseColor("#FB8C00"))
            }
        }


        // 🔥 Decode Base64 Image
        try {
            val bytes = Base64.decode(issue.imageBase64, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            holder.binding.ivIssueImage.setImageBitmap(bitmap)
        } catch (e: Exception) {
            holder.binding.ivIssueImage.setImageResource(android.R.color.darker_gray)
        }

        // 🔥 Show buttons only for resolver
        if (isResolver) {
            holder.binding.layoutResolverButtons.visibility = View.VISIBLE
        } else {
            holder.binding.layoutResolverButtons.visibility = View.GONE
        }

        // 🔥 Resolver: Mark as Resolved
        holder.binding.btnResolve.setOnClickListener {
            firestore.collection("issues")
                .document(issue.id)
                .update("status", "Resolved")
                .addOnSuccessListener {
                    com.google.android.material.snackbar.Snackbar
                        .make(holder.itemView, "Marked as Resolved", com.google.android.material.snackbar.Snackbar.LENGTH_SHORT)
                        .show()
                }
        }

        // 🔥 Resolver: Delete Issue
        holder.binding.btnDelete.setOnClickListener {

            android.app.AlertDialog.Builder(holder.itemView.context)
                .setTitle("Delete Issue")
                .setMessage("Are you sure you want to delete this issue?")
                .setPositiveButton("Yes") { _, _ ->

                    firestore.collection("issues")
                        .document(issue.id)
                        .delete()
                        .addOnSuccessListener {
                            com.google.android.material.snackbar.Snackbar
                                .make(holder.itemView, "Issue Deleted", com.google.android.material.snackbar.Snackbar.LENGTH_SHORT)
                                .show()
                        }
                }
                .setNegativeButton("Cancel", null)
                .show()
        }

        holder.itemView.animation =
            android.view.animation.AnimationUtils.loadAnimation(
                holder.itemView.context,
                R.anim.item_slide_up
            )


    }

    override fun getItemCount(): Int = issues.size
}
