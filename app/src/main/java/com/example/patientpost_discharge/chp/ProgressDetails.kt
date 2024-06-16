package com.example.patientpost_discharge.chp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.patientpost_discharge.databinding.ActivityProgressDetailsBinding
import com.example.patientpost_discharge.models.FinalDetails
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProgressDetails : AppCompatActivity() {
    private lateinit var binding : ActivityProgressDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProgressDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name  = intent.getStringExtra("name").toString()
        val age  = intent.getStringExtra("age")
        val residence  = intent.getStringExtra("residence")
        val phone  = intent.getStringExtra("phone")
        val diagnosis  = intent.getStringExtra("diagnosis")
        val uid  = intent.getStringExtra("uid")
        val test  = intent.getStringExtra("test")


        FirebaseDatabase.getInstance().getReference("Final details/$uid").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.medicineValue.text = snapshot.getValue(FinalDetails::class.java)!!.medicine
                binding.clinicValue.text = snapshot.getValue(FinalDetails::class.java)!!.clinic
                binding.recommendationsValue.text = snapshot.getValue(FinalDetails::class.java)!!.recommendation
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        binding.makeComment.setOnClickListener {
           startActivity(Intent(this@ProgressDetails , MakeComments::class.java))
            finish()
        }

        binding.patientName.text = name
        binding.ageValue.text = age
        binding.residenceValue.text = residence
        binding.phoneValue.text = phone
        binding.diagnosisValue.text = diagnosis
        binding.testValue.text = test
    }
}