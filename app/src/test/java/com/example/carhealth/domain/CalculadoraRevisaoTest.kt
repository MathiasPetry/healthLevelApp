package com.example.carhealth.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CalculadoraRevisaoTest {

    @Test
    fun `revisao por tempo fica em dia com 6 meses`() {
        val resumo = CalculadoraRevisao().calcular(
            dados = DadosRevisao(
                quilometragemTotal = 30_000,
                mesesDesdeUltimaRevisao = 6,
                preferencia = PreferenciaRevisao.POR_TEMPO
            ),
            anoFabricacao = 2020
        )

        assertEquals(180, resumo.diasParaProximaRevisao)
        assertFalse(resumo.revisaoAtrasada)
        assertEquals(10, resumo.pontuacaoRevisao)
        assertEquals("Revisão em dia", resumo.mensagem)
    }

    @Test
    fun `revisao por tempo fica atrasada com mais de 12 meses`() {
        val resumo = CalculadoraRevisao().calcular(
            dados = DadosRevisao(
                quilometragemTotal = 30_000,
                mesesDesdeUltimaRevisao = 14,
                preferencia = PreferenciaRevisao.POR_TEMPO
            ),
            anoFabricacao = 2020
        )

        assertEquals(-60, resumo.diasParaProximaRevisao)
        assertTrue(resumo.revisaoAtrasada)
        assertEquals(2, resumo.pontuacaoRevisao)
        assertEquals("Revisão atrasada", resumo.mensagem)
    }
}
