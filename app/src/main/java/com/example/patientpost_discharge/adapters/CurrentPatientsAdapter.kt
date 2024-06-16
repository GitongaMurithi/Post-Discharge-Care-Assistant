package com.example.patientpost_discharge.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.patientpost_discharge.R
import com.example.patientpost_discharge.doctor.PatientDetails
import com.example.patientpost_discharge.models.CurrentPatients
import javax.inject.Inject

class CurrentPatientsAdapter @Inject constructor(
    private val context: Context,
    private val patients: ArrayList<CurrentPatients>
) : RecyclerView.Adapter<CurrentPatientsAdapter.PatientsViewHolder>() {

    inner class PatientsViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val patient : TextView = itemView.findViewById(R.id.my_assignment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientsViewHolder {
        return PatientsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.patient_item , parent , false))
    }

    override fun getItemCount(): Int {
        return patients.size
    }

    override fun onBindViewHolder(holder: PatientsViewHolder, position: Int) {
        val currentPatient = patients[position]
        holder.patient.text = currentPatient.name
        holder.itemView.setOnClickListener {
            val intent = Intent(context , PatientDetails::class.java)
            intent.putExtra("name" , currentPatient.name)
            intent.putExtra("age" , currentPatient.age)
            intent.putExtra("residence" , currentPatient.residence)
            intent.putExtra("phone" , currentPatient.phone)
            intent.putExtra("diagnosis" , currentPatient.diagnosis)
            intent.putExtra("email" , currentPatient.email)
            intent.putExtra("uid" , currentPatient.uid)
            context.startActivity(intent)
        }
    }
}