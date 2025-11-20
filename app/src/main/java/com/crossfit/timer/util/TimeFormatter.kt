package com.crossfit.timer.util

object TimeFormatter {

    /**
     * Formatea segundos a formato MM:SS
     */
    fun formatTime(totalSeconds: Int): String {
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    /**
     * Formatea segundos a formato HH:MM:SS si es necesario
     */
    fun formatTimeLong(totalSeconds: Int): String {
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60

        return if (hours > 0) {
            String.format("%02d:%02d:%02d", hours, minutes, seconds)
        } else {
            String.format("%02d:%02d", minutes, seconds)
        }
    }

    /**
     * Convierte minutos a segundos
     */
    fun minutesToSeconds(minutes: Int): Int = minutes * 60

    /**
     * Convierte segundos a minutos (redondeado)
     */
    fun secondsToMinutes(seconds: Int): Int = seconds / 60
}
