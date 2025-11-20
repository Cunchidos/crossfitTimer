package com.crossfit.timer.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CustomInterval(
    val id: Int = 0,
    val name: String,               // "Trabajo", "Descanso", "Sprint", etc.
    val durationSeconds: Int,       // Duración en segundos
    val type: IntervalType,         // WORK o REST
    val order: Int = 0              // Orden en la secuencia
)
