package com.example.patientpost_discharge.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.patientpost_discharge.R
import com.example.patientpost_discharge.chp.ProgressDetails
import javax.inject.Inject

class ProgressDetailsAdapter @Inject constructor(
    private val context: Context,
    private val discharged : ArrayList<com.example.patientpost_discharge.models.CurrentPatients>
): RecyclerView.Adapter<ProgressDetailsAdapter.DPViewHolder>(){
     inner class DPViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
         val name : TextView = itemView.findViewById(R.id.my_assignment)
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
            val intent = Intent(context , ProgressDetails::class.java)
            intent.putExtra("name" , currentPosition.name)
            intent.putExtra("age" , currentPosition.age)
            intent.putExtra("residence" , currentPosition.residence)
            intent.putExtra("phone" , currentPosition.phone)
            intent.putExtra("diagnosis" , currentPosition.diagnosis)
            intent.putExtra("uid" , currentPosition.uid)
            intent.putExtra("test" , currentPosition.test)
            context.startActivity(intent)
        }
    }
}
