package com.example.carhealth.domain

import kotlin.math.roundToInt

class CalculadoraHealthLevel {
    fun calcular(
        perguntas: List<PerguntaAvaliacao>,
        respostasSelecionadas: Map<String, Int>,
        pontosExtras: Int = 0,
        pontosMaximosExtras: Int = 0
    ): ResultadoHealthLevel {
        val totalMaximo = perguntas.sumOf { pergunta ->
            pergunta.opcoes.maxOf { it.pontos }
        }.plus(pontosMaximosExtras).coerceAtLeast(1)

        val totalObtido = perguntas.sumOf { pergunta ->
            val indice = respostasSelecionadas[pergunta.id] ?: 0
            pergunta.opcoes.getOrNull(indice)?.pontos ?: 0
        } + pontosExtras

        val pontuacao = ((totalObtido.toDouble() / totalMaximo.toDouble()) * 100).roundToInt()
        val classificacao = when {
            pontuacao >= 85 -> "Excelente"
            pontuacao >= 70 -> "Bom"
            pontuacao >= 50 -> "Regular"
            else -> "Crítico"
        }

        val descricao = when (classificacao) {
            "Excelente" -> "Seu carro está em ótimo estado."
            "Bom" -> "Seu carro está bem cuidado, com pequenos pontos de atenção."
            "Regular" -> "Seu carro precisa de manutenção preventiva em breve."
            else -> "Seu carro precisa de atenção imediata."
        }

        return ResultadoHealthLevel(
            pontuacao = pontuacao.coerceIn(0, 100),
            classificacao = classificacao,
            descricao = descricao
        )
    }
}
