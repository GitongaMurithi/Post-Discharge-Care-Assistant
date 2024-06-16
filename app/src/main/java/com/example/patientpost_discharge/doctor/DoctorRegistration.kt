package com.example.patientpost_discharge.doctor

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.patientpost_discharge.databinding.ActivityDoctorRegistrationBinding
import com.example.patientpost_discharge.models.Doctor
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class DoctorRegistration : AppCompatActivity() {

    private lateinit var binding: ActivityDoctorRegistrationBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        binding.button5.setOnClickListener {

            val name = binding.editText5.text.toString().trim()
            val email = binding.editText7.text.toString().trim()
            val phone = binding.editText6.text.toString().trim()
            val password = binding.editText8.text.toString().trim()
            val specialization = binding.editText9.text.toString().trim()

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
            if (specialization.isEmpty()) {
                binding.editText9.error = "This is a required field!"
                binding.editText9.requestFocus()
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.editText7.error = "Enter a valid email address!"
                binding.editText7.requestFocus()
                return@setOnClickListener
            }
            if (password.isEmpty() || password.length < 4) {
                binding.editText8.error = "Enter password of at least 4 characters!"
                binding.editText8.requestFocus()
                return@setOnClickListener
            }
            binding.progressBar3.visibility = View.VISIBLE

            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val doctor =
                        Doctor(name = name, email = email, phone = phone, password = password , specialization = specialization)
                    FirebaseDatabase.getInstance().getReference("Doctors")
                        .child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(doctor)
                        .addOnCompleteListener { task1 ->
                            if (task1.isSuccessful) {

                                startActivity(
                                    Intent(
                                        this@DoctorRegistration,
                                        DoctorLogin::class.java
                                    )
                                )
                                binding.progressBar3.visibility = View.GONE
                                binding.editText5.setText("")
                                binding.editText6.setText("")
                                binding.editText7.setText("")
                                binding.editText8.setText("")
                                binding.editText9.setText("")

                            } else {
                                Toast.makeText(
                                    this@DoctorRegistration,
                                    task1.exception?.localizedMessage,
                                    Toast.LENGTH_LONG
                                ).show()
                                binding.progressBar3.visibility = View.GONE
                            }
                        }
                } else {
                    Toast.makeText(
                        this@DoctorRegistration,
                        task.exception?.localizedMessage,
                        Toast.LENGTH_LONG
                    ).show()
                    binding.progressBar3.visibility = View.GONE
                }
            }
        }
    }
}
