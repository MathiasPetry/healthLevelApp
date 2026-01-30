package com.example.carhealth.dominio

import java.util.Calendar
import kotlin.math.max

class CalculadoraRevisao {
    fun calcular(dados: DadosRevisao, anoFabricacao: Int): ResumoRevisao {
        val agora = Calendar.getInstance()
        val anoAtual = agora.get(Calendar.YEAR)
        val mesAtual = agora.get(Calendar.MONTH) + 1
        val idadeMeses = max(6, (anoAtual - anoFabricacao) * 12 + mesAtual)

        val (diasParaProxima, pontuacao, mensagem) = when (dados.preferencia) {
            PreferenciaRevisao.POR_TEMPO -> calcularPorTempo(dados.mesesDesdeUltimaRevisao)
            PreferenciaRevisao.POR_KM -> {
                val mediaKmMes = if (idadeMeses > 0) {
                    dados.quilometragemTotal.toDouble() / idadeMeses.toDouble()
                } else {
                    0.0
                }
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
        val mesesRestantes = 12 - mesesDesdeUltima
        val dias = mesesRestantes * 30
        val pontuacao = when {
            mesesDesdeUltima <= 6 -> 10
            mesesDesdeUltima <= 12 -> 6
            else -> 2
        }
        val mensagem = if (dias >= 0) {
            "Revisão em dia"
        } else {
            "Revisão atrasada"
        }
        return Triple(dias, pontuacao, mensagem)
    }

    private fun calcularPorKm(mesesDesdeUltima: Int, mediaKmMes: Double): Triple<Int, Int, String> {
        val kmDesdeUltima = (mediaKmMes * mesesDesdeUltima).toInt()
        val kmRestantes = 10000 - kmDesdeUltima
        val kmPorDia = if (mediaKmMes > 0) mediaKmMes / 30.0 else 0.0
        val dias = if (kmPorDia > 0) {
            (kmRestantes / kmPorDia).toInt()
        } else {
            0
        }
        val pontuacao = when {
            kmDesdeUltima <= 5000 -> 10
            kmDesdeUltima <= 10000 -> 6
            else -> 2
        }
        val mensagem = if (kmRestantes >= 0) {
            "Revisão em dia"
        } else {
            "Revisão atrasada"
        }
        val diasNormalizados = if (kmPorDia > 0) dias else 0
        return Triple(diasNormalizados, pontuacao, mensagem)
    }
}
