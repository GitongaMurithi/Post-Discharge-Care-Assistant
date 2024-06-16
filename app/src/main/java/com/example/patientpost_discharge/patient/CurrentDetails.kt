package com.example.patientpost_discharge.patient

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.patientpost_discharge.databinding.ActivityCurrentDetailsBinding
import com.example.patientpost_discharge.models.CurrentPatients
import com.example.patientpost_discharge.models.FinalDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CurrentDetails : AppCompatActivity() {
    private lateinit var binding : ActivityCurrentDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCurrentDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val patient = intent.getSerializableExtra("patient") as? CurrentPatients

        val name = patient?.name
        val age = patient?.age.toString()
        val residence = patient?.residence
        val diagnosis = patient?.diagnosis
        val phone = patient?.phone

        if (age == "") {
            binding.notYet.visibility = View.VISIBLE
        }

        FirebaseDatabase.getInstance().getReference("Final details/${FirebaseAuth.getInstance().currentUser!!.uid}")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    binding.clinicValue.text =
                        snapshot.getValue(FinalDetails::class.java)?.clinic ?: ""
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        binding.phoneValue.text = phone
        binding.diagnosisValue.text = diagnosis
        binding.residenceValue.text = residence
        binding.patientName.text = name
        binding.ageValue.text = age
    }
}