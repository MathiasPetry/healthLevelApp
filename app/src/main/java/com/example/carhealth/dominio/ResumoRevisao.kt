package com.example.carhealth.dominio

data class ResumoRevisao(
    val diasParaProximaRevisao: Int,
    val revisaoAtrasada: Boolean,
    val pontuacaoRevisao: Int,
    val mensagem: String
)
