package com.example.carhealth.apresentacao

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.carhealth.domain.AssessmentQuestion
import com.example.carhealth.domain.Car
import com.example.carhealth.domain.CarFactory
import com.example.carhealth.domain.CarType
import com.example.carhealth.domain.HealthLevelCalculator
import com.example.carhealth.domain.HealthLevelDiagnostic
import com.example.carhealth.domain.MaintenanceCalculator
import com.example.carhealth.domain.MaintenanceData
import com.example.carhealth.domain.MaintenancePreference
import com.example.carhealth.domain.MaintenanceSummary
import java.util.Calendar

class HealthLevelViewModel : ViewModel() {
    var formState: InitialFormState by mutableStateOf(InitialFormState())
        private set

    var maintenanceState: MaintenanceState by mutableStateOf(MaintenanceState())
        private set

    var currentCar: Car? by mutableStateOf(null)
        private set

    var questions: List<AssessmentQuestion> by mutableStateOf(emptyList())
        private set

    private val selectedAnswers = mutableStateMapOf<String, Int>()

    var healthLevelDiagnostic: HealthLevelDiagnostic? by mutableStateOf(null)
        private set

    fun updateModel(newModel: String) {
        formState = formState.copy(model = newModel, error = null)
    }

    fun updateManufactureYear(newYear: String) {
        formState = formState.copy(manufactureYear = newYear, error = null)
    }

    fun selectCarType(type: CarType) {
        formState = formState.copy(selectedType = type, error = null)
    }

    fun updateTotalMileage(newMileage: String) {
        maintenanceState = maintenanceState.copy(totalMileage = newMileage, error = null)
    }

    fun updateMonthsSinceLastService(newMonths: String) {
        maintenanceState = maintenanceState.copy(monthsSinceLastService = newMonths, error = null)
    }

    fun selectMaintenancePreference(preference: MaintenancePreference) {
        maintenanceState = maintenanceState.copy(preference = preference, error = null)
    }

    fun startAssessment(): Boolean {
        val model = formState.model.trim()
        if (model.isBlank()) {
            formState = formState.copy(error = "Informe o modelo do carro.")
            return false
        }

        val year = validateYear(formState.manufactureYear)
        if (year == null) {
            formState = formState.copy(error = "Informe um ano de fabricação válido.")
            return false
        }

        val type = formState.selectedType
        if (type == null) {
            formState = formState.copy(error = "Selecione o tipo de carro.")
            return false
        }

        currentCar = CarFactory.createCar(type, model, year)
        questions = currentCar?.listObjectiveQuestions().orEmpty()
        selectedAnswers.clear()
        healthLevelDiagnostic = null
        maintenanceState = MaintenanceState()
        formState = formState.copy(error = null)
        return true
    }

    fun recordAnswer(questionId: String, optionIndex: Int) {
        selectedAnswers[questionId] = optionIndex
    }

    fun getSelectedAnswer(questionId: String): Int? {
        return selectedAnswers[questionId]
    }

    fun allAnswered(): Boolean {
        return questions.isNotEmpty() && selectedAnswers.size == questions.size
    }

    fun calculateResult(): HealthLevelDiagnostic? {
        if (!allAnswered()) {
            return null
        }
        val maintenanceData = validateMaintenance() ?: return null
        val car = currentCar ?: return null
        val maintenanceSummary = MaintenanceCalculator().calculate(maintenanceData, car.manufactureYear)
        val baseResult = HealthLevelCalculator().calculate(
            questions = questions,
            selectedAnswers = selectedAnswers.toMap(),
            extraPoints = maintenanceSummary.serviceScore,
            extraMaxPoints = 10
        )
        val suggestions = generateSuggestions(car, maintenanceSummary)
        val diagnostic = HealthLevelDiagnostic(
            result = baseResult,
            suggestions = suggestions,
            maintenanceSummary = maintenanceSummary
        )
        healthLevelDiagnostic = diagnostic
        return diagnostic
    }

    fun reset() {
        formState = InitialFormState()
        maintenanceState = MaintenanceState()
        currentCar = null
        questions = emptyList()
        selectedAnswers.clear()
        healthLevelDiagnostic = null
    }

    private fun validateYear(yearText: String): Int? {
        val year = yearText.trim().toIntOrNull() ?: return null
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        return if (year in 1886..currentYear) year else null
    }

    private fun validateMaintenance(): MaintenanceData? {
        val mileage = maintenanceState.totalMileage.trim().toIntOrNull()
        if (mileage == null || mileage < 0) {
            maintenanceState = maintenanceState.copy(error = "Informe a quilometragem total do carro.")
            return null
        }

        val months = maintenanceState.monthsSinceLastService.trim().toIntOrNull()
        if (months == null || months < 0) {
            maintenanceState = maintenanceState.copy(error = "Informe quantos meses fazem desde a última revisão.")
            return null
        }

        val preference = maintenanceState.preference
        if (preference == null) {
            maintenanceState = maintenanceState.copy(error = "Escolha como prefere o lembrete da revisão.")
            return null
        }

        return MaintenanceData(
            totalMileage = mileage,
            monthsSinceLastService = months,
            preference = preference
        )
    }

    private fun generateSuggestions(car: Car, maintenanceSummary: MaintenanceSummary): List<String> {
        val suggestions = mutableListOf<String>()
        val suggestionById = mapOf(
            "freios" to "Agende uma inspeção dos freios e verifique pastilhas e discos.",
            "pneus" to "Revise a calibragem e faça o rodízio dos pneus.",
            "suspensao" to "Avalie a suspensão para eliminar ruídos e melhorar o conforto.",
            "direcao" to "Cheque alinhamento e balanceamento para uma direção precisa.",
            "motor" to "Faça diagnóstico do motor para evitar falhas e perda de desempenho.",
            "fluidos" to "Complete ou troque fluidos para evitar desgaste prematuro.",
            "bateria" to "Monitore a saúde da bateria e programe manutenção preventiva."
        )

        questions.forEach { question ->
            val index = selectedAnswers[question.id] ?: return@forEach
            val points = question.options.getOrNull(index)?.points ?: 0
            if (points <= 6) {
                suggestionById[question.id]?.let { suggestions.add(it) }
            }
        }

        if (maintenanceSummary.isOverdue) {
            suggestions.add("Agende uma revisão completa o quanto antes para evitar riscos.")
        } else if (maintenanceSummary.daysToNextService in 0..30) {
            suggestions.add("Sua próxima revisão está próxima. Agende com antecedência.")
        }

        when (car.type) {
            CarType.ELECTRIC -> suggestions.add("Evite descargas profundas e priorize recargas entre 20% e 80%.")
            CarType.HYBRID -> suggestions.add("Alterne o uso dos modos para otimizar consumo e preservar a bateria.")
            CarType.COMBUSTION -> suggestions.add("Prefira abastecer em postos confiáveis e mantenha o motor aquecido.")
        }

        if (suggestions.isEmpty()) {
            suggestions.add("Seu carro está equilibrado. Mantenha a manutenção preventiva em dia.")
        }

        return suggestions.distinct()
    }
}
