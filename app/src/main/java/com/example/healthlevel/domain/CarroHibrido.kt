package com.example.healthlevel.domain

class CarroHibrido(
    modelo: String,
    anoFabricacao: Int
) : Carro(modelo, anoFabricacao) {
    override val tipo: TipoCarro = TipoCarro.HIBRIDO

    override fun listarPerguntasObjetivas(): List<PerguntaAvaliacao> {
        return PerguntasCarro.perguntasComuns() +
            PerguntasCarro.perguntasCombustao() +
            PerguntasCarro.perguntaBateria()
    }
}
