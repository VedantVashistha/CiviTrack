package com.gcv.civicissue.ui.main

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gcv.civicissue.data.model.Issue
import com.gcv.civicissue.data.repository.FirebaseRepository
import com.gcv.civicissue.databinding.ActivityAddIssueBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.io.ByteArrayOutputStream
import java.util.*

class AddIssueActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddIssueBinding
    private var imageBase64: String = ""
    private val firestore = FirebaseFirestore.getInstance()
    private val repo = FirebaseRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddIssueBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSelectImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 100)
        }

        binding.btnSubmit.setOnClickListener {

            val title = binding.etTitle.text.toString().trim()
            val desc = binding.etDescription.text.toString().trim()
            val type = binding.etType.text.toString().trim()

            if (title.isEmpty() || desc.isEmpty() || type.isEmpty() || imageBase64.isEmpty()) {
                Toast.makeText(this, "All fields required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val id = UUID.randomUUID().toString()

            val issue = Issue(
                id = id,
                title = title,
                description = desc,
                type = type,
                imageBase64 = imageBase64,
                userId = repo.getCurrentUserId() ?: ""
            )

            firestore.collection("issues")
                .document(id)
                .set(issue)
                .addOnSuccessListener {
                    Toast.makeText(this, "Issue Submitted!", Toast.LENGTH_SHORT).show()
                    finish()
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.slide_out_right)
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            val uri: Uri? = data?.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)

            binding.ivImage.setImageBitmap(bitmap)
            imageBase64 = convertBitmapToBase64(bitmap)
        }
    }

    private fun convertBitmapToBase64(bitmap: Bitmap): String {
        val resized = Bitmap.createScaledBitmap(bitmap, 600, 600, true)
        val stream = ByteArrayOutputStream()
        resized.compress(Bitmap.CompressFormat.JPEG, 60, stream)
        val bytes = stream.toByteArray()
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }
}
