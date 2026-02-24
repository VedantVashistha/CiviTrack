package com.gcv.civicissue.ui.main

import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.gcv.civicissue.data.model.Issue
import com.gcv.civicissue.data.model.AiAnalysis
import com.gcv.civicissue.data.remote.AiApiService
import com.gcv.civicissue.data.remote.DescriptionRequest
import com.gcv.civicissue.databinding.ActivityAddIssueBinding
import com.gcv.civicissue.utils.RetrofitClient
import com.google.firebase.firestore.FirebaseFirestore
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.util.UUID

class AddIssueActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddIssueBinding
    private val firestore = FirebaseFirestore.getInstance()
    private var selectedImageBase64: String = ""

    private val imagePicker =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                binding.ivImage.setImageURI(it)
                val bitmap =
                    MediaStore.Images.Media.getBitmap(contentResolver, it)
                selectedImageBase64 = convertBitmapToBase64(bitmap)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddIssueBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSelectImage.setOnClickListener {
            imagePicker.launch("image/*")
        }

        binding.btnSubmit.setOnClickListener {
            submitIssue()
        }
    }

    private fun submitIssue() {

        val title = binding.etTitle.text.toString().trim()
        val description = binding.etDescription.text.toString().trim()
        val type = binding.etType.text.toString().trim()

        if (title.isEmpty() || description.isEmpty() || type.isEmpty()) {
            Toast.makeText(this, "All fields required", Toast.LENGTH_SHORT).show()
            return
        }

        binding.progressBar.visibility = View.VISIBLE

        val api = RetrofitClient.instance.create(AiApiService::class.java)
        val request = DescriptionRequest(description)

        api.analyzeIssue(request).enqueue(object : Callback<AiAnalysis> {

            override fun onResponse(
                call: Call<AiAnalysis>,
                response: Response<AiAnalysis>
            ) {
                Log.d("AI_DEBUG", "Code: ${response.code()}")
                Log.d("AI_DEBUG", "Body: ${response.body()}")

                if (!response.isSuccessful || response.body() == null) {
                    Log.e("AI_DEBUG", "Parsing failed")
                    Toast.makeText(this@AddIssueActivity, "PARSE FAILURE", Toast.LENGTH_LONG).show()
                    saveIssue(title, description, type, "Low", 48)
                    return
                }

                val severity = response.body()!!.severity
                val hours = response.body()!!.hours

                saveIssue(title, description, type, severity, hours)
            }

            override fun onFailure(call: Call<AiAnalysis>, t: Throwable) {
                Toast.makeText(this@AddIssueActivity, "NETWORK FAILURE", Toast.LENGTH_LONG).show()
                Log.e("AI_DEBUG", "Network error: ${t.message}")
                saveIssue(title, description, type, "Low", 48)
            }
        })

        Log.d("AI_DEBUG", "Calling URL: ${RetrofitClient.instance.baseUrl()}")

    }

    private fun saveIssue(
        title: String,
        description: String,
        type: String,
        severity: String,
        hours: Int
    ) {

        val issueId = UUID.randomUUID().toString()

        val issue = Issue(
            id = issueId,
            title = title,
            description = description,
            type = type,
            status = "Pending",
            imageBase64 = selectedImageBase64,
            severity = severity,
            estimatedHours = hours,
            timestamp = System.currentTimeMillis()
        )

        firestore.collection("issues")
            .document(issueId)
            .set(issue)
            .addOnSuccessListener {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, "Issue Reported", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
            }
    }

    private fun convertBitmapToBase64(bitmap: Bitmap): String {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream)
        return Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT)
    }
}
