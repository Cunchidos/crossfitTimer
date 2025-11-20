package com.crossfit.timer.data.model

enum class TimerMode {
    AMRAP,      // As Many Rounds As Possible
    EMOM,       // Every Minute On the Minute
    FOR_TIME,   // Cronómetro ascendente con Time Cap opcional
    CUSTOM,     // Intervalos personalizados
    COUNTER     // Contador de rondas simple
}
