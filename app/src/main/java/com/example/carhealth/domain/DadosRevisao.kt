package com.example.carhealth.domain

data class DadosRevisao(
    val quilometragemTotal: Int,
    val mesesDesdeUltimaRevisao: Int,
    val preferencia: PreferenciaRevisao
)
