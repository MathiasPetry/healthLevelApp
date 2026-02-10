package com.example.carhealth.apresentacao

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.carhealth.dominio.CalculadoraHealthLevel
import com.example.carhealth.dominio.CalculadoraRevisao
import com.example.carhealth.dominio.Carro
import com.example.carhealth.dominio.DadosRevisao
import com.example.carhealth.dominio.DiagnosticoHealthLevel
import com.example.carhealth.dominio.FabricaCarro
import com.example.carhealth.dominio.PerguntaAvaliacao
import com.example.carhealth.dominio.PreferenciaRevisao
import com.example.carhealth.dominio.ResumoRevisao
import com.example.carhealth.dominio.TipoCarro
import java.util.Calendar

class HealthLevelViewModel : ViewModel() {
    var formState: InitialFormState by mutableStateOf(InitialFormState())
        private set

    var maintenanceState: MaintenanceState by mutableStateOf(MaintenanceState())
        private set

    var currentCar: Carro? by mutableStateOf(null)
        private set

    var questions: List<PerguntaAvaliacao> by mutableStateOf(emptyList())
        private set

    private val respostasSelecionadas = mutableStateMapOf<String, Int>()

    var healthLevelDiagnostic: DiagnosticoHealthLevel? by mutableStateOf(null)
        private set

    fun updateModel(newModel: String) {
        formState = formState.copy(modelo = newModel, erro = null)
    }

    fun updateManufactureYear(newYear: String) {
        formState = formState.copy(anoFabricacao = newYear, erro = null)
    }

    fun selectCarType(type: TipoCarro) {
        formState = formState.copy(tipoSelecionado = type, erro = null)
    }

    fun updateTotalMileage(newMileage: String) {
        maintenanceState = maintenanceState.copy(quilometragemTotal = newMileage, erro = null)
    }

    fun updateMonthsSinceLastService(newMonths: String) {
        maintenanceState = maintenanceState.copy(mesesDesdeUltimaRevisao = newMonths, erro = null)
    }

    fun selectMaintenancePreference(preference: PreferenciaRevisao) {
        maintenanceState = maintenanceState.copy(preferencia = preference, erro = null)
    }

    fun startAssessment(): Boolean {
        val modelo = formState.modelo.trim()
        if (modelo.isBlank()) {
            formState = formState.copy(erro = "Informe o modelo do carro.")
            return false
        }

        val anoFabricacao = validateYear(formState.anoFabricacao)
        if (anoFabricacao == null) {
            formState = formState.copy(erro = "Informe um ano de fabricação válido.")
            return false
        }

        val tipoSelecionado = formState.tipoSelecionado
        if (tipoSelecionado == null) {
            formState = formState.copy(erro = "Selecione o tipo de carro.")
            return false
        }

        currentCar = FabricaCarro.criarCarro(tipoSelecionado, modelo, anoFabricacao)
        questions = currentCar?.listarPerguntasObjetivas().orEmpty()
        respostasSelecionadas.clear()
        healthLevelDiagnostic = null
        maintenanceState = MaintenanceState()
        formState = formState.copy(erro = null)
        return true
    }

    fun recordAnswer(questionId: String, optionIndex: Int) {
        respostasSelecionadas[questionId] = optionIndex
    }

    fun getSelectedAnswer(questionId: String): Int? {
        return respostasSelecionadas[questionId]
    }

    fun allAnswered(): Boolean {
        return questions.isNotEmpty() && respostasSelecionadas.size == questions.size
    }

    fun calculateResult(): DiagnosticoHealthLevel? {
        if (!allAnswered()) {
            return null
        }

        val dadosRevisao = validateMaintenance() ?: return null
        val carroAtual = currentCar ?: return null
        val resumoRevisao = CalculadoraRevisao().calcular(dadosRevisao, carroAtual.anoFabricacao)
        val resultadoBase = CalculadoraHealthLevel().calcular(
            perguntas = questions,
            respostasSelecionadas = respostasSelecionadas.toMap(),
            pontosExtras = resumoRevisao.pontuacaoRevisao,
            pontosMaximosExtras = 10
        )
        val sugestoes = generateSuggestions(carroAtual, resumoRevisao)
        val diagnostico = DiagnosticoHealthLevel(
            resultado = resultadoBase,
            sugestoes = sugestoes,
            resumoRevisao = resumoRevisao
        )
        healthLevelDiagnostic = diagnostico
        return diagnostico
    }

    fun reset() {
        formState = InitialFormState()
        maintenanceState = MaintenanceState()
        currentCar = null
        questions = emptyList()
        respostasSelecionadas.clear()
        healthLevelDiagnostic = null
    }

    private fun validateYear(yearText: String): Int? {
        val year = yearText.trim().toIntOrNull() ?: return null
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        return if (year in 1886..currentYear) year else null
    }

    private fun validateMaintenance(): DadosRevisao? {
        val quilometragemTotal = maintenanceState.quilometragemTotal.trim().toIntOrNull()
        if (quilometragemTotal == null || quilometragemTotal < 0) {
            maintenanceState = maintenanceState.copy(erro = "Informe a quilometragem total do carro.")
            return null
        }

        val mesesDesdeUltimaRevisao = maintenanceState.mesesDesdeUltimaRevisao.trim().toIntOrNull()
        if (mesesDesdeUltimaRevisao == null || mesesDesdeUltimaRevisao < 0) {
            maintenanceState = maintenanceState.copy(erro = "Informe quantos meses fazem desde a última revisão.")
            return null
        }

        val preferenciaRevisao = maintenanceState.preferencia
        if (preferenciaRevisao == null) {
            maintenanceState = maintenanceState.copy(erro = "Escolha como prefere o lembrete da revisão.")
            return null
        }

        return DadosRevisao(
            quilometragemTotal = quilometragemTotal,
            mesesDesdeUltimaRevisao = mesesDesdeUltimaRevisao,
            preferencia = preferenciaRevisao
        )
    }

    private fun generateSuggestions(car: Carro, maintenanceSummary: ResumoRevisao): List<String> {
        val sugestoes = mutableListOf<String>()
        val sugestaoPorId = mapOf(
            "freios" to "Agende uma inspeção dos freios e verifique o estado das pastilhas e dos discos.",
            "pneus" to "Revise a calibragem dos pneus e realize o rodízio para garantir desgaste uniforme.",
            "suspensao" to "Avalie o sistema de suspensão para eliminar ruídos e melhorar o conforto ao dirigir.",
            "direcao" to "Cheque o alinhamento e o balanceamento para garantir precisão e estabilidade na direção.",
            "motor" to "Realize um diagnóstico do motor para prevenir falhas e perda de desempenho.",
            "fluidos" to "Verifique e substitua os fluidos do veículo para evitar desgaste prematuro dos componentes.",
            "bateria" to "Monitore a condição da bateria e programe manutenções preventivas."
        )

        questions.forEach { question ->
            val indiceResposta = respostasSelecionadas[question.id] ?: return@forEach
            val pontosOpcao = question.opcoes.getOrNull(indiceResposta)?.pontos ?: 0
            if (pontosOpcao <= 6) {
                sugestaoPorId[question.id]?.let { sugestoes.add(it) }
            }
        }

        if (maintenanceSummary.revisaoAtrasada) {
            sugestoes.add("Agende uma revisão completa o quanto antes para evitar riscos.")
        } else if (maintenanceSummary.diasParaProximaRevisao in 0..30) {
            sugestoes.add("Sua proxima revisão esta próxima. Agende com antecedência.")
        }

        when (car.tipo) {
            TipoCarro.ELETRICO -> sugestoes.add("Evite descargas profundas e priorize recargas entre 20% e 80%.")
            TipoCarro.HIBRIDO -> sugestoes.add("Alterne o uso dos modos para otimizar consumo e preservar a bateria.")
            TipoCarro.COMBUSTAO -> sugestoes.add("Prefira abastecer em postos confiáveis e mantenha o motor aquecido.")
        }

        if (sugestoes.isEmpty()) {
            sugestoes.add("Seu carro está equilibrado. Mantenha a manutenção preventiva em dia.")
        }

        return sugestoes.distinct()
    }
}
