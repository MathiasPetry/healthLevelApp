package com.example.carhealth.domain

abstract class Carro(
    val modelo: String,
    val anoFabricacao: Int
) {
    abstract val tipo: TipoCarro
    abstract fun listarPerguntasObjetivas(): List<PerguntaAvaliacao>
}
