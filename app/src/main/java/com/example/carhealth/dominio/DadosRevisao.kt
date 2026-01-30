package com.example.carhealth.dominio

data class DadosRevisao(
    val quilometragemTotal: Int,
    val mesesDesdeUltimaRevisao: Int,
    val preferencia: PreferenciaRevisao
)
