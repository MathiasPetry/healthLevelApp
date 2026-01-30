package com.example.carhealth.apresentacao.telas

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.carhealth.apresentacao.InitialFormState
import com.example.carhealth.domain.CarType

private val Preto = Color(0xFF000000)
private val CinzaEscuro = Color(0xFF1F2933)
private val Branco = Color(0xFFF9FAFB)
private val Verde = Color(0xFF10B981)
private val Vermelho = Color(0xFFDC2626)

@Composable
fun InitialScreen(
    formState: InitialFormState,
    onUpdateModel: (String) -> Unit,
    onUpdateYear: (String) -> Unit,
    onSelectType: (CarType) -> Unit,
    onContinue: () -> Unit
) {
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
                style = MaterialTheme.typography.displayLarge,
                color = Branco
            )
            Text(
                text = "Diagnóstico objetivo do seu carro",
                style = MaterialTheme.typography.bodyLarge,
                color = Branco.copy(alpha = 0.7f)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, CinzaEscuro, RoundedCornerShape(20.dp))
                .background(CinzaEscuro, RoundedCornerShape(20.dp))
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Dados iniciais",
                style = MaterialTheme.typography.titleMedium,
                color = Branco
            )

            OutlinedTextField(
                value = formState.model,
                onValueChange = onUpdateModel,
                label = { Text("Modelo do carro") },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Verde,
                    unfocusedBorderColor = Branco.copy(alpha = 0.15f),
                    focusedLabelColor = Verde,
                    unfocusedLabelColor = Branco.copy(alpha = 0.5f),
                    cursorColor = Verde,
                    focusedTextColor = Branco,
                    unfocusedTextColor = Branco
                ),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = formState.manufactureYear,
                onValueChange = onUpdateYear,
                label = { Text("Ano de fabricação") },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Verde,
                    unfocusedBorderColor = Branco.copy(alpha = 0.15f),
                    focusedLabelColor = Verde,
                    unfocusedLabelColor = Branco.copy(alpha = 0.5f),
                    cursorColor = Verde,
                    focusedTextColor = Branco,
                    unfocusedTextColor = Branco
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "Tipo de carro",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Branco.copy(alpha = 0.85f)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CarType.values().forEach { type ->
                        CarTypeCard(
                            type = type,
                            selected = formState.selectedType == type,
                            onSelect = { onSelectType(type) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            if (formState.error != null) {
                Text(
                    text = formState.error,
                    color = Vermelho,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Button(
            onClick = onContinue,
            colors = ButtonDefaults.buttonColors(
                containerColor = Verde,
                contentColor = Preto
            ),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text(
                text = "Continuar",
                style = MaterialTheme.typography.titleMedium,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
private fun CarTypeCard(
    type: CarType,
    selected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    val background = if (selected) Verde else Preto
    val border = if (selected) Verde else Branco.copy(alpha = 0.2f)
    val textColor = if (selected) Preto else Branco

    Column(
        modifier = modifier
            .height(56.dp)
            .border(1.dp, border, RoundedCornerShape(14.dp))
            .background(background, RoundedCornerShape(14.dp))
            .clickable(onClick = onSelect),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = type.label,
            color = textColor,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}
