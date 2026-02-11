package com.example.carhealth.presentation

import com.example.carhealth.domain.TipoCarro

data class InitialFormState(
    val modelo: String = "",
    val anoFabricacao: String = "",
    val tipoSelecionado: TipoCarro? = null,
    val erro: String? = null
)
