package com.example.patientpost_discharge.common

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.patientpost_discharge.chp.HealthWorkerLogin
import com.example.patientpost_discharge.databinding.ActivityLoginBinding
import com.example.patientpost_discharge.doctor.DoctorLogin
import com.example.patientpost_discharge.patient.PatientLogin

class Login : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var binding: ActivityLoginBinding
    private val options = arrayOf("Select", "Doctor", "Patient", "Health provider")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginSpinner.onItemSelectedListener = this
        val adapter = ArrayAdapter(this@Login, android.R.layout.simple_spinner_dropdown_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.loginSpinner.adapter = adapter

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val selected = binding.loginSpinner.selectedItem.toString()
        if (selected == "Select") {
            Toast.makeText(this, "Select login mode!", Toast.LENGTH_SHORT).show()
            return
        }
        if (selected == "Doctor") {
            startActivity(Intent(this@Login , DoctorLogin::class.java))
            finish()
            return
        }
        if (selected == "Patient") {
            startActivity(Intent(this@Login , PatientLogin::class.java))
            finish()
            return
        }
        if (selected == "Health provider") {
            startActivity(Intent(this@Login , HealthWorkerLogin::class.java))
            finish()
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}
