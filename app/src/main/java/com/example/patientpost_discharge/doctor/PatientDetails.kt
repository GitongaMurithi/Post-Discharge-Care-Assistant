package com.example.patientpost_discharge.doctor

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.patientpost_discharge.databinding.ActivityPatientDetailsBinding
import com.example.patientpost_discharge.models.CurrentPatients
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PatientDetails : AppCompatActivity() {
    private lateinit var binding: ActivityPatientDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPatientDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra("name").toString()
        val age = intent.getStringExtra("age")
        val residence = intent.getStringExtra("residence")
        val phone = intent.getStringExtra("phone")
        val diagnosis = intent.getStringExtra("diagnosis")
        val email = intent.getStringExtra("email")
        val uid = intent.getStringExtra("uid")

        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("email", email)
        editor.apply()

        binding.edit.setOnClickListener {
            val intent = Intent(this@PatientDetails, AddNewPatient::class.java)
            intent.putExtra("uid", uid)
            intent.putExtra("editing", true)
            startActivity(intent)
        }
        binding.discharge.setOnClickListener {

            if (uid != null) {
                FirebaseDatabase.getInstance().getReference("Current Patients/$uid")
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val userId = snapshot.getValue(CurrentPatients::class.java)?.uid
                            val test = snapshot.getValue(CurrentPatients::class.java)?.test
                            if (userId != null || test != null) {
                                val intent = Intent(this@PatientDetails, AddFinalDetails::class.java)
                                intent.putExtra("uid", uid)
                                intent.putExtra("name", name)
                                startActivity(intent)
                            } else {
                                Toast.makeText(
                                    this@PatientDetails,
                                    "Please update patient details first",
                                    Toast.LENGTH_LONG
                                ).show()
                                val intent = Intent(this@PatientDetails, AddNewPatient::class.java)
                                intent.putExtra("uid", uid)
                                intent.putExtra("editing", true)
                                startActivity(intent)
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    })
            } else {
                FirebaseDatabase.getInstance().getReference("Current Patients").orderByChild("email").equalTo(email)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (dataSnapshot in snapshot.children) {

                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    })
            }
        }

        binding.patientName.text = name
        binding.ageValue.text = age
        binding.residenceValue.text = residence
        binding.phoneValue.text = phone
        binding.diagnosisValue.text = diagnosis
    }
}