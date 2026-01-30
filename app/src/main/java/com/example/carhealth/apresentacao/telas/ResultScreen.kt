package com.example.carhealth.apresentacao.telas

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.carhealth.dominio.Carro
import com.example.carhealth.dominio.DiagnosticoHealthLevel

private val Preto = Color(0xFF000000)
private val CinzaEscuro = Color(0xFF1F2933)
private val Branco = Color(0xFFF9FAFB)
private val Verde = Color(0xFF10B981)

@Composable
fun TelaResultado(
    carro: Carro?,
    diagnostico: DiagnosticoHealthLevel?,
    aoReiniciar: () -> Unit
) {
    val resultado = diagnostico?.resultado
    val resumoRevisao = diagnostico?.resumoRevisao
    val diasParaRevisao = resumoRevisao?.diasParaProximaRevisao
    val mensagemRevisao = when {
        diasParaRevisao == null -> "Dados insuficientes para calcular a próxima revisão."
        diasParaRevisao > 0 -> "Próxima revisão em ${diasParaRevisao} dias"
        diasParaRevisao == 0 -> "Revisão prevista para hoje"
        else -> "Revisão atrasada há ${-diasParaRevisao} dias"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Preto)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = "HealthLevel",
                style = MaterialTheme.typography.titleLarge,
                color = Branco
            )
            Text(
                text = "Diagnóstico completo",
                style = MaterialTheme.typography.bodyMedium,
                color = Branco.copy(alpha = 0.7f)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, CinzaEscuro, RoundedCornerShape(20.dp))
                .background(CinzaEscuro, RoundedCornerShape(20.dp))
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "${resultado?.pontuacao ?: 0}",
                style = MaterialTheme.typography.displayLarge,
                color = Verde,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = resultado?.classificacao ?: "",
                style = MaterialTheme.typography.titleMedium,
                color = Branco
            )
            Text(
                text = resultado?.descricao ?: "",
                style = MaterialTheme.typography.bodyLarge,
                color = Branco.copy(alpha = 0.8f)
            )

            Text(
                text = mensagemRevisao,
                style = MaterialTheme.typography.bodyMedium,
                color = Branco.copy(alpha = 0.75f)
            )

            if (carro != null) {
                Text(
                    text = "${carro.modelo} • ${carro.anoFabricacao} • ${carro.tipo.rotulo}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Branco.copy(alpha = 0.6f)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, CinzaEscuro, RoundedCornerShape(20.dp))
                .background(CinzaEscuro, RoundedCornerShape(20.dp))
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Sugestões personalizadas",
                style = MaterialTheme.typography.titleMedium,
                color = Branco
            )
            val sugestoes = diagnostico?.sugestoes.orEmpty()
            sugestoes.forEach { sugestao ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "•",
                        color = Verde,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = sugestao,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Branco.copy(alpha = 0.85f)
                    )
                }
            }
        }

        Button(
            onClick = aoReiniciar,
            colors = ButtonDefaults.buttonColors(
                containerColor = Verde,
                contentColor = Preto
            ),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Nova avaliação",
                style = MaterialTheme.typography.titleMedium,
                fontSize = 16.sp
            )
        }
    }
}
