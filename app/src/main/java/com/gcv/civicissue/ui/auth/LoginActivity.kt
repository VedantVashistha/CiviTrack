package com.gcv.civicissue.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gcv.civicissue.data.repository.FirebaseRepository
import com.gcv.civicissue.databinding.ActivityLoginBinding
import com.gcv.civicissue.ui.main.MainActivity
import com.gcv.civicissue.ui.main.ResolverActivity
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val repo = FirebaseRepository()
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 🔥 Auto login if already logged in
        if (repo.isUserLoggedIn()) {
            redirectBasedOnRole()
        }

        // 🔥 Login Button
        binding.btnLogin.setOnClickListener {

            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            repo.login(email, password) { success, message ->
                if (success) {
                    redirectBasedOnRole()
                } else {
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                }
            }
        }

        // 🔥 Signup Navigation
        binding.tvSignup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }

    // 🔥 Function to redirect based on role
    private fun redirectBasedOnRole() {

        val userId = repo.getCurrentUserId()

        if (userId == null) {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
            return
        }

        firestore.collection("users")
            .document(userId)
            .get()
            .addOnSuccessListener { doc ->

                val role = doc.getString("role")

                if (role == "resolver") {
                    startActivity(Intent(this, ResolverActivity::class.java))
                } else {
                    startActivity(Intent(this, MainActivity::class.java))
                }

                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            }
    }
}
