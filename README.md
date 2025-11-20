# CrossFit Timer ⏱️

Una aplicación Android moderna, construida con Kotlin y Jetpack Compose, diseñada para atletas de CrossFit. Proporciona temporizadores flexibles y herramientas para registrar y seguir tu progreso.

## ✨ Características

- **5 Modos de Entrenamiento**:
  - **AMRAP**: Tantos asaltos/repeticiones como sea posible en un tiempo determinado.
  - **EMOM**: Realiza un ejercicio al inicio de cada minuto.
  - **For Time**: Completa una tarea lo más rápido posible, con un *time cap* opcional.
  - **Custom**: Crea tus propios intervalos de trabajo y descanso.
  - **Contador**: Un contador simple a pantalla completa para llevar la cuenta de rondas o repeticiones con un solo toque.

- **Identidad Visual Juvenil**: Una interfaz con una paleta de colores vibrante y enérgica (turquesas, magentas y amarillos) para una experiencia de usuario más atractiva.

- **Interfaz Optimizada**: La pantalla del temporizador fuerza la orientación horizontal para una mejor visualización durante el entrenamiento.

- **Arquitectura Moderna**: Construida sobre los últimos principios de desarrollo de Android para un rendimiento y mantenibilidad óptimos.

## 🛠️ Tecnologías Utilizadas

- **Lenguaje**: Kotlin
- **UI**: Jetpack Compose con Material 3
- **Arquitectura**: MVVM (Model-View-ViewModel)
- **Navegación**: Navigation Compose
- **Inyección de Dependencias**: Dagger Hilt
- **Base de Datos**: Room para persistencia local
- **Asincronía**: Kotlin Coroutines & Flow / StateFlow

## 🚀 Cómo Empezar

### Requisitos
- Android Studio (última versión recomendada)
- JDK 11 o superior

### Pasos

1.  **Clona o descarga** el repositorio en tu máquina local.
2.  **Abre el proyecto** con Android Studio.
3.  **Sincroniza Gradle** y espera a que se descarguen todas las dependencias.
4.  **Ejecuta la aplicación** en un emulador o dispositivo físico.

## 📂 Estructura del Proyecto

La aplicación sigue una arquitectura limpia y modular, separando las responsabilidades en diferentes capas:

```
app/src/main/java/com/crossfit/timer/
├── data/              # Modelos de datos, fuentes de datos (local/remota) y repositorios
│   ├── local/         # Clases de Room (DAO, Entities, Database)
│   └── model/         # Clases de datos (TimerConfig, TimerMode, etc.)
├── di/                # Módulos de inyección de dependencias (Hilt)
├── presentation/      # Capa de UI (Compose Screens y ViewModels)
│   ├── navigation/    # Lógica de navegación (NavGraph, Screen)
│   └── screens/       # Las diferentes pantallas de la app (Home, Timer, Counter...)
├── ui/                # Tema, colores y tipografía
│   └── theme/
└── util/              # Clases de utilidad y constantes
```

## 📝 Estado y Próximos Pasos

El núcleo de la aplicación está implementado, pero siempre hay espacio para mejorar y añadir nuevas funcionalidades.

### ✅ Implementado

- [x] Navegación completa entre todas las pantallas.
- [x] Lógica de temporizador para los modos **AMRAP, EMOM, For Time y Custom**.
- [x] Pantalla de **Contador** de rondas a pantalla completa.
- [x] Tema personalizado con una paleta de colores juvenil y enérgica.
- [x] Arquitectura base con Hilt, Room y MVVM.

### 🚧 Próximos Pasos

- [ ] **Historial de Entrenamientos**: Guardar los resultados de los WODs en la base de datos local.
- [ ] **Registro Manual**: Implementar la pantalla para añadir entrenamientos pasados de forma manual.
- [ ] **Sonidos y Vibración**: Añadir feedback auditivo y táctil durante los entrenamientos.
- [ ] **WODs Favoritos**: Permitir a los usuarios guardar y acceder rápidamente a sus entrenamientos más comunes.

---
*Este README ha sido generado y actualizado para reflejar el estado actual del proyecto.*
