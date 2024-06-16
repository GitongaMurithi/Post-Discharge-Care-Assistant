package com.example.patientpost_discharge.doctor

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.patientpost_discharge.R
import com.example.patientpost_discharge.databinding.ActivityAddNewPatientBinding
import com.example.patientpost_discharge.models.CurrentPatients
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AddNewPatient : AppCompatActivity() {
    private lateinit var binding: ActivityAddNewPatientBinding
    private lateinit var name: String
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var uid: String
    private lateinit var age: String
    private lateinit var residence: String
    private lateinit var phone: String
    private lateinit var diagnosis: String
    private lateinit var test: String
    private lateinit var userId: String
    private lateinit var currentPatient: CurrentPatients
    private  var editing : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewPatientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        uid = intent.getStringExtra("uid").toString()

         editing = intent.getBooleanExtra("editing", false)

        if (!editing) {
            binding.textField.visibility = View.VISIBLE
            binding.upload.setOnClickListener {
                validateInput()
            }
        } else {
            binding.editText9.visibility = View.VISIBLE
            populateFields()
            binding.addNew.text = getString(R.string.edit_patient_details)
            binding.upload.setOnClickListener {
                validateInput()
            }
        }
    }

    private fun updatePatient() {
        binding.progressBar3.visibility = View.VISIBLE
        FirebaseDatabase.getInstance().getReference("Current Patients").child(uid)
            .child("age").setValue(age)
        FirebaseDatabase.getInstance().getReference("Current Patients").child(uid)
            .child("test").setValue(test)
        FirebaseDatabase.getInstance().getReference("Current Patients").child(uid)
            .child("residence").setValue(residence)
        FirebaseDatabase.getInstance().getReference("Current Patients").child(uid)
            .child("diagnosis").setValue(diagnosis)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this@AddNewPatient,
                        "Details updated",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.editText1.setText("")
                    binding.editText2.setText("")
                    binding.editText3.setText("")
                    binding.editText4.setText("")
                    binding.editText5.setText("")
                    binding.editText6.setText("")
                    binding.editText7.setText("")

                    startActivity(
                        Intent(
                            this@AddNewPatient,
                            com.example.patientpost_discharge.doctor.CurrentPatients::class.java
                        )
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    )
                    finish()
                } else {
                    binding.progressBar3.visibility = View.VISIBLE
                    Toast.makeText(
                        this@AddNewPatient,
                        task.exception?.localizedMessage,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun savePatientInDatabase() {
        FirebaseDatabase.getInstance().getReference("Current Patients")
            .child(userId)
            .setValue(currentPatient)
            .addOnCompleteListener { task1 ->
                if (task1.isSuccessful) {
                    Toast.makeText(
                        this@AddNewPatient,
                        "Details saved successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.editText1.setText("")
                    binding.editText2.setText("")
                    binding.editText3.setText("")
                    binding.editText4.setText("")
                    binding.editText5.setText("")
                    binding.editText6.setText("")
                    binding.editText7.setText("")
                    binding.editText8.setText("")

                    startActivity(
                        Intent(
                            this@AddNewPatient,
                            com.example.patientpost_discharge.doctor.CurrentPatients::class.java
                        )
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    )
                    finish()
                } else {
                    Toast.makeText(
                        this@AddNewPatient,
                        task1.exception?.localizedMessage,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun populateFields() {
        FirebaseDatabase.getInstance().getReference("Current Patients/$uid")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    binding.editText1.setText(snapshot.getValue(CurrentPatients::class.java)!!.name)
                    binding.editText7.setText(snapshot.getValue(CurrentPatients::class.java)!!.email)
                    binding.editText2.setText(snapshot.getValue(CurrentPatients::class.java)!!.age)
                    binding.editText3.setText(snapshot.getValue(CurrentPatients::class.java)!!.residence)
                    binding.editText4.setText(snapshot.getValue(CurrentPatients::class.java)!!.phone)
                    binding.editText6.setText(snapshot.getValue(CurrentPatients::class.java)!!.test)
                    binding.editText5.setText(snapshot.getValue(CurrentPatients::class.java)!!.diagnosis)
                    binding.editText8.setText(snapshot.getValue(CurrentPatients::class.java)!!.password)
                    binding.editText9.setText(snapshot.getValue(CurrentPatients::class.java)!!.password)

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    private fun validateInput() {
        name = binding.editText1.text.toString()
        email = binding.editText7.text.toString()
        age = binding.editText2.text.toString()
        residence = binding.editText3.text.toString()
        phone = binding.editText4.text.toString()
        diagnosis = binding.editText5.text.toString()
        test = binding.editText6.text.toString()
        password = binding.editText8.text.toString()

        if (name.isEmpty()) {
            binding.editText1.error = "This is a required field!"
            binding.editText1.requestFocus()
            return
        }

        if (email.isEmpty()) {
            binding.editText7.error = "This is a required field!"
            binding.editText7.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.editText1.error = "Enter a valid email address!"
            binding.editText1.requestFocus()
            return
        }

        if (password.isEmpty()) {
            binding.editText8.error = "This is a required field!"
            binding.editText8.requestFocus()
            return
        }

        if (age.isEmpty()) {
            binding.editText2.error = "This is a required field!"
            binding.editText2.requestFocus()
            return
        }

        if (residence.isEmpty()) {
            binding.editText3.error = "This is a required field!"
            binding.editText3.requestFocus()
            return
        }

        if (phone.isEmpty()) {
            binding.editText4.error = "This is a required field!"
            binding.editText4.requestFocus()
            return
        }

        if (diagnosis.isEmpty()) {
            binding.editText5.error = "This is a required field!"
            binding.editText5.requestFocus()
            return
        }

        if (test.isEmpty()) {
            binding.editText6.error = "This is a required field!"
            binding.editText6.requestFocus()
            return
        }
        if (!editing) {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { authResult ->
                    userId = authResult.user!!.uid
                    currentPatient = CurrentPatients(
                        name = name,
                        age = age,
                        residence = residence,
                        phone = phone,
                        diagnosis = diagnosis,
                        email = email,
                        password = password,
                        test = test,
                        uid = userId,
                        discharged = false
                    )
                    savePatientInDatabase()
                }
        } else {
            updatePatient()
        }
    }
}