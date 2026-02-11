package com.example.carhealth.presentation

import com.example.carhealth.domain.PreferenciaRevisao

data class MaintenanceState(
    val quilometragemTotal: String = "",
    val mesesDesdeUltimaRevisao: String = "",
    val preferencia: PreferenciaRevisao? = null,
    val erro: String? = null
)
