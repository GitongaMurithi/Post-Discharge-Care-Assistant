package com.example.patientpost_discharge.chp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.patientpost_discharge.databinding.ActivityHealthWorkerDashboardBinding
import com.example.patientpost_discharge.models.HealthWorker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HealthWorkerDashboard : AppCompatActivity() {
    private lateinit var binding: ActivityHealthWorkerDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHealthWorkerDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.card1.setOnClickListener {
            FirebaseDatabase.getInstance()
                .getReference("Health workers/" + FirebaseAuth.getInstance().currentUser!!.uid)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val chp = snapshot.getValue(HealthWorker::class.java)
                        if (chp != null) {
                            startActivity(
                                Intent(this@HealthWorkerDashboard, RoleApplication::class.java)
                                    .putExtra("chp", chp)
                            )
                        } else {
                            startActivity(
                                Intent(
                                    this@HealthWorkerDashboard,
                                    RoleApplication::class.java
                                )
                            )
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })
        }
        binding.card2.setOnClickListener {
            startActivity(
                Intent(this@HealthWorkerDashboard, ViewAssignments::class.java))
        }
    }

}