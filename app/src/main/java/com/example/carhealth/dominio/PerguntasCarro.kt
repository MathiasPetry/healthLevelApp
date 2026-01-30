package com.example.carhealth.dominio

object PerguntasCarro {
    fun perguntasComuns(): List<PerguntaAvaliacao> {
        return listOf(
            PerguntaAvaliacao(
                id = "freios",
                texto = "Como estão os freios?",
                opcoes = listOf(
                    OpcaoResposta("Resposta firme e sem ruídos", 10),
                    OpcaoResposta("Leve ruído ou resposta moderada", 6),
                    OpcaoResposta("Ruídos fortes ou resposta fraca", 2)
                )
            ),
            PerguntaAvaliacao(
                id = "pneus",
                texto = "Como estão os pneus?",
                opcoes = listOf(
                    OpcaoResposta("Desgaste uniforme e calibrados", 10),
                    OpcaoResposta("Desgaste moderado", 6),
                    OpcaoResposta("Desgaste acentuado ou bolhas", 2)
                )
            ),
            PerguntaAvaliacao(
                id = "suspensao",
                texto = "Como está a suspensão?",
                opcoes = listOf(
                    OpcaoResposta("Estável e sem barulhos", 10),
                    OpcaoResposta("Alguns ruídos em irregularidades", 6),
                    OpcaoResposta("Batidas constantes ou instável", 2)
                )
            ),
            PerguntaAvaliacao(
                id = "direcao",
                texto = "Como está a direção?",
                opcoes = listOf(
                    OpcaoResposta("Leve, precisa e sem vibrações", 10),
                    OpcaoResposta("Leve folga ou vibração ocasional", 6),
                    OpcaoResposta("Pesada ou com vibração constante", 2)
                )
            )
        )
    }

    fun perguntasCombustao(): List<PerguntaAvaliacao> {
        return listOf(
            PerguntaAvaliacao(
                id = "motor",
                texto = "Como está o estado do motor?",
                opcoes = listOf(
                    OpcaoResposta("Funcionamento suave e sem vazamentos", 10),
                    OpcaoResposta("Vibração leve ou consumo elevado", 6),
                    OpcaoResposta("Falhas, fumaça ou vazamentos", 2)
                )
            ),
            PerguntaAvaliacao(
                id = "fluidos",
                texto = "Como estão os fluidos do carro?",
                opcoes = listOf(
                    OpcaoResposta("Níveis corretos e limpos", 10),
                    OpcaoResposta("Algum fluido abaixo ou escurecido", 6),
                    OpcaoResposta("Vazamentos ou níveis críticos", 2)
                )
            )
        )
    }

    fun perguntaBateria(): PerguntaAvaliacao {
        return PerguntaAvaliacao(
            id = "bateria",
            texto = "Qual é o estado de saúde da bateria?",
            opcoes = listOf(
                OpcaoResposta("Superior a 90%", 10),
                OpcaoResposta("Entre 75% e 90%", 6),
                OpcaoResposta("Abaixo de 75%", 2)
            )
        )
    }
}
