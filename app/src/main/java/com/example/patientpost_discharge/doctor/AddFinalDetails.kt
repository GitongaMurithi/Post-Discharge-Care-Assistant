package com.example.patientpost_discharge.doctor

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.patientpost_discharge.databinding.ActivityAddFinalDetailsBinding
import com.example.patientpost_discharge.models.FinalDetails
import com.google.firebase.database.FirebaseDatabase
import java.util.Calendar

class AddFinalDetails : AppCompatActivity() {

    private lateinit var binding: ActivityAddFinalDetailsBinding
    private lateinit var uid: String
    private lateinit var name: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddFinalDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        uid = intent.getStringExtra("uid").toString()
        name = intent.getStringExtra("name").toString()

        binding.datePicker.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this@AddFinalDetails,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                    binding.nextClinicValue.text = selectedDate
                }, year, month, day
            )
            datePickerDialog.datePicker.minDate = System.currentTimeMillis()
            datePickerDialog.show()
        }

        binding.upload.setOnClickListener {
            uploadDetails()
        }
    }

    private fun uploadDetails() {
        if (binding.editText1.text.toString().isEmpty()) {
            binding.editText1.error = "This is a required field!"
            binding.editText1.requestFocus()
            return
        }
        if (binding.editText3.text.toString().isEmpty()) {
            binding.editText3.error = "This is a required field!"
            binding.editText3.requestFocus()
            return
        }
        if (binding.nextClinicValue.text == "") {
            Toast.makeText(this@AddFinalDetails, "Select a date", Toast.LENGTH_SHORT).show()
            return
        }
        binding.finalDetailsProgress.visibility = View.VISIBLE
        FirebaseDatabase.getInstance().getReference("Final details").child(uid)
            .setValue(
                FinalDetails(
                    binding.editText1.text.toString(),
                    binding.nextClinicValue.text.toString(),
                    binding.editText3.text.toString(),
                )
            ).addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    binding.finalDetailsProgress.visibility = View.GONE
                    startActivity(
                        Intent(this@AddFinalDetails, ConfirmDismissal::class.java)
                            .putExtra("name", name)
                            .putExtra("uid", uid)
                    )
                    binding.editText1.setText("")
                    binding.nextClinicValue.text = ""
                    binding.editText3.setText("")
                } else {
                    binding.finalDetailsProgress.visibility = View.GONE
                    Toast.makeText(
                        this@AddFinalDetails,
                        task.exception?.localizedMessage,
                        Toast.LENGTH_LONG
                    ).show()
                }

            }
    }

//    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
//        val calendar = Calendar.getInstance()
//        calendar.set(Calendar.YEAR, year)
//        calendar.set(Calendar.MONTH, month)
//        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
//
//        val selectedDate = DateFormat.getDateInstance().format(calendar.time)
//        binding.nextClinicValue.text = selectedDate
//    }
}