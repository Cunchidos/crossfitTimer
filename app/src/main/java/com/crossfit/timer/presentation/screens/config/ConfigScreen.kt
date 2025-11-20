package com.crossfit.timer.presentation.screens.config

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.crossfit.timer.data.model.CustomInterval
import com.crossfit.timer.data.model.IntervalType
import com.crossfit.timer.data.model.TimerConfig
import com.crossfit.timer.data.model.TimerMode
import com.crossfit.timer.presentation.SharedConfigViewModel
import com.crossfit.timer.util.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfigScreen(
    mode: String,
    onBack: () -> Unit,
    onStartTimer: () -> Unit,
    sharedViewModel: SharedConfigViewModel = hiltViewModel()
) {
    val timerMode = try {
        TimerMode.valueOf(mode)
    } catch (e: Exception) {
        TimerMode.FOR_TIME
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configurar ${getModeDisplayName(timerMode)}") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when (timerMode) {
                TimerMode.AMRAP -> AmrapConfig(
                    onStartTimer = { config ->
                        sharedViewModel.setConfig(config)
                        onStartTimer()
                    }
                )
                TimerMode.EMOM -> EmomConfig(
                    onStartTimer = { config ->
                        sharedViewModel.setConfig(config)
                        onStartTimer()
                    }
                )
                TimerMode.FOR_TIME -> ForTimeConfig(
                    onStartTimer = { config ->
                        sharedViewModel.setConfig(config)
                        onStartTimer()
                    }
                )
                TimerMode.CUSTOM -> CustomConfig(
                    onStartTimer = { config ->
                        sharedViewModel.setConfig(config)
                        onStartTimer()
                    }
                )
            }
        }
    }
}

@Composable
fun AmrapConfig(onStartTimer: (TimerConfig) -> Unit) {
    var durationMinutes by remember { mutableStateOf(Constants.DEFAULT_AMRAP_MINUTES.toString()) }

    ConfigSection(title = "Duración") {
        OutlinedTextField(
            value = durationMinutes,
            onValueChange = { durationMinutes = it.filter { char -> char.isDigit() } },
            label = { Text("Minutos") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
    }

    Spacer(modifier = Modifier.height(16.dp))

    Button(
        onClick = {
            val minutes = durationMinutes.toIntOrNull() ?: Constants.DEFAULT_AMRAP_MINUTES
            val config = TimerConfig(
                mode = TimerMode.AMRAP,
                amrapDurationSeconds = minutes * 60
            )
            onStartTimer(config)
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("INICIAR AMRAP")
    }
}

@Composable
fun EmomConfig(onStartTimer: (TimerConfig) -> Unit) {
    var totalRounds by remember { mutableStateOf(Constants.DEFAULT_EMOM_ROUNDS.toString()) }

    ConfigSection(title = "Configuración") {
        OutlinedTextField(
            value = totalRounds,
            onValueChange = { totalRounds = it.filter { char -> char.isDigit() } },
            label = { Text("Número de rondas (minutos)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
    }

    Spacer(modifier = Modifier.height(16.dp))

    Button(
        onClick = {
            val rounds = totalRounds.toIntOrNull() ?: Constants.DEFAULT_EMOM_ROUNDS
            val config = TimerConfig(
                mode = TimerMode.EMOM,
                emomRounds = rounds
            )
            onStartTimer(config)
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("INICIAR EMOM")
    }
}

@Composable
fun ForTimeConfig(onStartTimer: (TimerConfig) -> Unit) {
    var useTimeCap by remember { mutableStateOf(false) }
    var timeCapMinutes by remember { mutableStateOf("20") }

    ConfigSection(title = "Time Cap (opcional)") {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Usar Time Cap")
            Switch(
                checked = useTimeCap,
                onCheckedChange = { useTimeCap = it }
            )
        }

        if (useTimeCap) {
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = timeCapMinutes,
                onValueChange = { timeCapMinutes = it.filter { char -> char.isDigit() } },
                label = { Text("Minutos") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    Button(
        onClick = {
            val capMinutes = timeCapMinutes.toIntOrNull() ?: 20
            val config = TimerConfig(
                mode = TimerMode.FOR_TIME,
                forTimeWithCap = useTimeCap,
                forTimeCapSeconds = if (useTimeCap) capMinutes * 60 else 0
            )
            onStartTimer(config)
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("INICIAR FOR TIME")
    }
}

@Composable
fun CustomConfig(onStartTimer: (TimerConfig) -> Unit) {
    var totalRounds by remember { mutableStateOf("3") }
    var intervals by remember { mutableStateOf(listOf<CustomInterval>()) }
    var showAddDialog by remember { mutableStateOf(false) }

    ConfigSection(title = "Configuración") {
        OutlinedTextField(
            value = totalRounds,
            onValueChange = { totalRounds = it.filter { char -> char.isDigit() } },
            label = { Text("Número de rondas") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
    }

    ConfigSection(title = "Intervalos") {
        if (intervals.isEmpty()) {
            Text(
                text = "No hay intervalos configurados.\nAgrega al menos uno para comenzar.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        } else {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                intervals.forEachIndexed { index, interval ->
                    IntervalItem(
                        interval = interval,
                        index = index,
                        onDelete = {
                            intervals = intervals.toMutableList().apply { removeAt(index) }
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = { showAddDialog = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Agregar Intervalo")
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    Button(
        onClick = {
            val rounds = totalRounds.toIntOrNull() ?: 3
            val config = TimerConfig(
                mode = TimerMode.CUSTOM,
                customTotalRounds = rounds,
                customIntervals = intervals
            )
            onStartTimer(config)
        },
        modifier = Modifier.fillMaxWidth(),
        enabled = intervals.isNotEmpty()
    ) {
        Text("INICIAR CUSTOM")
    }

    if (showAddDialog) {
        AddIntervalDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { interval ->
                intervals = intervals + interval.copy(
                    id = intervals.size,
                    order = intervals.size
                )
                showAddDialog = false
            }
        )
    }
}

@Composable
fun IntervalItem(
    interval: CustomInterval,
    index: Int,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (interval.type == IntervalType.WORK)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${index + 1}. ${interval.name}",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${interval.durationSeconds}s - ${if (interval.type == IntervalType.WORK) "Trabajo" else "Descanso"}",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddIntervalDialog(
    onDismiss: () -> Unit,
    onAdd: (CustomInterval) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var durationSeconds by remember { mutableStateOf("30") }
    var intervalType by remember { mutableStateOf(IntervalType.WORK) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Agregar Intervalo") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre") },
                    placeholder = { Text("ej: Burpees") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = durationSeconds,
                    onValueChange = { durationSeconds = it.filter { char -> char.isDigit() } },
                    label = { Text("Duración (segundos)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = intervalType == IntervalType.WORK,
                        onClick = { intervalType = IntervalType.WORK },
                        label = { Text("Trabajo") },
                        modifier = Modifier.weight(1f)
                    )
                    FilterChip(
                        selected = intervalType == IntervalType.REST,
                        onClick = { intervalType = IntervalType.REST },
                        label = { Text("Descanso") },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (name.isNotBlank() && durationSeconds.isNotBlank()) {
                        onAdd(
                            CustomInterval(
                                name = name,
                                durationSeconds = durationSeconds.toIntOrNull() ?: 30,
                                type = intervalType
                            )
                        )
                    }
                }
            ) {
                Text("Agregar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun ConfigSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            content()
        }
    }
}

private fun getModeDisplayName(mode: TimerMode): String {
    return when (mode) {
        TimerMode.AMRAP -> "AMRAP"
        TimerMode.EMOM -> "EMOM"
        TimerMode.FOR_TIME -> "For Time"
        TimerMode.CUSTOM -> "Custom"
    }
}
