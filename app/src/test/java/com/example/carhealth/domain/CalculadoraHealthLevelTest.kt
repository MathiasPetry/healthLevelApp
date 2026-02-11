package com.example.carhealth.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class CalculadoraHealthLevelTest {

    private val pergunta = PerguntaAvaliacao(
        id = "estado-geral",
        texto = "Estado geral",
        opcoes = listOf(
            OpcaoResposta("Perfeito", 100),
            OpcaoResposta("Muito bom", 85),
            OpcaoResposta("Bom", 70),
            OpcaoResposta("Regular", 50),
            OpcaoResposta("Crítico", 49)
        )
    )

    @Test
    fun `classifica como Excelente no limite de 85`() {
        val resultado = CalculadoraHealthLevel().calcular(
            perguntas = listOf(pergunta),
            respostasSelecionadas = mapOf(pergunta.id to 1)
        )

        assertEquals(85, resultado.pontuacao)
        assertEquals("Excelente", resultado.classificacao)
    }

    @Test
    fun `classifica como Bom no limite de 70`() {
        val resultado = CalculadoraHealthLevel().calcular(
            perguntas = listOf(pergunta),
            respostasSelecionadas = mapOf(pergunta.id to 2)
        )

        assertEquals(70, resultado.pontuacao)
        assertEquals("Bom", resultado.classificacao)
    }

    @Test
    fun `classifica como Regular no limite de 50`() {
        val resultado = CalculadoraHealthLevel().calcular(
            perguntas = listOf(pergunta),
            respostasSelecionadas = mapOf(pergunta.id to 3)
        )

        assertEquals(50, resultado.pontuacao)
        assertEquals("Regular", resultado.classificacao)
    }

    @Test
    fun `classifica como Crítico abaixo de 50`() {
        val resultado = CalculadoraHealthLevel().calcular(
            perguntas = listOf(pergunta),
            respostasSelecionadas = mapOf(pergunta.id to 4)
        )

        assertEquals(49, resultado.pontuacao)
        assertEquals("Crítico", resultado.classificacao)
    }
}
