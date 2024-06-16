package com.example.patientpost_discharge.patient

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.patientpost_discharge.databinding.ActivityPatientRegistrationBinding
import com.example.patientpost_discharge.models.CurrentPatients
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class PatientRegistration : AppCompatActivity() {

    private lateinit var binding: ActivityPatientRegistrationBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPatientRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        binding.button4.setOnClickListener {

            val name = binding.editText1.text.toString().trim()
            val email = binding.editText3.text.toString().trim()
            val phone = binding.editText2.text.toString().trim()
            val password = binding.editText4.text.toString().trim()

            if (name.isEmpty()) {
                binding.editText1.error = "Enter your name!"
                binding.editText1.requestFocus()
                return@setOnClickListener
            }
            if (phone.isEmpty()) {
                binding.editText2.error = "Enter your phone number!"
                binding.editText2.requestFocus()
                return@setOnClickListener
            }
            if (email.isEmpty()) {
                binding.editText3.error = "Enter your email!"
                binding.editText3.requestFocus()
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.editText3.error = "Enter a valid email address!"
                binding.editText3.requestFocus()
                return@setOnClickListener
            }
            if (password.isEmpty() || password.length < 4) {
                binding.editText4.error = "Enter password of at least 4 characters!"
                binding.editText4.requestFocus()
                return@setOnClickListener
            }
            binding.progressBar2.visibility = View.VISIBLE
            auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener { authResult ->
                val uid = authResult.user?.uid

                    val patient =
                        CurrentPatients(name = name, phone = phone, email = email, password = password , uid = uid!!)
                    FirebaseDatabase.getInstance().getReference("Current Patients")
                        .child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(patient)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {

                                val intent = Intent(this@PatientRegistration, PatientLogin::class.java)
//                                intent.putExtra("name" , name)
//                                intent.putExtra("phone" , phone)
                                startActivity(intent)
                                binding.progressBar2.visibility = View.GONE
                                binding.editText1.setText("")
                                binding.editText2.setText("")
                                binding.editText3.setText("")
                                binding.editText4.setText("")

                            } else {
                                Toast.makeText(
                                    this@PatientRegistration,
                                    it.exception?.localizedMessage,
                                    Toast.LENGTH_LONG
                                ).show()
                                binding.progressBar2.visibility = View.GONE
                            }
                        }
            }.addOnFailureListener { p0 ->
                Toast.makeText(
                    this@PatientRegistration,
                    p0.localizedMessage,
                    Toast.LENGTH_LONG
                ).show()
                binding.progressBar2.visibility = View.GONE
            }
        }
    }
}