package com.example.carhealth.domain

data class ResumoRevisao(
    val diasParaProximaRevisao: Int,
    val revisaoAtrasada: Boolean,
    val pontuacaoRevisao: Int,
    val mensagem: String
)
