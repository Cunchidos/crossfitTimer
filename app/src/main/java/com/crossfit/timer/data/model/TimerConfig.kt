
package com.crossfit.timer.data.model

data class TimerConfig(
    // Propiedades que ya estabas usando
    val mode: TimerMode,
    val amrapDurationSeconds: Int = 0,
    val emomRounds: Int = 0,
    val forTimeWithCap: Boolean = false,
    val forTimeCapSeconds: Int = 0,
    val customTotalRounds: Int = 0,
    val customIntervals: List<CustomInterval> = emptyList(),

    // Propiedades que causaban el error porque faltaban
    val soundEnabled: Boolean = true,
    val vibrationEnabled: Boolean = true
)
