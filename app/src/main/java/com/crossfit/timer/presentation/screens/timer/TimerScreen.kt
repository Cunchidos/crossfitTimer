package com.crossfit.timer.presentation.screens.timer

import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.crossfit.timer.data.model.TimerConfig
import com.crossfit.timer.data.model.TimerMode
import com.crossfit.timer.data.model.TimerState
import com.crossfit.timer.presentation.SharedConfigViewModel
import com.crossfit.timer.util.TimeFormatter

@Composable
fun TimerScreen(
    mode: String,
    onBack: () -> Unit,
    viewModel: TimerViewModel = hiltViewModel(),
    sharedViewModel: SharedConfigViewModel = hiltViewModel()
) {
    // Efectos de la pantalla: forzar paisaje y mantenerla encendida
    ForceLandscape()
    KeepScreenOn()

    val config by sharedViewModel.sharedConfig.collectAsState()
    LaunchedEffect(config) {
        config?.let { timerConfig: TimerConfig ->
            viewModel.setConfig(timerConfig)
        }
    }
    val uiState by viewModel.uiState.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        when (val state = uiState.timerState) {
            is TimerState.Idle -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }
            is TimerState.Ready -> {
                ReadyDisplay(
                    onStart = { viewModel.startTimer() },
                    onBack = onBack // <-- Pasamos la acción de volver
                )
            }
            is TimerState.Countdown -> {
                CountdownDisplay(count = state.count)
            }
            is TimerState.Running, is TimerState.Paused -> {
                RunningStateLayout(
                    uiState = uiState,
                    isPaused = state is TimerState.Paused,
                    onPause = { viewModel.pauseTimer() },
                    onStart = { viewModel.startTimer() }, // Para reanudar
                    onStop = { viewModel.stopTimer() },
                    onAddRound = { viewModel.addRound() }
                )
            }
            is TimerState.Completed -> { // Pantalla de "COMPLETADO"
                CompletedDisplay(
                    mode = uiState.config.mode,
                    elapsedSeconds = uiState.elapsedSeconds,
                    rounds = uiState.currentRound,
                    onReset = { viewModel.resetTimer() },
                    onBack = onBack
                )
            }
            else -> {}
        }
    }
}


@Composable
fun RunningStateLayout(
    uiState: TimerUiState,
    isPaused: Boolean,
    onPause: () -> Unit,
    onStart: () -> Unit,
    onStop: () -> Unit,
    onAddRound: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.width(160.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically)
        ) {
            Button(
                onClick = if (isPaused) onStart else onPause,
                modifier = Modifier.fillMaxWidth().height(80.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isPaused) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary,
                    contentColor = if (isPaused) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onPrimary
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
            ) {
                Icon(
                    if (isPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                    contentDescription = if (isPaused) "Reanudar" else "Pausar",
                    modifier = Modifier.size(48.dp)
                )
            }

            OutlinedButton(
                onClick = onStop,
                modifier = Modifier.fillMaxWidth().height(70.dp),
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.secondary)
            ) {
                Icon(
                    Icons.Default.Stop,
                    contentDescription = "Detener",
                    modifier = Modifier.size(36.dp)
                )
            }
        }

        Text(
            text = TimeFormatter.formatTime(uiState.elapsedSeconds),
            modifier = Modifier.weight(1f),
            fontSize = 160.sp,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = if (isPaused) 0.6f else 1.0f),
            lineHeight = 160.sp
        )

        if (uiState.config.mode == TimerMode.AMRAP || uiState.config.mode == TimerMode.FOR_TIME) {
            Column(
                modifier = Modifier.width(160.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = uiState.currentRound.toString(),
                    fontSize = 90.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "RONDAS",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Light,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                    modifier = Modifier.offset(y = (-12).dp)
                )
                Spacer(Modifier.height(20.dp))
                FilledIconButton(
                    onClick = onAddRound,
                    modifier = Modifier.size(80.dp),
                    enabled = !isPaused
                ) {
                    Icon(Icons.Default.Add, "Añadir Ronda", modifier = Modifier.size(48.dp))
                }
            }
        } else {
            Spacer(Modifier.width(160.dp))
        }
    }
}

@Composable
fun CountdownDisplay(count: Int) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = if (count > 0) count.toString() else "GO!",
            fontSize = 300.sp,
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun ReadyDisplay(onStart: () -> Unit, onBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = onStart,
            modifier = Modifier.size(width = 300.dp, height = 120.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
        ) {
            Icon(Icons.Default.PlayArrow, "Iniciar", modifier = Modifier.size(48.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text("INICIAR", fontSize = 32.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.size(width = 250.dp, height = 60.dp)
        ) {
            Icon(Icons.Default.ArrowBack, "Volver", modifier = Modifier.size(24.dp))
            Spacer(Modifier.width(8.dp))
            Text("VOLVER", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun CompletedDisplay(mode: TimerMode, elapsedSeconds: Int, rounds: Int, onReset: () -> Unit, onBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("¡TERMINADO!", fontSize = 60.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(40.dp))
        Text("Tiempo: ${TimeFormatter.formatTime(elapsedSeconds)}", fontSize = 48.sp)
        if (mode == TimerMode.AMRAP || mode == TimerMode.FOR_TIME) {
            Spacer(Modifier.height(16.dp))
            Text("Rondas: $rounds", fontSize = 40.sp)
        }
        Spacer(Modifier.height(60.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                onClick = onBack, // Botón para volver a la home
                modifier = Modifier.size(width = 180.dp, height = 90.dp)
            ) {
                Icon(Icons.Default.Home, "Volver a inicio", modifier = Modifier.size(36.dp))
                Spacer(Modifier.width(12.dp))
                Text("INICIO", fontSize = 20.sp)
            }
            Button(
                onClick = onReset, // Botón para reiniciar el mismo timer
                modifier = Modifier.size(width = 220.dp, height = 90.dp)
            ) {
                Icon(Icons.Default.Refresh, "Reiniciar", modifier = Modifier.size(36.dp))
                Spacer(Modifier.width(12.dp))
                Text("REINICIAR", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// --- Screen Effects ---

@Composable
private fun KeepScreenOn() {
    val view = LocalView.current
    DisposableEffect(Unit) {
        view.keepScreenOn = true
        onDispose {
            view.keepScreenOn = false
        }
    }
}

@Composable
private fun ForceLandscape() {
    val context = LocalContext.current
    DisposableEffect(Unit) {
        val activity = context as? Activity
        val originalOrientation = activity?.requestedOrientation
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        onDispose {
            activity?.requestedOrientation = originalOrientation ?: ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }
}
