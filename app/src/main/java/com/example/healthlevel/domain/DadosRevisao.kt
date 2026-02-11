package com.example.healthlevel.domain

data class DadosRevisao(
    val quilometragemTotal: Int,
    val mesesDesdeUltimaRevisao: Int,
    val preferencia: PreferenciaRevisao
)
