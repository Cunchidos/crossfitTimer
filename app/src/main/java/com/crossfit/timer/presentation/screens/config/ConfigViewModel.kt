package com.crossfit.timer.presentation.screens.config

import androidx.lifecycle.ViewModel
import com.crossfit.timer.data.model.CustomInterval
import com.crossfit.timer.data.model.TimerConfig
import com.crossfit.timer.data.model.TimerMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ConfigViewModel @Inject constructor() : ViewModel() {

    private val _currentConfig = MutableStateFlow(TimerConfig(mode = TimerMode.FOR_TIME))
    val currentConfig: StateFlow<TimerConfig> = _currentConfig.asStateFlow()

    fun setMode(mode: TimerMode) {
        _currentConfig.update { it.copy(mode = mode) }
    }

    fun updateAmrapConfig(durationMinutes: Int, soundEnabled: Boolean, vibrationEnabled: Boolean) {
        _currentConfig.update {
            it.copy(
                amrapDurationSeconds = durationMinutes * 60,
                // ↓↓↓ ESTAS LÍNEAS FALTABAN AQUÍ ↓↓↓
                soundEnabled = soundEnabled,
                vibrationEnabled = vibrationEnabled
            )
        }
    }

    fun updateEmomConfig(rounds: Int, soundEnabled: Boolean, vibrationEnabled: Boolean) {
        _currentConfig.update {
            it.copy(
                emomRounds = rounds,
                soundEnabled = soundEnabled,
                vibrationEnabled = vibrationEnabled
            )
        }
    }

    fun updateForTimeConfig(
        useTimeCap: Boolean,
        timeCapMinutes: Int,
        soundEnabled: Boolean,
        vibrationEnabled: Boolean
    ) {
        _currentConfig.update {
            it.copy(
                forTimeWithCap = useTimeCap,
                forTimeCapSeconds = if (useTimeCap) timeCapMinutes * 60 else 0,
                soundEnabled = soundEnabled,
                vibrationEnabled = vibrationEnabled
            )
        }
    }

    fun updateCustomConfig(
        rounds: Int,
        intervals: List<CustomInterval>,
        soundEnabled: Boolean,
        vibrationEnabled: Boolean
    ) {
        _currentConfig.update {
            it.copy(
                customTotalRounds = rounds,
                customIntervals = intervals,
                soundEnabled = soundEnabled,
                vibrationEnabled = vibrationEnabled
            )
        }
    }

    fun addCustomInterval(interval: CustomInterval) {
        val currentIntervals = _currentConfig.value.customIntervals.toMutableList()
        currentIntervals.add(interval.copy(order = currentIntervals.size))
        _currentConfig.update { it.copy(customIntervals = currentIntervals) }
    }

    fun removeCustomInterval(index: Int) {
        val currentIntervals = _currentConfig.value.customIntervals.toMutableList()
        if (index in currentIntervals.indices) {
            currentIntervals.removeAt(index)
            // Reordenar
            currentIntervals.forEachIndexed { idx, interval ->
                currentIntervals[idx] = interval.copy(order = idx)
            }
            _currentConfig.update { it.copy(customIntervals = currentIntervals) }
        }
    }

    fun getConfig(): TimerConfig {
        return _currentConfig.value
    }
}
