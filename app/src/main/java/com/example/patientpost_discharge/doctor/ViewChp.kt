package com.example.patientpost_discharge.doctor

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.patientpost_discharge.databinding.ActivityViewChpBinding
import com.example.patientpost_discharge.models.HealthWorker
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ViewChp : AppCompatActivity() {

    private lateinit var binding: ActivityViewChpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewChpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showDialog()

        val uid = intent.getStringExtra("uid")

        FirebaseDatabase.getInstance().getReference("Health workers/$uid")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    binding.patientName.text = snapshot.getValue(HealthWorker::class.java)!!.name
                    binding.callValue.text = snapshot.getValue(HealthWorker::class.java)!!.phone
                    binding.emailValue.text = snapshot.getValue(HealthWorker::class.java)!!.email
                    binding.residenceValue.text =
                        snapshot.getValue(HealthWorker::class.java)!!.location

                    binding.callValue.setOnClickListener {
                        val intent = Intent(Intent.ACTION_DIAL)
                        intent.setData(Uri.parse("tel:" + snapshot.getValue(HealthWorker::class.java)!!.phone))
                        startActivity(intent)
                    }

                    binding.emailValue.setOnClickListener {
                        val emailIntent = Intent(Intent.ACTION_SEND)
                        emailIntent.setType("text/plain")
                        emailIntent.putExtra(
                            Intent.EXTRA_EMAIL,
                            arrayOf(snapshot.getValue(HealthWorker::class.java)!!.email)
                        )
                        startActivity(Intent.createChooser(emailIntent, "Send email"))
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

    }

    private fun showDialog() {
        val alertDialog = AlertDialog.Builder(this@ViewChp)
        alertDialog.setTitle("Info")
            .setMessage("Click phone or email to communicate with the chp")
            .setPositiveButton(
                "Okay"
            ) { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(true)
            .create()
            .show()
    }
}