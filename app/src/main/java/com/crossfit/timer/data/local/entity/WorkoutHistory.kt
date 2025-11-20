package com.crossfit.timer.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout_history")
data class WorkoutHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val date: Long,                     // timestamp en milisegundos
    val mode: String,                   // AMRAP, EMOM, FOR_TIME, CUSTOM
    val wodName: String? = null,        // Nombre del WOD si aplica

    val durationSeconds: Int,           // Tiempo total del entrenamiento
    val roundsCompleted: Int = 0,       // Rondas completadas

    val notes: String? = null,          // Notas del usuario
    val photoUri: String? = null,       // URI de la foto

    val configJson: String              // Configuración completa en JSON
)
