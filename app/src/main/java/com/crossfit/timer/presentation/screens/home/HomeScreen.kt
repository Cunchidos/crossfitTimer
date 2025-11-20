package com.crossfit.timer.presentation.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.crossfit.timer.data.model.TimerMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToTimer: (String) -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToManualEntry: () -> Unit // <- Nuevo canal de navegación
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CrossFit Timer") }
            )
        },
        floatingActionButton = { // <- Aquí está el nuevo botón flotante
            FloatingActionButton(
                onClick = { onNavigateToManualEntry() },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Añadir registro")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Selecciona un modo",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            // Grid de modos de entrenamiento
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(getModes()) { mode ->
                    ModeCard(
                        mode = mode,
                        onClick = { onNavigateToTimer(mode.mode.name) }
                    )
                }
            }

            // Botones de navegación
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onNavigateToHistory,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Historial")
                }

                OutlinedButton(
                    onClick = onNavigateToSettings,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Configuración")
                }
            }
        }
    }
}

@Composable
fun ModeCard(
    mode: ModeInfo,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = mode.displayName,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = mode.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

data class ModeInfo(
    val mode: TimerMode,
    val displayName: String,
    val description: String
)

fun getModes(): List<ModeInfo> = listOf(
    ModeInfo(
        mode = TimerMode.AMRAP,
        displayName = "AMRAP",
        description = "As Many Rounds As Possible"
    ),
    ModeInfo(
        mode = TimerMode.EMOM,
        displayName = "EMOM",
        description = "Every Minute On the Minute"
    ),
    ModeInfo(
        mode = TimerMode.FOR_TIME,
        displayName = "For Time",
        description = "Cronómetro ascendente"
    ),
    ModeInfo(
        mode = TimerMode.CUSTOM,
        displayName = "Personalizado",
        description = "Intervalos personalizados"
    )
)
