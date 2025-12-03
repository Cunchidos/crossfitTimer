package com.crossfit.timer.presentation.screens.counter

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Lista de colores para el fondo
private val backgroundColors = listOf(
    Color(0xFF1E1E1E), // Dark Gray
    Color(0xFFB71C1C), // Strong Red
    Color(0xFF0D47A1), // Strong Blue
    Color(0xFF1B5E20), // Strong Green
    Color(0xFFFF6F00), // Strong Orange
    Color(0xFF4A148C)  // Strong Purple
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CounterScreen(
    onBack: () -> Unit
) {
    var count by remember { mutableStateOf(0) }
    var colorIndex by remember { mutableStateOf(0) }

    KeepScreenOn()

    Scaffold(
        // Asignamos el color de fondo dinámicamente
        containerColor = backgroundColors[colorIndex],
        topBar = {
            TopAppBar(
                title = { Text("Contador de Rondas") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent, titleContentColor = Color.White)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { 
                    count = 0
                    colorIndex = 0 // También reiniciamos el color
                },
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            ) {
                Icon(Icons.Default.Refresh, contentDescription = "Reiniciar contador")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .clickable( 
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    count++
                    // Cambiamos al siguiente color de la lista, volviendo al principio si llegamos al final
                    colorIndex = (colorIndex + 1) % backgroundColors.size
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = count.toString(),
                fontSize = 300.sp,
                fontWeight = FontWeight.Black,
                // El color del texto cambia para ser siempre visible sobre el fondo oscuro
                color = Color.White.copy(alpha = 0.9f) 
            )
        }
    }
}


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