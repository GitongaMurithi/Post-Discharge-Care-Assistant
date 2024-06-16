package com.example.patientpost_discharge.chp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.patientpost_discharge.databinding.ActivityMakeCommentsBinding
import com.google.firebase.database.FirebaseDatabase

class MakeComments : AppCompatActivity() {
    private lateinit var binding: ActivityMakeCommentsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMakeCommentsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val uid = sharedPreferences.getString("uid", null)

        binding.done.setOnClickListener {
            val comment = binding.progress.text.toString().trim()

            if (comment.isEmpty()) {
                binding.progress.error = "This is a required field!"
                binding.progress.requestFocus()
                return@setOnClickListener
            }
            binding.progressBar3.visibility = View.VISIBLE
            FirebaseDatabase.getInstance().getReference("Final details/$uid").child("progress")
                .setValue(binding.progress.text.toString().trim())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        binding.progressBar3.visibility = View.GONE
                        Toast.makeText(this@MakeComments, "Submitted", Toast.LENGTH_SHORT).show()
                        binding.progress.setText("")
                        startActivity(Intent(this@MakeComments, ViewAssignments::class.java))
                        finish()
                    } else {
                        Toast.makeText(
                            this@MakeComments,
                            task.exception?.localizedMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }
}