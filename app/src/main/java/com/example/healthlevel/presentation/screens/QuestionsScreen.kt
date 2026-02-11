package com.example.healthlevel.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.healthlevel.presentation.MaintenanceState
import com.example.healthlevel.presentation.theme.PremiumBackground
import com.example.healthlevel.presentation.theme.PremiumPanel
import com.example.healthlevel.presentation.theme.premiumFieldColors
import com.example.healthlevel.domain.Carro
import com.example.healthlevel.domain.PerguntaAvaliacao
import com.example.healthlevel.domain.PreferenciaRevisao

@Composable
fun QuestionsScreen(
    car: Carro?,
    questions: List<PerguntaAvaliacao>,
    maintenanceState: MaintenanceState,
    getSelectedAnswer: (String) -> Int?,
    recordAnswer: (String, Int) -> Unit,
    allAnswered: () -> Boolean,
    onUpdateMileage: (String) -> Unit,
    onUpdateMonthsSinceService: (String) -> Unit,
    onSelectPreference: (PreferenciaRevisao) -> Unit,
    onBack: () -> Unit,
    onSeeResult: () -> Unit
) {
    PremiumBackground {
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(22.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    CabecalhoPerguntas(carro = car)
                }

                item {
                    SecaoRevisao(
                        maintenanceState = maintenanceState,
                        onUpdateMileage = onUpdateMileage,
                        onUpdateMonthsSinceService = onUpdateMonthsSinceService,
                        onSelectPreference = onSelectPreference
                    )
                }

                items(questions, key = { it.id }) { question ->
                    PerguntaMinimalista(
                        pergunta = question,
                        indiceSelecionado = getSelectedAnswer(question.id),
                        aoSelecionarOpcao = { indice -> recordAnswer(question.id, indice) }
                    )
                }

                item { Spacer(modifier = Modifier.height(16.dp)) }
            }

            RodapePerguntas(
                habilitarResultado = allAnswered(),
                aoVoltar = onBack,
                aoVerResultado = onSeeResult
            )
        }
    }
}

@Composable
private fun CabecalhoPerguntas(carro: Carro?) {
    val colors = MaterialTheme.colorScheme

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = "Checklist inteligente",
            style = MaterialTheme.typography.titleLarge,
            color = colors.onBackground
        )
        val descricao = if (carro != null) {
            "${carro.modelo} • ${carro.anoFabricacao} • ${carro.tipo.rotulo}"
        } else {
            "Responda com atenção para um diagnóstico preciso."
        }
        Text(
            text = descricao,
            style = MaterialTheme.typography.bodyMedium,
            color = colors.onSurfaceVariant
        )
    }
}

@Composable
private fun SecaoRevisao(
    maintenanceState: MaintenanceState,
    onUpdateMileage: (String) -> Unit,
    onUpdateMonthsSinceService: (String) -> Unit,
    onSelectPreference: (PreferenciaRevisao) -> Unit
) {
    val colors = MaterialTheme.colorScheme

    PremiumPanel(modifier = Modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(
                text = "Revisão do carro",
                style = MaterialTheme.typography.titleMedium,
                color = colors.onSurface
            )

            OutlinedTextField(
                value = maintenanceState.quilometragemTotal,
                onValueChange = onUpdateMileage,
                label = { Text("Quilometragem total") },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = premiumFieldColors(),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = maintenanceState.mesesDesdeUltimaRevisao,
                onValueChange = onUpdateMonthsSinceService,
                label = { Text("Meses desde a última revisão") },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = premiumFieldColors(),
                modifier = Modifier.fillMaxWidth()
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Preferência de lembrete",
                    style = MaterialTheme.typography.bodyLarge,
                    color = colors.onSurfaceVariant
                )
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    PreferenciaCard(
                        preferencia = PreferenciaRevisao.POR_KM,
                        selecionada = maintenanceState.preferencia == PreferenciaRevisao.POR_KM,
                        aoSelecionar = { onSelectPreference(PreferenciaRevisao.POR_KM) },
                        modifier = Modifier.weight(1f)
                    )
                    PreferenciaCard(
                        preferencia = PreferenciaRevisao.POR_TEMPO,
                        selecionada = maintenanceState.preferencia == PreferenciaRevisao.POR_TEMPO,
                        aoSelecionar = { onSelectPreference(PreferenciaRevisao.POR_TEMPO) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            if (maintenanceState.erro != null) {
                Text(
                    text = maintenanceState.erro,
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.error
                )
            }
        }
    }
}

@Composable
private fun PreferenciaCard(
    preferencia: PreferenciaRevisao,
    selecionada: Boolean,
    aoSelecionar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val shape = RoundedCornerShape(16.dp)
    val corFundo = if (selecionada) colors.primaryContainer else colors.surface
    val corBorda = if (selecionada) colors.primary else colors.outline.copy(alpha = 0.7f)
    val corTexto = if (selecionada) colors.onPrimaryContainer else colors.onSurface

    Column(
        modifier = modifier
            .border(BorderStroke(1.dp, corBorda), shape)
            .background(corFundo, shape)
            .clickable { aoSelecionar() }
            .padding(vertical = 12.dp, horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = preferencia.rotulo,
            style = MaterialTheme.typography.bodyMedium,
            color = corTexto,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun PerguntaMinimalista(
    pergunta: PerguntaAvaliacao,
    indiceSelecionado: Int?,
    aoSelecionarOpcao: (Int) -> Unit
) {
    val colors = MaterialTheme.colorScheme

    PremiumPanel(modifier = Modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                text = pergunta.texto,
                style = MaterialTheme.typography.titleMedium,
                color = colors.onSurface
            )
            pergunta.opcoes.forEachIndexed { indice, opcao ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { aoSelecionarOpcao(indice) }
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = indiceSelecionado == indice,
                        onClick = { aoSelecionarOpcao(indice) },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = colors.primary,
                            unselectedColor = colors.onSurfaceVariant
                        )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = opcao.texto,
                        style = MaterialTheme.typography.bodyLarge,
                        color = colors.onSurface.copy(alpha = 0.95f)
                    )
                }
            }
        }
    }
}

@Composable
private fun RodapePerguntas(
    habilitarResultado: Boolean,
    aoVoltar: () -> Unit,
    aoVerResultado: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.background.copy(alpha = 0.7f))
            .padding(horizontal = 20.dp, vertical = 18.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            onClick = aoVoltar,
            colors = ButtonDefaults.buttonColors(
                containerColor = colors.surface,
                contentColor = colors.onSurface
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 2.dp,
                pressedElevation = 1.dp
            ),
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(18.dp)
        ) {
            Text(text = "Voltar")
        }
        Button(
            onClick = aoVerResultado,
            enabled = habilitarResultado,
            colors = ButtonDefaults.buttonColors(
                containerColor = colors.primary,
                contentColor = colors.onPrimary,
                disabledContainerColor = colors.surfaceVariant,
                disabledContentColor = colors.onSurfaceVariant
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 4.dp,
                pressedElevation = 2.dp
            ),
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(18.dp)
        ) {
            Text(text = "Ver HealthLevel")
        }
    }
}
