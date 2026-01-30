package com.example.carhealth.apresentacao

import com.example.carhealth.domain.CarType

data class InitialFormState(
    val model: String = "",
    val manufactureYear: String = "",
    val selectedType: CarType? = null,
    val error: String? = null
)
