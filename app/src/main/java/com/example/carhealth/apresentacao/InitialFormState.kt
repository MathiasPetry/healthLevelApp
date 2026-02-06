package com.example.carhealth.apresentacao

import com.example.carhealth.dominio.TipoCarro

data class InitialFormState(
    val modelo: String = "",
    val anoFabricacao: String = "",
    val tipoSelecionado: TipoCarro? = null,
    val erro: String? = null
)
