package com.example.patientpost_discharge.models

data class Patient(
    val name : String = "",
    val phone : String = "",
    val email : String = "",
    val password : String = "",
    val age : Int = 0,
    val residence : String = "",
    val diagnosis : String = "",
    val discharged : Boolean = false,
    val authCode : Int = 0
)
