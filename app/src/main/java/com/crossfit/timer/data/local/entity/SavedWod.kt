package com.crossfit.timer.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_wods")
data class SavedWod(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val name: String,                   // "Murph", "Mi WOD", etc.
    val mode: String,                   // AMRAP, EMOM, FOR_TIME, CUSTOM

    val description: String? = null,    // Descripción opcional
    val photoUri: String? = null,       // URI de foto opcional

    val configJson: String,             // Configuración completa en JSON

    val createdAt: Long,                // Timestamp de creación
    val lastUsed: Long? = null          // Timestamp del último uso
)
