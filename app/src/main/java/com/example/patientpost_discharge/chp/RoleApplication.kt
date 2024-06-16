package com.example.patientpost_discharge.chp

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.patientpost_discharge.databinding.ActivityRoleApplicationBinding
import com.example.patientpost_discharge.models.HealthWorker
import com.example.patientpost_discharge.models.JobApplication
import com.google.firebase.database.FirebaseDatabase

class RoleApplication : AppCompatActivity() {
    private lateinit var binding: ActivityRoleApplicationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoleApplicationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val chp = intent.getSerializableExtra("chp") as? HealthWorker
        binding.editText2.setText(chp?.name)
        binding.editText3.setText(chp?.email)
        binding.editText4.setText(chp?.location)

        binding.attach.setOnClickListener {
            activityResultLauncher.launch("application/pdf")
        }

        binding.confirm.setOnClickListener {
            registerCHP()
        }

        binding.cancel.setOnClickListener {
            binding.progressBar3.visibility = View.GONE
            binding.editText2.setText("")
            binding.editText3.setText("")
            binding.editText4.setText("")
            binding.editText5.setText("")
            binding.editText6.setText("")
            binding.editText2.requestFocus()

        }
    }

    private fun registerCHP() {
        val experience = binding.editText5.text.toString()
        val expertise = binding.editText6.text.toString()
        val name = binding.editText2.text.toString()
        val email = binding.editText3.text.toString()
        val residence = binding.editText4.text.toString()

        if (name.isEmpty()) {
            binding.editText2.error = "This is a required field!"
            binding.editText2.requestFocus()
            return
        }
        if (email.isEmpty()) {
            binding.editText3.error = "This is a required field!"
            binding.editText3.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.editText3.error = "Enter a valid email address"
            binding.editText3.requestFocus()
            return
        }
        if (residence.isEmpty()) {
            binding.editText4.error = "This is a required field!"
            binding.editText4.requestFocus()
            return
        }
        if (experience.isEmpty()) {
            binding.editText5.error = "This is a required field!"
            binding.editText5.requestFocus()
            return
        }
        if (expertise.isEmpty()) {
            binding.editText6.error = "This is a required field!"
            binding.editText6.requestFocus()
            return
        }
        if (binding.chosenFile.text == "No file chosen") {
            Toast.makeText(this@RoleApplication, "Attach your CV in PDF format", Toast.LENGTH_LONG)
                .show()
            return
        }

        binding.progressBar3.visibility = View.VISIBLE
        val application = JobApplication(
            name = name,
            email = email,
            residence = residence,
            experience = experience.toInt(),
            expertise = expertise,
//            bio = bio
        )
        FirebaseDatabase.getInstance().getReference("Applications").child(name)
            .setValue(application)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this@RoleApplication,
                        "Applied successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.progressBar3.visibility = View.GONE
                    binding.editText2.setText("")
                    binding.editText3.setText("")
                    binding.editText4.setText("")
                    binding.editText5.setText("")
                    binding.editText6.setText("")
                    startActivity(
                        Intent(
                            this@RoleApplication,
                            HealthWorkerDashboard::class.java
                        )
                    )
                    finish()

                } else {
                    Toast.makeText(
                        this@RoleApplication,
                        task.exception?.localizedMessage,
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.progressBar3.visibility = View.GONE
                }
            }
    }

    private val activityResultLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            val fileName = getFileName(applicationContext, uri)
            binding.chosenFile.text = fileName

        } else {
            Toast.makeText(this@RoleApplication , "Null uri" , Toast.LENGTH_SHORT).show()
        }

    }

    private fun getFileName(context: Context, fileUri: Uri?): String? {
        var fileName: String? = null
        val cursor = fileUri?.let { context.contentResolver.query(it, null, null, null, null) }
        cursor?.use {
            if (it.moveToFirst()) {
                val displayNameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (displayNameIndex != -1) {
                    fileName = it.getString(displayNameIndex)
                }
            }
        }
        return fileName
    }

}