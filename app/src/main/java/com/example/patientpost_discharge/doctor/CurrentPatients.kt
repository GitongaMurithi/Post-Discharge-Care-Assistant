package com.example.patientpost_discharge.doctor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.patientpost_discharge.adapters.CurrentPatientsAdapter
import com.example.patientpost_discharge.databinding.ActivityCurrentPatientsBinding
import com.example.patientpost_discharge.models.CurrentPatients
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CurrentPatients : AppCompatActivity() {
    private lateinit var binding: ActivityCurrentPatientsBinding
    private lateinit var patients: ArrayList<CurrentPatients>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCurrentPatientsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        patients = ArrayList()
        binding.patients.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.patients.layoutManager = layoutManager

        binding.searchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchPatients(newText)
                return true
            }

        })
        binding.add.setOnClickListener {
            startActivity(Intent(this@CurrentPatients, AddNewPatient::class.java))
        }
        FirebaseDatabase.getInstance().getReference("Current Patients")
            .addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    patients.clear()
                    snapshot.children.forEach { child ->
                        val name = child.child("name").value.toString()
                        val ageString = child.child("age").value?.toString()
                        val age =
                            if (!ageString.isNullOrBlank() && (ageString.toIntOrNull() != null)) {
                                ageString.toInt()
                            } else {
                                0
                            }
                        val residence = child.child("residence").value.toString()
                        val phone = child.child("phone").value.toString()
                        val diagnosis = child.child("diagnosis").value.toString()
                        val email = child.child("email").value.toString()
                        val uid = child.child("uid").value.toString()
                         val discharged = child.child("discharged").value as Boolean

                        val currentPatient = CurrentPatients(
                            name = name,
                            age = age.toString(),
                            residence = residence,
                            phone = phone,
                            diagnosis = diagnosis,
                            email = email,
                            uid = uid,
                            discharged = discharged
                        )
                        if (!discharged) {
                            patients.add(currentPatient)
                        }
                        binding.loading.visibility = View.GONE
                        binding.patients.visibility = View.VISIBLE
                    }
                    binding.patients.adapter =
                        CurrentPatientsAdapter(this@CurrentPatients, patients)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    private fun searchPatients(query: String?) {
        val filteredPatients = patients.filter { currentPatient ->
            currentPatient.name.contains(query.orEmpty() , ignoreCase = true)
        }
        binding.patients.adapter = CurrentPatientsAdapter(
            this@CurrentPatients,
            filteredPatients as ArrayList<CurrentPatients>
        )
    }
}