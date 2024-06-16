package com.example.patientpost_discharge.models

data class FinalDetails(
    val medicine : String = "",
    val clinic : String = "",
    val recommendation : String = "",
    val assignedTo : String = "",
    val progress : String = "",
    val chpEmail : String = "",
    val chpUid : String = ""
)
