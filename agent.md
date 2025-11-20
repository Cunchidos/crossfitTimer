# CrossFit Timer - Agent Documentation

## Descripción del Proyecto

CrossFit Timer es una aplicación Android nativa desarrollada en Kotlin y Jetpack Compose que proporciona temporizadores especializados y herramientas para entrenamientos de CrossFit. La aplicación cuenta con una identidad visual moderna y enérgica, basada en una paleta de colores vibrantes.

## Arquitectura de la Aplicación

### Tecnologías Principales
- **Lenguaje**: Kotlin
- **UI Framework**: Jetpack Compose con Material 3
- **Arquitectura**: MVVM (Model-View-ViewModel)
- **Inyección de Dependencias**: Dagger Hilt
- **Base de Datos**: Room (para futura implementación de historial)
- **Navegación**: Navigation Compose
- **Asincronía**: Kotlin Coroutines & Flow / StateFlow

### Estructura del Proyecto

```
app/src/main/java/com/crossfit/timer/
├── data/              # Modelos de datos y repositorios
│   └── model/         # Clases de datos (TimerConfig, TimerMode, etc.)
├── di/                # Módulos de inyección de dependencias (Hilt)
├── presentation/      # Capa de UI (Compose Screens y ViewModels)
│   ├── navigation/    # Lógica de navegación (NavGraph, Screen)
│   └── screens/       # Las diferentes pantallas de la app (Home, Timer, Counter...)
├── ui/                # Tema, colores y tipografía
│   └── theme/
└── util/              # Clases de utilidad y constantes
```

## Modos de Entrenamiento

### 1. AMRAP (As Many Rounds As Possible)
- Duración configurable en minutos.

### 2. EMOM (Every Minute On the Minute)
- Número configurable de rondas (minutos).

### 3. For Time
- Cronómetro simple con un *time cap* opcional.

### 4. Custom
- Intervalos personalizables de trabajo y descanso.

### 5. Contador
- Un contador simple a pantalla completa que incrementa con cada toque.
- Fondo de color dinámico que cambia con cada pulsación.

## Flujo de Navegación

```
HomeScreen
    ├─> ConfigScreen (para AMRAP, EMOM, etc.)
    │   └─> TimerScreen (ejecuta el temporizador)
    │
    └─> CounterScreen (acceso directo al contador)
```

## Componentes Clave

### ViewModels

#### TimerViewModel
- Maneja la lógica de los temporizadores (AMRAP, EMOM, etc.).
- Gestiona los estados: Ready, Countdown, Running, Paused, Completed.

### Tema y Estilo
- **`CrossFitTimerTheme`**: Tema principal de la aplicación.
- **Paleta Juvenil**: Se ha implementado una paleta de colores personalizada (`VibrantTurquoise`, `EnergeticMagenta`, `HighlightYellow`) y se han desactivado los colores dinámicos del sistema para garantizar una identidad visual consistente y enérgica en todos los dispositivos.

## Características Implementadas

- ✅ 5 modos de entrenamiento, incluyendo el nuevo **Contador**.
- ✅ Configuración personalizable para los modos de temporizador.
- ✅ Tema de la aplicación completamente rediseñado con una paleta de colores juvenil y vibrante.
- ✅ Pantalla de contador con cambio de color de fondo dinámico.
- ✅ Arquitectura base robusta con Hilt, Navigation Compose y MVVM.

## Características Descartadas / Eliminadas

- ❌ **Sonido y Vibración**: Se ha eliminado toda la lógica y las opciones de la interfaz relacionadas con el sonido y la vibración para simplificar la aplicación.

## Próximos Pasos Potenciales

- ⏳ **Historial de Entrenamientos**: Guardar los resultados en una base de datos Room.
- ⏳ **Registro Manual**: Permitir a los usuarios añadir entrenamientos pasados.
- ⏳ **WODs Favoritos**: Guardar configuraciones de entrenamiento comunes.

---

**Última actualización**: Hecha para reflejar la adición del modo Contador y el nuevo tema visual.
**Versión**: 1.1.0
**Estado**: En desarrollo activo
