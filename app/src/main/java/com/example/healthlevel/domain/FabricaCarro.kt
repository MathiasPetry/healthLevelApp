package com.example.healthlevel.domain

object FabricaCarro {
    fun criarCarro(tipo: TipoCarro, modelo: String, anoFabricacao: Int): Carro {
        return when (tipo) {
            TipoCarro.ELETRICO -> CarroEletrico(modelo, anoFabricacao)
            TipoCarro.COMBUSTAO -> CarroCombustao(modelo, anoFabricacao)
            TipoCarro.HIBRIDO -> CarroHibrido(modelo, anoFabricacao)
        }
    }
}
