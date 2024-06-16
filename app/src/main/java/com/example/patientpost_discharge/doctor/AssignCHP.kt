package com.example.patientpost_discharge.doctor

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.patientpost_discharge.adapters.AssignCHPAdapter
import com.example.patientpost_discharge.databinding.ActivityAssignChpBinding
import com.example.patientpost_discharge.models.HealthWorker
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AssignCHP : AppCompatActivity() {
    private lateinit var binding : ActivityAssignChpBinding
    private lateinit var chp : ArrayList<HealthWorker>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAssignChpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        chp = ArrayList()
        binding.recycler.setHasFixedSize(false)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recycler.layoutManager = layoutManager

        binding.searchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchChp(newText)
                return true
            }

        })
        FirebaseDatabase.getInstance().getReference("Health workers").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                chp.clear()
                snapshot.children.forEach {
                    val name = it.child("name").value.toString()
                    val location = it.child("location").value.toString()
                    val email = it.child("email").value.toString()
                    val uid = it.child("uid").value.toString()

                    val worker = HealthWorker(name = name , location = location , email = email , uid = uid)
                    chp.add(worker)
                }
                binding.recycler.adapter = AssignCHPAdapter(this@AssignCHP , chp)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun searchChp(query: String?) {
        val filteredChp = chp.filter {  healthWorker ->
            healthWorker.location.contains(query.orEmpty() , ignoreCase = true)
        }
        binding.recycler.adapter = AssignCHPAdapter(this@AssignCHP , filteredChp as ArrayList<HealthWorker>)
    }
}