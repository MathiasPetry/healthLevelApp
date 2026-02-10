package com.example.carhealth.apresentacao.telas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.carhealth.apresentacao.tema.PremiumBackground
import com.example.carhealth.apresentacao.tema.PremiumPanel
import com.example.carhealth.dominio.Carro
import com.example.carhealth.dominio.DiagnosticoHealthLevel

@Composable
fun ResultScreen(
    car: Carro?,
    diagnostic: DiagnosticoHealthLevel?,
    onRestart: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val resultado = diagnostic?.resultado
    val resumoRevisao = diagnostic?.resumoRevisao
    val diasParaRevisao = resumoRevisao?.diasParaProximaRevisao
    val mensagemRevisao = when {
        diasParaRevisao == null -> "Dados insuficientes para calcular a próxima revisão."
        diasParaRevisao > 0 -> "Próxima revisão em $diasParaRevisao dias"
        diasParaRevisao == 0 -> "Revisão prevista para hoje"
        else -> "Revisão atrasada há ${-diasParaRevisao} dias"
    }

    PremiumBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 28.dp),
            verticalArrangement = Arrangement.spacedBy(22.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "HealthLevel",
                    style = MaterialTheme.typography.titleLarge,
                    color = colors.onBackground
                )
                Text(
                    text = "Diagnóstico completo",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.onSurfaceVariant
                )
            }

            PremiumPanel(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "${resultado?.pontuacao ?: 0}",
                        style = MaterialTheme.typography.displayLarge,
                        color = colors.primary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = resultado?.classificacao ?: "",
                        style = MaterialTheme.typography.titleMedium,
                        color = colors.onSurface
                    )
                    Text(
                        text = resultado?.descricao ?: "",
                        style = MaterialTheme.typography.bodyLarge,
                        color = colors.onSurface.copy(alpha = 0.86f)
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = mensagemRevisao,
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.onSurfaceVariant
                    )

                    if (car != null) {
                        Text(
                            text = "${car.modelo} • ${car.anoFabricacao} • ${car.tipo.rotulo}",
                            style = MaterialTheme.typography.bodySmall,
                            color = colors.onSurfaceVariant.copy(alpha = 0.9f)
                        )
                    }
                }
            }

            PremiumPanel(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Sugestões personalizadas",
                        style = MaterialTheme.typography.titleMedium,
                        color = colors.onSurface
                    )
                    val sugestoes = diagnostic?.sugestoes.orEmpty()
                    sugestoes.forEach { sugestao ->
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "•",
                                color = colors.primary,
                                fontSize = 18.sp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = sugestao,
                                style = MaterialTheme.typography.bodyLarge,
                                color = colors.onSurface.copy(alpha = 0.9f)
                            )
                        }
                    }
                }
            }

            Button(
                onClick = onRestart,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.primary,
                    contentColor = colors.onPrimary
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp,
                    pressedElevation = 2.dp
                ),
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    text = "Nova avaliação",
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 16.sp
                )
            }
        }
    }
}
