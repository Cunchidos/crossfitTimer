# CrossFit Timer - Agent Documentation

## Descripción del Proyecto

CrossFit Timer es una aplicación Android nativa desarrollada en Kotlin y Jetpack Compose que proporciona temporizadores especializados para entrenamientos de CrossFit. La aplicación soporta múltiples modos de entrenamiento (AMRAP, EMOM, For Time, Custom) con configuraciones personalizables.

## Arquitectura de la Aplicación

### Tecnologías Principales
- **Lenguaje**: Kotlin
- **UI Framework**: Jetpack Compose
- **Arquitectura**: MVVM (Model-View-ViewModel)
- **Dependency Injection**: Dagger Hilt
- **Base de Datos**: Room
- **Navegación**: Navigation Compose
- **Coroutines**: Manejo asíncrono con Flow y StateFlow

### Estructura del Proyecto

```
app/src/main/java/com/crossfit/timer/
├── data/
│   ├── local/
│   │   ├── dao/              # Data Access Objects para Room
│   │   ├── database/         # Configuración de base de datos
│   │   └── entity/           # Entidades de base de datos
│   └── model/                # Modelos de datos y estados
├── di/                       # Módulos de inyección de dependencias
├── presentation/
│   ├── navigation/           # Configuración de navegación
│   ├── screens/
│   │   ├── config/          # Pantalla de configuración del timer
│   │   ├── history/         # Historial de entrenamientos
│   │   ├── home/            # Pantalla principal
│   │   ├── settings/        # Ajustes de la app
│   │   └── timer/           # Pantalla del temporizador
│   └── SharedConfigViewModel.kt  # ViewModel compartido
├── ui/theme/                 # Tema y estilos
└── util/                     # Utilidades y constantes
```

## Modos de Entrenamiento

### 1. AMRAP (As Many Rounds As Possible)
- Duración configurable en minutos
- Contador manual de rondas
- Beeps en los últimos segundos

### 2. EMOM (Every Minute On the Minute)
- Número configurable de rondas (minutos)
- Beep al inicio de cada minuto
- Muestra segundo dentro del minuto actual

### 3. For Time
- Cronómetro simple
- Time cap opcional
- Ideal para WODs con un objetivo de tiempo

### 4. Custom
- Intervalos personalizables (trabajo/descanso)
- Múltiples rondas
- Configuración detallada de cada intervalo

## Flujo de Navegación

```
HomeScreen
    ↓
ConfigScreen (configurar parámetros del modo)
    ↓
TimerScreen (ejecutar el temporizador)
    ↓
(Opcionalmente) HistoryScreen
```

## Componentes Clave

### ViewModels

#### TimerViewModel
- Maneja la lógica del temporizador
- Estados: Idle, Ready, Countdown, Running, Paused, Completed
- Funciones principales:
  - `startTimer()`: Inicia countdown y luego el temporizador
  - `pauseTimer()`: Pausa la ejecución
  - `stopTimer()`: Detiene y resetea a Idle
  - `resetTimer()`: Resetea manteniendo configuración (Ready)
  - `addRound()`: Incrementa contador de rondas (AMRAP/For Time)

#### SharedConfigViewModel
- **CRÍTICO**: Comparte configuración entre ConfigScreen y TimerScreen
- Debe ser instanciado una sola vez a nivel del NavGraph
- Previene pérdida de configuración entre pantallas

### Estados del Timer

```kotlin
sealed class TimerState {
    object Idle         // Sin configurar
    object Ready        // Configurado, listo para iniciar
    data class Countdown(val count: Int)  // Cuenta regresiva 3-2-1
    object Running      // Temporizador en ejecución
    object Paused       // Pausado
    object Completed    // Finalizado
}
```

## Problemas Conocidos y Soluciones

### Problema: Timer no inicia después de mostrar "LISTO"

**Síntoma**: Al presionar "INICIAR", aparece "LISTO" pero el temporizador no comienza.

**Causa**: Múltiples instancias del `SharedConfigViewModel` creadas con `hiltViewModel()` sin scope compartido.

**Solución**: Instanciar el `SharedConfigViewModel` a nivel del `NavGraph` y pasarlo como parámetro a ambas pantallas (ConfigScreen y TimerScreen).

```kotlin
// NavGraph.kt
@Composable
fun NavGraph(navController: NavHostController) {
    val sharedConfigViewModel: SharedConfigViewModel = hiltViewModel()

    NavHost(...) {
        composable(Screen.Config.route) {
            ConfigScreen(
                sharedViewModel = sharedConfigViewModel  // ✓ Misma instancia
            )
        }
        composable(Screen.Timer.route) {
            TimerScreen(
                sharedViewModel = sharedConfigViewModel  // ✓ Misma instancia
            )
        }
    }
}
```

## Características Implementadas

- ✅ Múltiples modos de entrenamiento
- ✅ Configuración personalizable por modo
- ✅ Countdown antes de iniciar (3-2-1-GO)
- ✅ Pausar/reanudar temporizador
- ✅ Contador de rondas (manual y automático)
- ✅ Orientación landscape forzada en pantalla del timer
- ✅ Base de datos local con Room
- ✅ Historial de entrenamientos
- ✅ Opciones de sonido y vibración

## Características Pendientes (TODO)

- ⏳ Implementar reproducción de sonidos
  - Beeps de countdown
  - Beeps en últimos segundos
  - Sonido de finalización
- ⏳ Implementar vibración
- ⏳ Guardar workouts completados en historial
- ⏳ Exportar/compartir resultados
- ⏳ Temas personalizables
- ⏳ WODs predefinidos

## Configuración del Entorno de Desarrollo

### Requisitos
- Android Studio Arctic Fox o superior
- JDK 11+
- Gradle 7.0+
- Android SDK 24+ (target 34)

### Dependencias Principales
```gradle
// Compose
implementation "androidx.compose.ui:ui"
implementation "androidx.compose.material3:material3"

// Navigation
implementation "androidx.navigation:navigation-compose"

// Hilt
implementation "com.google.dagger:hilt-android"
kapt "com.google.dagger:hilt-compiler"

// Room
implementation "androidx.room:room-runtime"
kapt "androidx.room:room-compiler"
implementation "androidx.room:room-ktx"

// Coroutines
implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android"
```

## Testing

### Unit Tests
- TimerViewModel: Lógica de estados y transiciones
- TimeFormatter: Formateo de tiempo

### Instrumentación Tests
- Navegación entre pantallas
- Guardar y recuperar configuraciones
- Persistencia en base de datos

## Contribución

Al trabajar en este proyecto:
1. Seguir arquitectura MVVM
2. Usar Compose para toda la UI
3. Inyección de dependencias con Hilt
4. Mantener ViewModels sin referencias a Context
5. Usar Flow/StateFlow para estados reactivos
6. Comentar lógica compleja en español

## Logs y Debugging

Para debuggear el flujo del timer:
- Agregar logs en `TimerViewModel.startTimer()`
- Verificar `_uiState.value.config` antes de iniciar
- Comprobar que `timerJob` no sea null durante ejecución

## Notas Importantes

1. **SharedConfigViewModel**: Siempre debe compartirse a nivel del NavGraph
2. **Orientación**: TimerScreen fuerza landscape automáticamente
3. **Lifecycle**: TimerJob se cancela en `onCleared()` del ViewModel
4. **Precisión**: Timer actualiza cada 100ms para mayor precisión visual
5. **Estados**: Las transiciones de estado son unidireccionales y predecibles

## Contacto y Soporte

Para issues, features o preguntas sobre la arquitectura, referirse a la documentación de cada componente individual en el código fuente.

---

**Última actualización**: 2025-11-17
**Versión**: 1.0.0
**Estado**: En desarrollo activo
