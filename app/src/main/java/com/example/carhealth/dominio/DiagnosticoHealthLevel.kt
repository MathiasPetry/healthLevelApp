package com.example.carhealth.dominio

data class DiagnosticoHealthLevel(
    val resultado: ResultadoHealthLevel,
    val sugestoes: List<String>,
    val resumoRevisao: ResumoRevisao
)
