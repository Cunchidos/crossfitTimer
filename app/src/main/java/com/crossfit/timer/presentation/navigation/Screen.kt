package com.crossfit.timer.presentation.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Timer : Screen("timer/{mode}") {
        fun createRoute(mode: String) = "timer/$mode"
    }
    object Config : Screen("config/{mode}") {
        fun createRoute(mode: String) = "config/$mode"
    }
    object History : Screen("history")
    object SavedWods : Screen("saved_wods")
    object Settings : Screen("settings")
    object Counter : Screen("counter") // <- Nueva ruta añadida
}
