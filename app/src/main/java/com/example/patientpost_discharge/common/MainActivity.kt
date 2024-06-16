package com.example.patientpost_discharge.common

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.patientpost_discharge.chp.HealthWorkerRegistration
import com.example.patientpost_discharge.databinding.ActivityMainBinding
import com.example.patientpost_discharge.doctor.DoctorRegistration
import com.example.patientpost_discharge.patient.PatientRegistration

class MainActivity : AppCompatActivity() , AdapterView.OnItemSelectedListener {

    private lateinit var binding : ActivityMainBinding
    private val options = arrayOf("Select", "Doctor", "Patient", "Health provider")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.register.onItemSelectedListener = this
        val adapter = ArrayAdapter(this@MainActivity,android.R.layout.simple_spinner_dropdown_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.register.adapter = adapter

        binding.button1.setOnClickListener {
            startActivity(Intent(this@MainActivity , Login::class.java))
        }
    }


    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val selected = binding.register.selectedItem.toString()
        if (selected == "Select") {
            Toast.makeText(this@MainActivity, "Select registration mode!", Toast.LENGTH_SHORT).show()
            return
        }
        if (selected == "Doctor") {
            startActivity(Intent(this@MainActivity , DoctorRegistration::class.java))
            return
        }
        if (selected == "Patient") {
            startActivity(Intent(this@MainActivity , PatientRegistration::class.java))
            return
        }
        if (selected == "Health provider") {
            startActivity(Intent(this@MainActivity , HealthWorkerRegistration::class.java))
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}