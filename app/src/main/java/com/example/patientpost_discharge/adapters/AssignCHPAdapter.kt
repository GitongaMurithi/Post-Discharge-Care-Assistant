package com.example.patientpost_discharge.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.patientpost_discharge.doctor.ConfirmAssignment
import com.example.patientpost_discharge.R
import com.example.patientpost_discharge.models.HealthWorker
import javax.inject.Inject

class AssignCHPAdapter @Inject constructor(
    private val context: Context,
    private val chp : ArrayList<HealthWorker>
): RecyclerView.Adapter<AssignCHPAdapter.CHPViewHolder>(){
     inner class CHPViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
         val chpName : TextView = itemView.findViewById(R.id.healthProvider)
         val location : TextView = itemView.findViewById(R.id.location)
         val assign : TextView = itemView.findViewById(R.id.assign)
     }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CHPViewHolder {
        return CHPViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.assignment_item , parent , false))
    }

    override fun getItemCount(): Int {
        return chp.size
    }

    override fun onBindViewHolder(holder: CHPViewHolder, position: Int) {
        val currentCHP = chp[position]
        holder.chpName.text = currentCHP.name
        holder.location.text = currentCHP.location
        holder.assign.setOnClickListener {
            val intent = Intent(context , ConfirmAssignment::class.java)
                .putExtra("name" , currentCHP.name)
                .putExtra("email" , currentCHP.email)
                .putExtra("uid" , currentCHP.uid)
            context.startActivity(intent)
        }
    }

}
