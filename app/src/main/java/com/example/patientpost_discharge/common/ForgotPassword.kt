package com.example.patientpost_discharge.common

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.patientpost_discharge.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPassword : AppCompatActivity() {
    private lateinit var binding : ActivityForgotPasswordBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        binding.button9.setOnClickListener {
            val email = binding.edittext17.text.toString().trim()

            if (email.isEmpty()) {
                binding.edittext17.error = "This is a required field!"
                binding.edittext17.requestFocus()
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.edittext17.error = "Enter a valid emal address!"
                binding.edittext17.requestFocus()
                return@setOnClickListener
            }
            binding.progressBar7.visibility = View.VISIBLE

            auth.sendPasswordResetEmail(email).addOnCompleteListener {  task ->
                if (task.isSuccessful) {
                    if (task.isSuccessful) {
                        binding.progressBar7.visibility = View.GONE
                        Toast.makeText(
                            this@ForgotPassword,
                            "Check your email to reset password",
                            Toast.LENGTH_LONG
                        ).show()
                        startActivity(Intent(this@ForgotPassword, Login::class.java))
                        binding.edittext17.setText("")
                    } else {
                        binding.progressBar7.visibility = View.GONE
                        Toast.makeText(
                            this@ForgotPassword,
                            "Failed to reset the password!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }
}