package com.crossfit.timer.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.crossfit.timer.presentation.SharedConfigViewModel
import com.crossfit.timer.presentation.screens.counter.CounterScreen
import com.crossfit.timer.presentation.screens.home.HomeScreen

@Composable
fun NavGraph(
    navController: NavHostController
) {
    // Crear una instancia compartida del SharedConfigViewModel a nivel del NavGraph
    val sharedConfigViewModel: SharedConfigViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToTimer = { mode ->
                    navController.navigate(Screen.Config.createRoute(mode))
                },
                onNavigateToHistory = {
                    navController.navigate(Screen.History.route)
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                },
                onNavigateToCounter = { // <- Acción conectada
                    navController.navigate(Screen.Counter.route)
                }
            )
        }

        composable(Screen.Counter.route) { // <- Destino final conectado
            CounterScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.Config.route,
            arguments = listOf(
                navArgument("mode") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val mode = backStackEntry.arguments?.getString("mode") ?: "FOR_TIME"
            com.crossfit.timer.presentation.screens.config.ConfigScreen(
                mode = mode,
                onBack = { navController.popBackStack() },
                onStartTimer = { navController.navigate(Screen.Timer.createRoute(mode)) },
                sharedViewModel = sharedConfigViewModel
            )
        }

        composable(
            route = Screen.Timer.route,
            arguments = listOf(
                navArgument("mode") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val mode = backStackEntry.arguments?.getString("mode") ?: "FOR_TIME"
            com.crossfit.timer.presentation.screens.timer.TimerScreen(
                mode = mode,
                onBack = { navController.popBackStack() },
                sharedViewModel = sharedConfigViewModel
            )
        }

        composable(Screen.History.route) {
            com.crossfit.timer.presentation.screens.history.HistoryScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Settings.route) {
            com.crossfit.timer.presentation.screens.settings.SettingsScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}
