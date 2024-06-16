package com.example.patientpost_discharge.chp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.patientpost_discharge.adapters.ProgressDetailsAdapter
import com.example.patientpost_discharge.databinding.ActivityViewAssignmentsBinding
import com.example.patientpost_discharge.models.CurrentPatients
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ViewAssignments : AppCompatActivity() {
    private lateinit var binding: ActivityViewAssignmentsBinding
    private lateinit var patients: ArrayList<CurrentPatients>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewAssignmentsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        patients = ArrayList()
        binding.recyclerView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = layoutManager

        binding.searchView.setOnQueryTextListener(object :
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchAssignment(newText)
                return true
            }

        })

        FirebaseDatabase.getInstance().getReference("Current Patients")
            .addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    patients.clear()
                    snapshot.children.forEach { child ->
                        val name = child.child("name").value.toString()
                        val age = child.child("age").value.toString()
                        val residence = child.child("residence").value.toString()
                        val phone = child.child("phone").value.toString()
                        val diagnosis = child.child("diagnosis").value.toString()
                        val email = child.child("email").value.toString()
                        val test = child.child("test").value.toString()
                        val uid = child.child("uid").value.toString()
                        val chpEmail = child.child("chpEmail").value.toString()
                        val discharged = child.child("discharged").value as Boolean

                        val currentPatient = CurrentPatients(
                            name = name,
                            age = age,
                            residence = residence,
                            phone = phone,
                            diagnosis = diagnosis,
                            email = email,
                            test = test,
                            uid = uid,
                            chpEmail = chpEmail,
                            discharged = discharged
                        )

                        if (discharged && chpEmail == FirebaseAuth.getInstance().currentUser!!.email) {
                            patients.add(currentPatient)
                        }
                    }
                    binding.recyclerView.adapter =
                        ProgressDetailsAdapter(this@ViewAssignments, patients)
                    binding.assignmentsProgress.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }

    private fun searchAssignment(query: String?) {
        val filteredAssignments = patients.filter { currentPatient ->
            currentPatient.name.contains(query.orEmpty(), ignoreCase = true)
        }
        binding.recyclerView.adapter = ProgressDetailsAdapter(
            this@ViewAssignments,
            filteredAssignments as ArrayList<CurrentPatients>,
        )
    }
}