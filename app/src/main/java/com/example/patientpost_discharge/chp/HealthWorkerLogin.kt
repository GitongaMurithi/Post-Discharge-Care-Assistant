package com.example.patientpost_discharge.chp

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.patientpost_discharge.common.Login
import com.example.patientpost_discharge.common.MainActivity
import com.example.patientpost_discharge.databinding.ActivityHealthWorkerLoginBinding
import com.example.patientpost_discharge.patient.PatientRegistration
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HealthWorkerLogin : AppCompatActivity() {

    private lateinit var binding: ActivityHealthWorkerLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var email: String
    private lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHealthWorkerLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.textview10.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        binding.button8.setOnClickListener {

            validateUser()
        }
    }

    private fun signInWithEmailAndPassword() {
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener { authResult ->
            val uid = authResult.user!!.uid
            FirebaseDatabase.getInstance().getReference("Health workers/$uid")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            binding.progressBar6.visibility = View.GONE
                            startActivity(
                                Intent(
                                    this@HealthWorkerLogin,
                                    HealthWorkerDashboard::class.java
                                )
                            )
                            binding.edittext15.setText("")
                            binding.edittext16.setText("")
                        } else {
                            val alertDialog = AlertDialog.Builder(this@HealthWorkerLogin)
                            alertDialog.apply {
                                setTitle("Error")
                                setMessage("This account does not exist. Ensure you have a chp account to login.")
                                setCancelable(false)
                                setPositiveButton("Register") { dialog, _ ->
                                    startActivity(
                                        Intent(
                                            this@HealthWorkerLogin,
                                            PatientRegistration::class.java
                                        )
                                    )
                                    finish()
                                    dialog.dismiss()
                                }
                                setNegativeButton("Cancel") { dialog, _ ->
                                    startActivity(Intent(this@HealthWorkerLogin, Login::class.java))
                                    finish()
                                    dialog.dismiss()
                                }
                                create()
                                show()
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@HealthWorkerLogin, error.message, Toast.LENGTH_LONG)
                            .show()
                    }

                })
        }
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    binding.progressBar6.visibility = View.GONE
                    Toast.makeText(
                        this@HealthWorkerLogin,
                        task.exception?.localizedMessage,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    private fun validateUser() {
        email = binding.edittext15.text.toString().trim()
        password = binding.edittext16.text.toString().trim()

        if (email.isEmpty()) {
            binding.edittext15.error = "This is a required field!"
            binding.edittext15.requestFocus()
            return
        }

        if (password.isEmpty()) {
            binding.edittext16.error = "This is a required field!"
            binding.edittext16.requestFocus()
            return
        }

        if (password.length < 4) {
            binding.edittext16.error = "Enter a minimum of 4 characters!"
            binding.edittext16.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.edittext15.error = "Enter a valid email address!"
            binding.edittext15.requestFocus()
            return
        }

        binding.progressBar6.visibility = View.VISIBLE

        signInWithEmailAndPassword()

    }
}

