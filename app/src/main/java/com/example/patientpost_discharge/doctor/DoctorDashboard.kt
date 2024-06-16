package com.example.patientpost_discharge.doctor

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.patientpost_discharge.databinding.ActivityDoctorDashboardBinding

class DoctorDashboard : AppCompatActivity() {
    private lateinit var binding : ActivityDoctorDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.card1.setOnClickListener {
            startActivity(Intent(this@DoctorDashboard , CurrentPatients::class.java))
        }

        binding.card2.setOnClickListener {
            startActivity(Intent(this@DoctorDashboard , DischargedPatients::class.java))
        }

    }
}