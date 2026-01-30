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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.carhealth.apresentacao.EstadoRevisao
import com.example.carhealth.dominio.Carro
import com.example.carhealth.dominio.PerguntaAvaliacao
import com.example.carhealth.dominio.PreferenciaRevisao

private val Preto = Color(0xFF000000)
private val CinzaEscuro = Color(0xFF1F2933)
private val Branco = Color(0xFFF9FAFB)
private val Verde = Color(0xFF10B981)
private val Vermelho = Color(0xFFDC2626)

@Composable
fun TelaPerguntas(
    carro: Carro?,
    perguntas: List<PerguntaAvaliacao>,
    estadoRevisao: EstadoRevisao,
    obterRespostaSelecionada: (String) -> Int?,
    registrarResposta: (String, Int) -> Unit,
    todasRespondidas: () -> Boolean,
    aoAtualizarQuilometragem: (String) -> Unit,
    aoAtualizarMesesRevisao: (String) -> Unit,
    aoSelecionarPreferencia: (PreferenciaRevisao) -> Unit,
    aoVoltar: () -> Unit,
    aoVerResultado: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Preto)
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                CabecalhoPerguntas(carro = carro)
            }

            item {
                SecaoRevisao(
                    estadoRevisao = estadoRevisao,
                    aoAtualizarQuilometragem = aoAtualizarQuilometragem,
                    aoAtualizarMesesRevisao = aoAtualizarMesesRevisao,
                    aoSelecionarPreferencia = aoSelecionarPreferencia
                )
            }

            items(perguntas, key = { it.id }) { pergunta ->
                PerguntaMinimalista(
                    pergunta = pergunta,
                    indiceSelecionado = obterRespostaSelecionada(pergunta.id),
                    aoSelecionarOpcao = { indice -> registrarResposta(pergunta.id, indice) }
                )
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }

        RodapePerguntas(
            habilitarResultado = todasRespondidas(),
            aoVoltar = aoVoltar,
            aoVerResultado = aoVerResultado
        )
    }
}

@Composable
private fun CabecalhoPerguntas(carro: Carro?) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = "Checklist inteligente",
            style = MaterialTheme.typography.titleLarge,
            color = Branco
        )
        val descricao = if (carro != null) {
            "${carro.modelo} • ${carro.anoFabricacao} • ${carro.tipo.rotulo}"
        } else {
            "Responda com atenção para um diagnóstico preciso."
        }
        Text(
            text = descricao,
            style = MaterialTheme.typography.bodyMedium,
            color = Branco.copy(alpha = 0.7f)
        )
    }
}

@Composable
private fun SecaoRevisao(
    estadoRevisao: EstadoRevisao,
    aoAtualizarQuilometragem: (String) -> Unit,
    aoAtualizarMesesRevisao: (String) -> Unit,
    aoSelecionarPreferencia: (PreferenciaRevisao) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, CinzaEscuro, RoundedCornerShape(20.dp))
            .background(CinzaEscuro, RoundedCornerShape(20.dp))
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Revisão do carro",
            style = MaterialTheme.typography.titleMedium,
            color = Branco
        )

        OutlinedTextField(
            value = estadoRevisao.quilometragemTotal,
            onValueChange = aoAtualizarQuilometragem,
            label = { Text("Quilometragem total") },
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
            value = estadoRevisao.mesesDesdeUltimaRevisao,
            onValueChange = aoAtualizarMesesRevisao,
            label = { Text("Meses desde a última revisão") },
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

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "Preferência de lembrete",
                style = MaterialTheme.typography.bodyLarge,
                color = Branco.copy(alpha = 0.85f)
            )
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                PreferenciaCard(
                    preferencia = PreferenciaRevisao.POR_KM,
                    selecionada = estadoRevisao.preferencia == PreferenciaRevisao.POR_KM,
                    aoSelecionar = { aoSelecionarPreferencia(PreferenciaRevisao.POR_KM) },
                    modifier = Modifier.weight(1f)
                )
                PreferenciaCard(
                    preferencia = PreferenciaRevisao.POR_TEMPO,
                    selecionada = estadoRevisao.preferencia == PreferenciaRevisao.POR_TEMPO,
                    aoSelecionar = { aoSelecionarPreferencia(PreferenciaRevisao.POR_TEMPO) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        if (estadoRevisao.erro != null) {
            Text(
                text = estadoRevisao.erro,
                style = MaterialTheme.typography.bodyMedium,
                color = Vermelho
            )
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
    val corFundo = if (selecionada) Verde else Preto
    val corBorda = if (selecionada) Verde else Branco.copy(alpha = 0.2f)
    val corTexto = if (selecionada) Preto else Branco

    Column(
        modifier = modifier
            .border(1.dp, corBorda, RoundedCornerShape(14.dp))
            .background(corFundo, RoundedCornerShape(14.dp))
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
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, CinzaEscuro, RoundedCornerShape(18.dp))
            .background(CinzaEscuro, RoundedCornerShape(18.dp))
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = pergunta.texto,
            style = MaterialTheme.typography.titleMedium,
            color = Branco
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
                        selectedColor = Verde,
                        unselectedColor = Branco.copy(alpha = 0.5f)
                    )
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = opcao.texto,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Branco.copy(alpha = 0.9f)
                )
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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Preto)
            .padding(20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            onClick = aoVoltar,
            colors = ButtonDefaults.buttonColors(
                containerColor = CinzaEscuro,
                contentColor = Branco
            ),
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text(text = "Voltar")
        }
        Button(
            onClick = aoVerResultado,
            enabled = habilitarResultado,
            colors = ButtonDefaults.buttonColors(
                containerColor = Verde,
                contentColor = Preto,
                disabledContainerColor = CinzaEscuro,
                disabledContentColor = Branco.copy(alpha = 0.6f)
            ),
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text(text = "Ver HealthLevel")
        }
    }
}
