package com.example.patientpost_discharge.models

import java.io.Serializable

data class CurrentPatients(
    val name : String = "",
    val email : String = "",
    val password : String = "",
    val age : String = "",
    val residence : String = "",
    val phone : String = "",
    val diagnosis : String = "",
    val test : String = "",
    val uid : String = "",
    val chpEmail : String = "",
    val discharged : Boolean = false,
) : Serializable
