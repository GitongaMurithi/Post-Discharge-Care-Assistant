package com.example.patientpost_discharge.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.patientpost_discharge.R
import com.example.patientpost_discharge.chp.PatientDetails
import com.example.patientpost_discharge.models.CurrentPatients
import javax.inject.Inject

class AssignmentsAdapter @Inject constructor(
    private val context: Context,
    private val assignments : ArrayList<CurrentPatients>
): RecyclerView.Adapter<AssignmentsAdapter.AssignmentsViewHolder>(){

    inner class AssignmentsViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val patientName : TextView = itemView.findViewById(R.id.my_assignment)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssignmentsViewHolder {
        return AssignmentsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.my_assignments , parent , false))
    }

    override fun getItemCount(): Int {
        return assignments.size
    }

    override fun onBindViewHolder(holder: AssignmentsViewHolder, position: Int) {
        val chp = assignments[position]
        holder.patientName.text = chp.name
        holder.itemView.setOnClickListener {
            val intent = Intent(context , PatientDetails::class.java)
            intent.putExtra("name" , chp.name)
            intent.putExtra("age" , chp.age.toString())
            intent.putExtra("residence" , chp.residence)
            intent.putExtra("phone" , chp.phone)
            intent.putExtra("diagnosis" , chp.diagnosis)
            intent.putExtra("uid" , chp.uid)
            intent.putExtra("chpEmail" , chp.chpEmail)
            context.startActivity(intent)
        }
    }
}