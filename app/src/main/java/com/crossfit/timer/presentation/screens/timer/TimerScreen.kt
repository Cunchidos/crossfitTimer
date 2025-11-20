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
    ForceLandscape()
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
                ReadyDisplay(onStart = { viewModel.startTimer() })
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
                    onReset = { viewModel.resetTimer() }
                )
            }
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
                    containerColor = if (isPaused) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onError
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
            ) {
                Icon(
                    if (isPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                    contentDescription = null,
                    modifier = Modifier.size(36.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(if (isPaused) "SEGUIR" else "PAUSAR", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }

            OutlinedButton(
                onClick = onStop,
                modifier = Modifier.fillMaxWidth().height(70.dp),
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f)),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.9f))
            ) {
                Icon(Icons.Default.Stop, null)
                Spacer(Modifier.width(8.dp))
                Text("DETENER", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
        }

        Text(
            text = TimeFormatter.formatTime(uiState.elapsedSeconds),
            modifier = Modifier.weight(1f),
            fontSize = 160.sp,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center,
            color = if (isPaused) MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f) else MaterialTheme.colorScheme.onBackground,
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
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
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
                    enabled = !isPaused,
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                        disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f)
                    )
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
fun ReadyDisplay(onStart: () -> Unit) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Button(
            onClick = onStart,
            modifier = Modifier.size(width = 300.dp, height = 120.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
        ) {
            Icon(Icons.Default.PlayArrow, "Iniciar", modifier = Modifier.size(48.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text("INICIAR", fontSize = 32.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun CompletedDisplay(mode: TimerMode, elapsedSeconds: Int, rounds: Int, onReset: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("¡TERMINADO!", fontSize = 60.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(40.dp))
        Text("Tiempo: ${TimeFormatter.formatTime(elapsedSeconds)}", fontSize = 48.sp, color = MaterialTheme.colorScheme.onBackground)
        if (mode == TimerMode.AMRAP || mode == TimerMode.FOR_TIME) {
            Spacer(Modifier.height(16.dp))
            Text("Rondas: $rounds", fontSize = 40.sp, color = MaterialTheme.colorScheme.onBackground)
        }
        Spacer(Modifier.height(60.dp))
        Button(
            onClick = onReset,
            modifier = Modifier.size(width = 280.dp, height = 90.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Icon(Icons.Default.Refresh, "Reiniciar", modifier = Modifier.size(36.dp))
            Spacer(Modifier.width(16.dp))
            Text("REINICIAR", fontSize = 28.sp, fontWeight = FontWeight.Bold)
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
