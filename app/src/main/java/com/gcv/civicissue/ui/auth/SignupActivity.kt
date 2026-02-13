package com.gcv.civicissue.ui.auth

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gcv.civicissue.data.model.User
import com.gcv.civicissue.data.repository.FirebaseRepository
import com.gcv.civicissue.databinding.ActivitySignupBinding
import com.google.firebase.firestore.FirebaseFirestore

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private val repo = FirebaseRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignup.setOnClickListener {

            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isEmpty() || password.length < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            repo.signup(email, password) { success, message ->
                if (success) {

                    val userId = repo.getCurrentUserId() ?: return@signup

                    val role = if (email == "admin@gmail.com") "resolver" else "citizen"

                    val user = User(
                        userId = userId,
                        email = email,
                        role = role
                    )

                    FirebaseFirestore.getInstance()
                        .collection("users")
                        .document(userId)
                        .set(user)

                    Toast.makeText(this, "Account Created!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                }
            }

        }
    }
}
