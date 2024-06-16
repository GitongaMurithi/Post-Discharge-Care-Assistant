package com.example.patientpost_discharge.chp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.patientpost_discharge.databinding.ActivityPatientDetails2Binding

class PatientDetails : AppCompatActivity() {
    private lateinit var binding : ActivityPatientDetails2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPatientDetails2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.comment.setOnClickListener {
            startActivity(Intent(this@PatientDetails , MakeComments::class.java))
        }
        val name  = intent.getStringExtra("name")
        val age  = intent.getStringExtra("age")
        val residence  = intent.getStringExtra("residence")
        val phone  = intent.getStringExtra("phone")
        val diagnosis  = intent.getStringExtra("diagnosis")

        binding.patientName.text = name
        binding.ageValue.text = age
        binding.residenceValue.text = residence
        binding.phoneValue.text = phone
        binding.diagnosisValue.text = diagnosis
    }
}