package com.example.carhealth.apresentacao

import com.example.carhealth.dominio.PreferenciaRevisao

data class MaintenanceState(
    val quilometragemTotal: String = "",
    val mesesDesdeUltimaRevisao: String = "",
    val preferencia: PreferenciaRevisao? = null,
    val erro: String? = null
)
