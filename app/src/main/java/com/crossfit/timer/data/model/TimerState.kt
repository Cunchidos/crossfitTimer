package com.crossfit.timer.data.model

sealed class TimerState {
    object Idle : TimerState()                      // En reposo, sin configurar
    object Ready : TimerState()                     // Configurado, listo para countdown
    data class Countdown(val count: Int) : TimerState()  // 3-2-1-GO
    object Running : TimerState()                   // Timer en ejecución
    object Paused : TimerState()                    // Timer pausado
    object Completed : TimerState()                 // Timer finalizado
}
