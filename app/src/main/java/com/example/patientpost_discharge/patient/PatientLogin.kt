package com.example.patientpost_discharge.patient

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.patientpost_discharge.common.ForgotPassword
import com.example.patientpost_discharge.common.Login
import com.example.patientpost_discharge.common.MainActivity
import com.example.patientpost_discharge.databinding.ActivityPatientLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PatientLogin : AppCompatActivity() {

    private lateinit var binding: ActivityPatientLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var email: String
    private lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPatientLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()


        binding.textview10.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }


        binding.textview9.setOnClickListener {
            startActivity(Intent(this, ForgotPassword::class.java))
        }
        binding.button8.setOnClickListener {

            email = binding.edittext15.text.toString().trim()
            password = binding.edittext16.text.toString().trim()

            if (email.isEmpty()) {
                binding.edittext15.error = "This is a required field!"
                binding.edittext15.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                binding.edittext16.error = "This is a required field!"
                binding.edittext16.requestFocus()
                return@setOnClickListener
            }

            if (password.length < 4) {
                binding.edittext16.error = "This is a required field!"
                binding.edittext16.requestFocus()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.edittext15.error = "Enter a valid email address!"
                binding.edittext15.requestFocus()
                return@setOnClickListener
            }

            binding.progressBar6.visibility = View.VISIBLE

            signInWithEmailAndPassword()
        }
    }

    private fun signInWithEmailAndPassword() {
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener { authResult ->
            val uid = authResult.user?.uid
            if (uid != null) {

                FirebaseDatabase.getInstance()
                    .getReference("Current Patients")
                    .child(uid)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                binding.progressBar6.visibility = View.GONE

                                val intent = Intent(
                                    this@PatientLogin,
                                    PatientDashboard::class.java
                                )
                                startActivity(intent)

                                binding.edittext15.setText("")
                                binding.edittext16.setText("")
                            } else {
                                val alertDialog = AlertDialog.Builder(this@PatientLogin)
                                alertDialog.apply {
                                    setTitle("Error")
                                    setMessage("This account does not exist. Ensure you have a patient account to login.")
                                    setCancelable(false)
                                    setPositiveButton("Register") { dialog, _ ->
                                        startActivity(
                                            Intent(
                                                this@PatientLogin,
                                                PatientRegistration::class.java
                                            )
                                        )
                                        finish()
                                        dialog.dismiss()
                                    }
                                    setNegativeButton("Cancel") { dialog, _ ->
                                        startActivity(Intent(this@PatientLogin, Login::class.java))
                                        finish()
                                        dialog.dismiss()
                                    }
                                    create()
                                    show()
                                }
                            }

                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    })
            }

        }
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    binding.progressBar6.visibility = View.GONE
                    Toast.makeText(this@PatientLogin , task.exception?.localizedMessage , Toast.LENGTH_LONG).show()
                }
            }
    }
}



