package com.example.carhealth.domain

data class DiagnosticoHealthLevel(
    val resultado: ResultadoHealthLevel,
    val sugestoes: List<String>,
    val resumoRevisao: ResumoRevisao
)
