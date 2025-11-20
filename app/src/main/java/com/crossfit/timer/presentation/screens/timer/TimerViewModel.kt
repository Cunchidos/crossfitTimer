package com.crossfit.timer.presentation.screens.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crossfit.timer.data.model.*
import com.crossfit.timer.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimerViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(TimerUiState())
    val uiState: StateFlow<TimerUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null
    private var startTimeMillis: Long = 0

    fun setConfig(config: TimerConfig) {
        _uiState.update {
            it.copy(
                config = config,
                timerState = TimerState.Ready // State is Ready once config is set
            )
        }
    }

    fun startTimer() {
        when (_uiState.value.timerState) {
            is TimerState.Ready -> { // Only start from Ready
                startCountdown()
            }
            is TimerState.Paused -> {
                resumeTimer()
            }
            else -> {}
        }
    }

    fun pauseTimer() {
        if (_uiState.value.timerState is TimerState.Running) {
            timerJob?.cancel()
            _uiState.update { it.copy(timerState = TimerState.Paused) }
        }
    }

    fun stopTimer() {
        timerJob?.cancel()
        _uiState.update {
            it.copy(
                timerState = TimerState.Ready, // Go back to Ready, not Idle
                elapsedSeconds = 0,
                currentRound = 0,
                currentIntervalIndex = 0
            )
        }
    }

    fun resetTimer() {
        stopTimer()
    }

    fun addRound() {
        val config = _uiState.value.config
        if (config.mode == TimerMode.AMRAP || config.mode == TimerMode.FOR_TIME) {
            _uiState.update { it.copy(currentRound = it.currentRound + 1) }
        }
    }

    private fun startCountdown() {
        _uiState.update { it.copy(timerState = TimerState.Countdown(Constants.DEFAULT_COUNTDOWN_SECONDS)) }

        timerJob = viewModelScope.launch {
            for (count in Constants.DEFAULT_COUNTDOWN_SECONDS downTo 1) {
                _uiState.update { it.copy(timerState = TimerState.Countdown(count)) }
                delay(1000)
            }
            startMainTimer()
        }
    }

    private fun startMainTimer() {
        startTimeMillis = System.currentTimeMillis()
        _uiState.update {
            it.copy(
                timerState = TimerState.Running,
                currentRound = if (it.config.mode == TimerMode.AMRAP || it.config.mode == TimerMode.FOR_TIME) 0 else 1
            )
        }

        timerJob = viewModelScope.launch {
            while (_uiState.value.timerState is TimerState.Running) {
                delay(100)

                val currentMillis = System.currentTimeMillis()
                val elapsedMillis = currentMillis - startTimeMillis
                val elapsedSeconds = (elapsedMillis / 1000).toInt()

                _uiState.update { it.copy(elapsedSeconds = elapsedSeconds) }

                when (_uiState.value.config.mode) {
                    TimerMode.AMRAP -> handleAmrapTick()
                    TimerMode.EMOM -> handleEmomTick()
                    TimerMode.FOR_TIME -> handleForTimeTick()
                    TimerMode.CUSTOM -> handleCustomTick()
                }
            }
        }
    }

    private fun resumeTimer() {
        val pausedSeconds = _uiState.value.elapsedSeconds
        startTimeMillis = System.currentTimeMillis() - (pausedSeconds * 1000L)

        _uiState.update { it.copy(timerState = TimerState.Running) }

        timerJob = viewModelScope.launch {
            while (_uiState.value.timerState is TimerState.Running) {
                delay(100)

                val currentMillis = System.currentTimeMillis()
                val elapsedMillis = currentMillis - startTimeMillis
                val elapsedSeconds = (elapsedMillis / 1000).toInt()

                _uiState.update { it.copy(elapsedSeconds = elapsedSeconds) }

                when (_uiState.value.config.mode) {
                    TimerMode.AMRAP -> handleAmrapTick()
                    TimerMode.EMOM -> handleEmomTick()
                    TimerMode.FOR_TIME -> handleForTimeTick()
                    TimerMode.CUSTOM -> handleCustomTick()
                }
            }
        }
    }

    private fun handleAmrapTick() {
        val config = _uiState.value.config
        val elapsed = _uiState.value.elapsedSeconds

        if (elapsed >= config.amrapDurationSeconds) {
            completeTimer()
            return
        }
    }

    private fun handleEmomTick() {
        val config = _uiState.value.config
        val elapsed = _uiState.value.elapsedSeconds

        val currentRound = (elapsed / 60) + 1

        if (currentRound > config.emomRounds) {
            completeTimer()
            return
        }

        _uiState.update { it.copy(currentRound = currentRound) }
    }

    private fun handleForTimeTick() {
        val config = _uiState.value.config
        val elapsed = _uiState.value.elapsedSeconds

        if (config.forTimeWithCap && elapsed >= config.forTimeCapSeconds) {
            completeTimer()
            return
        }
    }

    private fun handleCustomTick() {
        val config = _uiState.value.config
        val intervals = config.customIntervals

        if (intervals.isEmpty()) {
            completeTimer()
            return
        }

        val roundDuration = intervals.sumOf { it.durationSeconds }
        val totalDuration = roundDuration * config.customTotalRounds

        val elapsed = _uiState.value.elapsedSeconds

        if (elapsed >= totalDuration) {
            completeTimer()
            return
        }

        val currentRound = (elapsed / roundDuration) + 1
        val timeInRound = elapsed % roundDuration

        var accumulatedTime = 0
        var currentIntervalIndex = 0

        for ((index, interval) in intervals.withIndex()) {
            if (timeInRound < accumulatedTime + interval.durationSeconds) {
                currentIntervalIndex = index
                break
            }
            accumulatedTime += interval.durationSeconds
        }

        _uiState.update {
            it.copy(
                currentRound = currentRound,
                currentIntervalIndex = currentIntervalIndex
            )
        }
    }

    private fun completeTimer() {
        timerJob?.cancel()
        _uiState.update { it.copy(timerState = TimerState.Completed) }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}

data class TimerUiState(
    val config: TimerConfig = TimerConfig(mode = TimerMode.FOR_TIME),
    val timerState: TimerState = TimerState.Ready, // Default state is Ready
    val elapsedSeconds: Int = 0,
    val currentRound: Int = 0,
    val currentIntervalIndex: Int = 0
)
