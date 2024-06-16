package com.example.patientpost_discharge.doctor

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.patientpost_discharge.databinding.ActivityDischargedDetailsBinding
import com.example.patientpost_discharge.models.FinalDetails
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.properties.HorizontalAlignment
import com.itextpdf.layout.properties.TextAlignment
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class DischargedDetails : AppCompatActivity() {
    private lateinit var binding: ActivityDischargedDetailsBinding
    private lateinit var name: String
    private lateinit var age: String
    private lateinit var residence: String
    private lateinit var diagnosis: String
    private lateinit var phone: String
    private lateinit var chp: String
    private lateinit var progress: String
    private lateinit var chpId: String

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDischargedDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        name = intent.getStringExtra("name").toString()
        age = intent.getStringExtra("age").toString()
        residence = intent.getStringExtra("residence").toString()
        phone = intent.getStringExtra("phone").toString()
        diagnosis = intent.getStringExtra("diagnosis").toString()
        val uid = intent.getStringExtra("uid")

        if (age == "0") {
            Toast.makeText(
                this@DischargedDetails,
                "Details are yet to be updated by the doctor",
                Toast.LENGTH_LONG
            ).show()
        }
        FirebaseDatabase.getInstance().getReference("Final details/$uid")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    chp = snapshot.getValue(FinalDetails::class.java)!!.assignedTo
                    progress = snapshot.getValue(FinalDetails::class.java)!!.progress
                    chpId = snapshot.getValue(FinalDetails::class.java)!!.chpUid

                    binding.assignedToValue.text = chp
                    binding.statusValue.text = progress
                    binding.patientName.text = name
                    binding.ageValue.text = age
                    binding.residenceValue.text = residence
                    binding.phoneValue.text = phone
                    binding.diagnosisValue.text = diagnosis


                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        binding.assignedToValue.setOnClickListener {
            startActivity(Intent(this@DischargedDetails , ViewChp::class.java)
                .putExtra("uid" , chpId)
            )
        }
        binding.save.setOnClickListener {

            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Permission not granted , request it
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    100
                )
            } else {
                // Permission already granted
                savePdfLocally(
                    name = name,
                    age = age,
                    residence = residence,
                    phone = phone,
                    diagnosis = diagnosis,
                    assignedTo = chp,
                    progress = progress
                )
            }


        }
    }

    private fun savePdfLocally(
        name: String,
        age: String,
        residence: String,
        phone: String,
        diagnosis: String,
        assignedTo: String,
        progress: String
    ) {
        val pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            .toString()
        val file = File(pdfPath, "Patient details.pdf")
        val outputStream = FileOutputStream(file)
        val pdfDocument = PdfDocument(PdfWriter(outputStream))
        val document = Document(pdfDocument)

        pdfDocument.defaultPageSize = PageSize.A4
        document.setMargins(20f, 0f, 20f, 0f)
        val paragraph = Paragraph("Patient details").setBold().setFontSize(24f)
            .setTextAlignment(TextAlignment.CENTER)
        val width = floatArrayOf(150f, 150f)
        val table = Table(width)
        table.setHorizontalAlignment(HorizontalAlignment.CENTER)

        table.addCell(Cell().add(Paragraph("Patient name")))
        table.addCell(Cell().add(Paragraph(name)))

        table.addCell(Cell().add(Paragraph("Status")))
        table.addCell(Cell().add(Paragraph("Discharged")))

        table.addCell(Cell().add(Paragraph("Patient age")))
        table.addCell(Cell().add(Paragraph(age)))

        table.addCell(Cell().add(Paragraph("Patient phone")))
        table.addCell(Cell().add(Paragraph(phone)))

        table.addCell(Cell().add(Paragraph("Patient residence")))
        table.addCell(Cell().add(Paragraph(residence)))

        table.addCell(Cell().add(Paragraph("CHP")))
        table.addCell(Cell().add(Paragraph(assignedTo)))

        table.addCell(Cell().add(Paragraph("Diagnosis")))
        table.addCell(Cell().add(Paragraph(diagnosis)))

        table.addCell(Cell().add(Paragraph("Progress")))
        table.addCell(Cell().add(Paragraph(progress)))

        val dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        table.addCell(Cell().add(Paragraph("Date")))
        table.addCell(Cell().add(Paragraph(LocalDate.now().format(dateTimeFormatter))))

        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss a")
        table.addCell(Cell().add(Paragraph("Time")))
        table.addCell(Cell().add(Paragraph(LocalTime.now().format(timeFormatter))))

        document.add(paragraph)
        document.add(table)

        document.close()

        Toast.makeText(this@DischargedDetails, "PDF saved", Toast.LENGTH_LONG).show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                savePdfLocally(
                    name = name,
                    age = age,
                    residence = residence,
                    phone = phone,
                    diagnosis = diagnosis,
                    assignedTo = chp,
                    progress = progress
                )
            } else {
                // Permission  denied
                Toast.makeText(
                    this,
                    "Allow the app to access your files in order to save the pdf file",
                    Toast.LENGTH_LONG
                )
                    .show()
            }
        }
    }

}