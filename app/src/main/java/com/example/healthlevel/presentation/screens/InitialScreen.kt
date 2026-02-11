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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.healthlevel.presentation.InitialFormState
import com.example.healthlevel.presentation.theme.PremiumBackground
import com.example.healthlevel.presentation.theme.PremiumPanel
import com.example.healthlevel.presentation.theme.HealthLevelEmblem
import com.example.healthlevel.presentation.theme.HealthLevelWordmark
import com.example.healthlevel.presentation.theme.premiumFieldColors
import com.example.healthlevel.domain.TipoCarro

@Composable
fun InitialScreen(
    formState: InitialFormState,
    onUpdateModel: (String) -> Unit,
    onUpdateYear: (String) -> Unit,
    onSelectType: (TipoCarro) -> Unit,
    onContinue: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    PremiumBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 28.dp),
            verticalArrangement = Arrangement.spacedBy(22.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HealthLevelEmblem(modifier = Modifier.size(74.dp))
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    HealthLevelWordmark()
                    Text(
                        text = "Diagnóstico objetivo do seu carro",
                        style = MaterialTheme.typography.bodyLarge,
                        color = colors.onSurfaceVariant
                    )
                }
            }

            PremiumPanel(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(
                        text = "Dados iniciais",
                        style = MaterialTheme.typography.titleMedium,
                        color = colors.onSurface
                    )

                    OutlinedTextField(
                        value = formState.modelo,
                        onValueChange = onUpdateModel,
                        label = { Text("Modelo do carro") },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = premiumFieldColors(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = formState.anoFabricacao,
                        onValueChange = onUpdateYear,
                        label = { Text("Ano de fabricação") },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = premiumFieldColors(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            text = "Tipo de carro",
                            style = MaterialTheme.typography.titleMedium,
                            color = colors.onSurface
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            TipoCarro.values().forEach { type ->
                                CarTypeCard(
                                    type = type,
                                    selected = formState.tipoSelecionado == type,
                                    onSelect = { onSelectType(type) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }

                    if (formState.erro != null) {
                        Text(
                            text = formState.erro,
                            color = colors.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(2.dp))

            Button(
                onClick = onContinue,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.primary,
                    contentColor = colors.onPrimary
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp,
                    pressedElevation = 2.dp
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(18.dp)
            ) {
                Text(
                    text = "Continuar",
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
private fun CarTypeCard(
    type: TipoCarro,
    selected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val shape = RoundedCornerShape(16.dp)
    val background = if (selected) colors.primaryContainer else colors.surface
    val border = if (selected) colors.primary else colors.outline.copy(alpha = 0.75f)
    val textColor = if (selected) colors.onPrimaryContainer else colors.onSurface

    Column(
        modifier = modifier
            .height(56.dp)
            .border(BorderStroke(1.dp, border), shape)
            .background(background, shape)
            .clickable(onClick = onSelect),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = type.rotulo,
            color = textColor,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}
