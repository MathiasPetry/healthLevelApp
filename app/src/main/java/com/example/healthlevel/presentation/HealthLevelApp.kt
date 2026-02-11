package com.example.healthlevel.presentation

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.healthlevel.presentation.screens.InitialScreen
import com.example.healthlevel.presentation.screens.QuestionsScreen
import com.example.healthlevel.presentation.screens.ResultScreen

@Composable
fun HealthLevelApp(
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit
) {
    val navController = rememberNavController()
    val viewModel: HealthLevelViewModel = viewModel()

    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = Routes.START,
            modifier = Modifier.fillMaxSize()
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

        ThemeToggleButton(
            isDarkTheme = isDarkTheme,
            onToggleTheme = onToggleTheme,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .statusBarsPadding()
                .padding(top = 10.dp, end = 18.dp)
        )
    }
}

@Composable
private fun ThemeToggleButton(
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme

    FloatingActionButton(
        onClick = onToggleTheme,
        modifier = modifier
            .size(46.dp)
            .border(1.dp, colors.outline.copy(alpha = 0.45f), CircleShape),
        shape = CircleShape,
        containerColor = colors.surface.copy(alpha = 0.92f),
        contentColor = colors.onSurface,
        elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = 5.dp,
            pressedElevation = 8.dp,
            focusedElevation = 6.dp,
            hoveredElevation = 6.dp
        )
    ) {
        Icon(
            imageVector = if (isDarkTheme) Icons.Rounded.LightMode else Icons.Rounded.DarkMode,
            contentDescription = "Alternar tema"
        )
    }
}
