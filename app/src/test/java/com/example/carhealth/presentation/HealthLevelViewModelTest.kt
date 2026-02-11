package com.example.carhealth.presentation

import com.example.carhealth.domain.TipoCarro
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class HealthLevelViewModelTest {

    @Test
    fun `falha quando modelo esta em branco`() {
        val viewModel = HealthLevelViewModel()
        viewModel.updateManufactureYear("2020")
        viewModel.selectCarType(TipoCarro.ELETRICO)

        val iniciou = viewModel.startAssessment()

        assertFalse(iniciou)
        assertEquals("Informe o modelo do carro.", viewModel.formState.erro)
    }

    @Test
    fun `falha quando ano de fabricacao e invalido`() {
        val viewModel = HealthLevelViewModel()
        viewModel.updateModel("Onix")
        viewModel.updateManufactureYear("9999")
        viewModel.selectCarType(TipoCarro.COMBUSTAO)

        val iniciou = viewModel.startAssessment()

        assertFalse(iniciou)
        assertEquals("Informe um ano de fabricação válido.", viewModel.formState.erro)
    }

    @Test
    fun `falha quando tipo de carro nao foi selecionado`() {
        val viewModel = HealthLevelViewModel()
        viewModel.updateModel("Onix")
        viewModel.updateManufactureYear("2020")

        val iniciou = viewModel.startAssessment()

        assertFalse(iniciou)
        assertEquals("Selecione o tipo de carro.", viewModel.formState.erro)
    }

    @Test
    fun `inicia avaliacao com dados validos`() {
        val viewModel = HealthLevelViewModel()
        viewModel.updateModel("Onix")
        viewModel.updateManufactureYear("2020")
        viewModel.selectCarType(TipoCarro.HIBRIDO)

        val iniciou = viewModel.startAssessment()

        assertTrue(iniciou)
        assertNotNull(viewModel.currentCar)
        assertTrue(viewModel.questions.isNotEmpty())
        assertNull(viewModel.formState.erro)
    }
}
