package com.example.patientpost_discharge.chp

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.patientpost_discharge.databinding.ActivityHealthWorkerRegistrationBinding
import com.example.patientpost_discharge.models.HealthWorker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class HealthWorkerRegistration : AppCompatActivity() {

    private lateinit var binding: ActivityHealthWorkerRegistrationBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var chp: HealthWorker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHealthWorkerRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        binding.button5.setOnClickListener {

            val name = binding.editText5.text.toString()
            val email = binding.editText7.text.toString()
            val phone = binding.editText6.text.toString()
            val password = binding.editText9.text.toString()
            val location = binding.editText8.text.toString()

            if (name.isEmpty()) {
                binding.editText5.error = "This is a required field!"
                binding.editText5.requestFocus()
                return@setOnClickListener
            }
            if (phone.isEmpty()) {
                binding.editText6.error = "This is a required field!"
                binding.editText6.requestFocus()
                return@setOnClickListener
            }
            if (email.isEmpty()) {
                binding.editText7.error = "This is a required field!"
                binding.editText7.requestFocus()
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.editText7.error = "Enter a valid email address!"
                binding.editText7.requestFocus()
                return@setOnClickListener
            }
            if (password.isEmpty() || password.length < 4) {
                binding.editText9.error = "Enter password of at least 4 characters!"
                binding.editText9.requestFocus()
                return@setOnClickListener
            }
            if (location.isEmpty()) {
                binding.editText8.error = "This is a required field!"
                binding.editText8.requestFocus()
                return@setOnClickListener
            }
            binding.progressBar3.visibility = View.VISIBLE

            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { authResult ->
                    val uid = authResult.user!!.uid
                    chp = HealthWorker(
                        name = name,
                        email = email,
                        phone = phone,
                        password = password,
                        location = location,
                        uid = uid
                    )
                    saveChpInDatabase()

                }
        }
    }

    private fun saveChpInDatabase() {
        FirebaseDatabase.getInstance().getReference("Health workers")
            .child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(chp)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    startActivity(
                        Intent(
                            this@HealthWorkerRegistration,
                            HealthWorkerLogin::class.java
                        )
                    )
                    binding.progressBar3.visibility = View.GONE
                    binding.editText5.setText("")
                    binding.editText7.setText("")
                    binding.editText6.setText("")
                    binding.editText8.setText("")
                    binding.editText9.setText("")

                } else {
                    Toast.makeText(
                        this@HealthWorkerRegistration,
                        task.exception?.localizedMessage,
                        Toast.LENGTH_LONG
                    ).show()
                    binding.progressBar3.visibility = View.GONE
                }
            }
    }
}