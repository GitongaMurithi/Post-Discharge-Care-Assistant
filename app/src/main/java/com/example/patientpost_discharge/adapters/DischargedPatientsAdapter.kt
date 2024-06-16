package com.example.patientpost_discharge.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.patientpost_discharge.R
import javax.inject.Inject

class DischargedPatientsAdapter @Inject constructor(
    private val context: Context,
    private val discharged : ArrayList<com.example.patientpost_discharge.models.CurrentPatients>,
    private val onPatientClicked : OnPatientClicked
): RecyclerView.Adapter<DischargedPatientsAdapter.DPViewHolder>(){
     inner class DPViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
         val name : TextView = itemView.findViewById(R.id.my_assignment)
     }

    interface OnPatientClicked {
        fun onPatientClicked(position: Int)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DPViewHolder {
        return DPViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.discharged_item , parent , false))
    }

    override fun getItemCount(): Int {
        return discharged.size
    }

    override fun onBindViewHolder(holder: DPViewHolder, position: Int) {
        val currentPosition = discharged[position]
        holder.name.text = currentPosition.name
        holder.itemView.setOnClickListener {
            onPatientClicked.onPatientClicked(position)
//            val intent = Intent(context , DischargedDetails::class.java)
//            intent.putExtra("name" , currentPosition.name)
//            intent.putExtra("age" , currentPosition.age.toString())
//            intent.putExtra("residence" , currentPosition.residence)
//            intent.putExtra("phone" , currentPosition.phone)
//            intent.putExtra("diagnosis" , currentPosition.diagnosis)
//            intent.putExtra("uid" , currentPosition.uid)
//            Toast.makeText(context , currentPosition.uid , Toast.LENGTH_LONG).show()
//            context.startActivity(intent)
        }
    }
}
