package com.example.carhealth.domain

data class PerguntaAvaliacao(
    val id: String,
    val texto: String,
    val opcoes: List<OpcaoResposta>
)
