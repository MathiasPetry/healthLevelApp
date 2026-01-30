package com.example.carhealth.apresentacao

import com.example.carhealth.domain.MaintenancePreference

data class MaintenanceState(
    val totalMileage: String = "",
    val monthsSinceLastService: String = "",
    val preference: MaintenancePreference? = null,
    val error: String? = null
)
