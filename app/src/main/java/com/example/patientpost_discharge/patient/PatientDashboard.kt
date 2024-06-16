package com.example.patientpost_discharge.patient

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.patientpost_discharge.databinding.ActivityPatientDashboardBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PatientDashboard : AppCompatActivity() {
    private lateinit var binding: ActivityPatientDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPatientDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.card3.setOnClickListener {
            binding.card3.visibility = View.GONE
            binding.waiting.visibility = View.VISIBLE
            FirebaseDatabase.getInstance().getReference("Current Patients")
                .child(FirebaseAuth.getInstance().currentUser!!.uid)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val patient =
                            snapshot.getValue(com.example.patientpost_discharge.models.CurrentPatients::class.java)
                        if (patient != null) {
                            val intent = Intent(this@PatientDashboard, CurrentDetails::class.java)
                            intent.putExtra("patient", patient)
                            startActivity(intent)
                            binding.waiting.visibility = View.GONE
                            finish()
                        } else {
                            val alertDialog = AlertDialog.Builder(this@PatientDashboard)
                            alertDialog.apply {
                                setTitle("Error")
                                setMessage("User data not found!")
                                setCancelable(false)
                                setPositiveButton(
                                    "OK"
                                ) { dialog, _ ->
                                    dialog.dismiss()
                                    startActivity(Intent(this@PatientDashboard , PatientDashboard::class.java))
                                    finish()
                                }
                                create()
                                show()
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })
        }
    }
}

















