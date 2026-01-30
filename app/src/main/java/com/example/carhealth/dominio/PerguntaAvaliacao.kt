package com.example.carhealth.dominio

data class PerguntaAvaliacao(
    val id: String,
    val texto: String,
    val opcoes: List<OpcaoResposta>
)
