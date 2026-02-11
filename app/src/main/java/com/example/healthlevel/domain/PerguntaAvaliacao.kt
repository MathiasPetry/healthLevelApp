package com.example.healthlevel.domain

data class PerguntaAvaliacao(
    val id: String,
    val texto: String,
    val opcoes: List<OpcaoResposta>
)
