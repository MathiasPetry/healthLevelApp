package com.example.healthlevel.domain

class CarroCombustao(
    modelo: String,
    anoFabricacao: Int
) : Carro(modelo, anoFabricacao) {
    override val tipo: TipoCarro = TipoCarro.COMBUSTAO

    override fun listarPerguntasObjetivas(): List<PerguntaAvaliacao> {
        return PerguntasCarro.perguntasComuns() + PerguntasCarro.perguntasCombustao()
    }
}
