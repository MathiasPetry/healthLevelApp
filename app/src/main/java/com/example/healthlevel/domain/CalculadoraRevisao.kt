package com.example.healthlevel.domain

import java.util.Calendar
import kotlin.math.max

class CalculadoraRevisao {
    companion object {
        private const val MIN_IDADE_MESES = 6
        private const val INTERVALO_REVISAO_MESES = 12
        private const val INTERVALO_REVISAO_KM = 10_000
        private const val LIMITE_KM_PONTUACAO_ALTA = 5_000
        private const val DIAS_POR_MES = 30
    }

    fun calcular(dados: DadosRevisao, anoFabricacao: Int): ResumoRevisao {
        val calendarioAtual = Calendar.getInstance()
        val anoAtual = calendarioAtual.get(Calendar.YEAR)
        val mesAtual = calendarioAtual.get(Calendar.MONTH) + 1
        val idadeMeses = max(MIN_IDADE_MESES, (anoAtual - anoFabricacao) * 12 + mesAtual)

        val (diasParaProxima, pontuacao, mensagem) = when (dados.preferencia) {
            PreferenciaRevisao.POR_TEMPO -> calcularPorTempo(dados.mesesDesdeUltimaRevisao)
            PreferenciaRevisao.POR_KM -> {
                val mediaKmMes = dados.quilometragemTotal.toDouble() / idadeMeses.toDouble()
                calcularPorKm(dados.mesesDesdeUltimaRevisao, mediaKmMes)
            }
        }

        return ResumoRevisao(
            diasParaProximaRevisao = diasParaProxima,
            revisaoAtrasada = diasParaProxima < 0,
            pontuacaoRevisao = pontuacao,
            mensagem = mensagem
        )
    }

    private fun calcularPorTempo(mesesDesdeUltima: Int): Triple<Int, Int, String> {
        val mesesRestantes = INTERVALO_REVISAO_MESES - mesesDesdeUltima
        val diasParaProximaRevisao = mesesRestantes * DIAS_POR_MES
        val pontuacaoRevisao = when {
            mesesDesdeUltima <= MIN_IDADE_MESES -> 10
            mesesDesdeUltima <= INTERVALO_REVISAO_MESES -> 6
            else -> 2
        }
        val mensagemRevisao = if (diasParaProximaRevisao >= 0) {
            "Revisão em dia"
        } else {
            "Revisão atrasada"
        }
        return Triple(diasParaProximaRevisao, pontuacaoRevisao, mensagemRevisao)
    }

    private fun calcularPorKm(mesesDesdeUltima: Int, mediaKmMes: Double): Triple<Int, Int, String> {
        val kmDesdeUltima = (mediaKmMes * mesesDesdeUltima).toInt()
        val kmRestantes = INTERVALO_REVISAO_KM - kmDesdeUltima
        val kmPorDia = if (mediaKmMes > 0) mediaKmMes / DIAS_POR_MES.toDouble() else 0.0
        val diasParaProximaRevisao = if (kmPorDia > 0) {
            (kmRestantes / kmPorDia).toInt()
        } else {
            0
        }
        val pontuacaoRevisao = when {
            kmDesdeUltima <= LIMITE_KM_PONTUACAO_ALTA -> 10
            kmDesdeUltima <= INTERVALO_REVISAO_KM -> 6
            else -> 2
        }
        val mensagemRevisao = if (kmRestantes >= 0) {
            "Revisão em dia"
        } else {
            "Revisão atrasada"
        }
        return Triple(diasParaProximaRevisao, pontuacaoRevisao, mensagemRevisao)
    }
}
