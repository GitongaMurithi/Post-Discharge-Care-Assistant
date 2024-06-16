package com.example.patientpost_discharge.doctor

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.patientpost_discharge.databinding.ActivityConfirmDismissalBinding
import com.google.firebase.database.FirebaseDatabase

class ConfirmDismissal : AppCompatActivity() {
    private lateinit var binding: ActivityConfirmDismissalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmDismissalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra("name").toString()
        val uid = intent.getStringExtra("uid").toString()

        val sharedPreferences = getSharedPreferences("Prefs" , MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("name" , name)
        editor.apply()

        val sharedPreferences1 = getSharedPreferences("MyPrefs" , Context.MODE_PRIVATE)
        val editor1 = sharedPreferences1.edit()
        editor1.putString("uid" , uid)
        editor1.apply()

        binding.cancel.setOnClickListener {
            startActivity(Intent(this@ConfirmDismissal , CurrentPatients::class.java))
            finish()
        }

        binding.confirm.setOnClickListener {
            FirebaseDatabase.getInstance().getReference("Current Patients").child(uid).child("discharged").setValue(true)
                .addOnCompleteListener {  task ->
                    if (task.isSuccessful) {
                        val intent = Intent(this@ConfirmDismissal , AssignCHP::class.java)
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                        finish()
                    }
                }

        }
    }
}