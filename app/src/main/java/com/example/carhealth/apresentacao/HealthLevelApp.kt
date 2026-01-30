package com.example.carhealth.apresentacao

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.carhealth.apresentacao.telas.InitialScreen
import com.example.carhealth.apresentacao.telas.QuestionsScreen
import com.example.carhealth.apresentacao.telas.ResultScreen

@Composable
fun HealthLevelApp() {
    val navController = rememberNavController()
    val viewModel: HealthLevelViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Routes.START
    ) {
        composable(Routes.START) {
            InitialScreen(
                formState = viewModel.formState,
                onUpdateModel = viewModel::updateModel,
                onUpdateYear = viewModel::updateManufactureYear,
                onSelectType = viewModel::selectCarType,
                onContinue = {
                    if (viewModel.startAssessment()) {
                        navController.navigate(Routes.QUESTIONS)
                    }
                }
            )
        }
        composable(Routes.QUESTIONS) {
            QuestionsScreen(
                car = viewModel.currentCar,
                questions = viewModel.questions,
                maintenanceState = viewModel.maintenanceState,
                getSelectedAnswer = viewModel::getSelectedAnswer,
                recordAnswer = viewModel::recordAnswer,
                allAnswered = viewModel::allAnswered,
                onUpdateMileage = viewModel::updateTotalMileage,
                onUpdateMonthsSinceService = viewModel::updateMonthsSinceLastService,
                onSelectPreference = viewModel::selectMaintenancePreference,
                onBack = { navController.popBackStack() },
                onSeeResult = {
                    val diagnostic = viewModel.calculateResult()
                    if (diagnostic != null) {
                        navController.navigate(Routes.RESULT)
                    }
                }
            )
        }
        composable(Routes.RESULT) {
            ResultScreen(
                car = viewModel.currentCar,
                diagnostic = viewModel.healthLevelDiagnostic,
                onRestart = {
                    viewModel.reset()
                    navController.navigate(Routes.START) {
                        popUpTo(Routes.START) { inclusive = true }
                    }
                }
            )
        }
    }
}
