package br.com.calculadoraimc

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val myViewModel: ImcViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "input"
    ) {

        composable("input") {
            InputScreen(
                viewModel = myViewModel,
                onCalculateSuccess = {
                    navController.navigate("result")
                }
            )
        }

        composable("result") {
            ResultScreen(
                viewModel = myViewModel,
                onNavigateRecommendations = {
                    navController.navigate("recommendation")
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable("recommendation") {
            RecommendationScreen(
                viewModel = myViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}




