package com.example.healthlevel.presentation

import com.example.healthlevel.domain.TipoCarro

data class InitialFormState(
    val modelo: String = "",
    val anoFabricacao: String = "",
    val tipoSelecionado: TipoCarro? = null,
    val erro: String? = null
)
