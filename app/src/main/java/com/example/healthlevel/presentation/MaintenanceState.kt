package com.example.healthlevel.presentation

import com.example.healthlevel.domain.PreferenciaRevisao

data class MaintenanceState(
    val quilometragemTotal: String = "",
    val mesesDesdeUltimaRevisao: String = "",
    val preferencia: PreferenciaRevisao? = null,
    val erro: String? = null
)
