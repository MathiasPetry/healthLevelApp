package com.example.carhealth.domain

class CarroEletrico(
    modelo: String,
    anoFabricacao: Int
) : Carro(modelo, anoFabricacao) {
    override val tipo: TipoCarro = TipoCarro.ELETRICO

    override fun listarPerguntasObjetivas(): List<PerguntaAvaliacao> {
        return PerguntasCarro.perguntasComuns() + PerguntasCarro.perguntaBateria()
    }
}
