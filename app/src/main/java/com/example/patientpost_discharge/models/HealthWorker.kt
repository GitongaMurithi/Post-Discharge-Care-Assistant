package com.example.patientpost_discharge.models

import java.io.Serializable

data class HealthWorker(
    val name : String = "",
    val email : String = "",
    val phone : String = "",
    val password : String = "",
    val location : String = "",
    val uid : String = ""
) : Serializable
