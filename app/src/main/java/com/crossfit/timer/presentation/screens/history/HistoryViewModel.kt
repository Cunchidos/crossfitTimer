package com.crossfit.timer.presentation.screens.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crossfit.timer.data.local.dao.WorkoutHistoryDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val workoutHistoryDao: WorkoutHistoryDao
) : ViewModel() {

    private val dateFormatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    val historyList: StateFlow<List<WorkoutHistoryItem>> = workoutHistoryDao
        .getAllHistory()
        .map { workouts ->
            workouts.map { workout ->
                WorkoutHistoryItem(
                    id = workout.id,
                    date = workout.date,
                    formattedDate = dateFormatter.format(Date(workout.date)),
                    mode = workout.mode,
                    wodName = workout.wodName,
                    durationSeconds = workout.durationSeconds,
                    roundsCompleted = workout.roundsCompleted,
                    notes = workout.notes,
                    photoUri = workout.photoUri
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun deleteWorkout(id: Long) {
        viewModelScope.launch {
            workoutHistoryDao.deleteById(id)
        }
    }
}
