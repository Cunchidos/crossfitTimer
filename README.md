# CrossFit Timer ⏱️

Aplicación Android para cronómetros de CrossFit desarrollada con Kotlin y Jetpack Compose.

## 📱 Características

- **4 Modos de Entrenamiento:**
  - **AMRAP** - As Many Rounds As Possible
  - **EMOM** - Every Minute On the Minute
  - **For Time** - Cronómetro ascendente con Time Cap opcional
  - **Custom** - Intervalos personalizados configurables

- **Historial de Entrenamientos** con fotos y notas
- **WODs Favoritos** guardables
- **Base de Datos Local** (Room)
- **Orientación Apaisada** para pantalla del cronómetro

## 🛠️ Tecnologías

- **Kotlin** 2.0.21
- **Jetpack Compose** (Material3)
- **Room Database** 2.6.1
- **Hilt** (Dependency Injection) 2.50
- **Navigation Compose** 2.7.7
- **Coroutines** 1.7.3
- **DataStore**
- **Coil** (Imágenes)
- **Kotlinx Serialization**

## 🏗️ Arquitectura

- **MVVM** (Model-View-ViewModel)
- **Single Activity Architecture**
- **Clean Architecture** (Data, Domain, Presentation)
- **Repository Pattern**

## 📂 Estructura del Proyecto

```
app/src/main/java/com/crossfit/timer/
├── CrossFitTimerApp.kt
├── MainActivity.kt
├── data/
│   ├── local/
│   │   ├── dao/
│   │   ├── database/
│   │   └── entity/
│   └── model/
├── di/
├── presentation/
│   ├── navigation/
│   └── screens/
└── util/
```

## 🚀 Compilación

### Requisitos

- Android Studio Hedgehog (2023.1.1) o superior
- JDK 11 o superior
- Android SDK API 26+ (Android 8.0)
- Gradle 8.13.1

### Pasos

1. **Clonar/Abrir el proyecto:**
   ```bash
   cd C:\Users\PC\Desktop\programmer\android\CrossfitTimer
   ```

2. **Abrir en Android Studio:**
   - File → Open
   - Seleccionar la carpeta `CrossfitTimer`

3. **Sincronizar Gradle:**
   - Android Studio sincronizará automáticamente
   - O manualmente: File → Sync Project with Gradle Files

4. **Compilar:**
   - Build → Make Project
   - O usar: `Ctrl + F9`

5. **Ejecutar:**
   - Run → Run 'app'
   - O usar: `Shift + F10`

## ⚠️ Posibles Errores de Compilación

### Error: Duplicate property `displayName`
**Estado:** ✅ CORREGIDO

Este error ha sido solucionado en `HomeScreen.kt`.

### Error: compileSdk version
Si aparece error con `version = release(36)`:

**Archivo:** `app/build.gradle.kts` línea 10

**Cambiar de:**
```kotlin
compileSdk {
    version = release(36)
}
```

**A:**
```kotlin
compileSdk = 34
```

## 📝 Estado Actual del Proyecto

### ✅ Implementado

- [x] Configuración de dependencias (Hilt, Room, Compose)
- [x] Modelos de datos (TimerMode, TimerState, TimerConfig, etc.)
- [x] Base de datos Room (Entities, DAOs, Database)
- [x] Navegación completa entre pantallas
- [x] HomeScreen con selección de modos
- [x] MainActivity con Hilt
- [x] **TimerViewModel** con lógica completa del cronómetro
- [x] **TimerScreen** (pantalla del cronómetro en landscape)
- [x] **ConfigScreen** (configuración de parámetros por modo)
- [x] **HistoryScreen** (historial de entrenamientos con fotos)
- [x] **HistoryViewModel** (gestión del historial)
- [x] **SettingsScreen** (configuración global de la app)
- [x] Lógica del cronómetro (countdown 3-2-1-GO, intervalos, etc.)
- [x] Soporte para los 4 modos: AMRAP, EMOM, For Time, Custom
- [x] Contador de rondas (manual para AMRAP/ForTime, automático para EMOM/Custom)
- [x] Time Cap opcional para For Time

### 🚧 Pendiente de Implementar

- [ ] SavedWodsScreen (WODs favoritos guardables)
- [ ] Sistema de sonidos (MediaPlayer/SoundPool)
- [ ] Implementación de vibración
- [ ] Captura y selección de fotos (CameraX o Image Picker)
- [ ] Diálogo para guardar workout al finalizar
- [ ] Configuración avanzada de intervalos Custom (drag & drop)
- [ ] DataStore para persistir configuraciones
- [ ] Integración completa de sonidos en TimerViewModel
- [ ] Keep screen on durante el cronómetro
- [ ] Notificaciones durante el workout
- [ ] Tests unitarios y de UI

## 📖 Documentación Adicional

Ver `PROJECT_SPEC.md` para la especificación completa del proyecto.

## 👨‍💻 Próximos Pasos

1. **Compilar el proyecto** y verificar que todo funciona correctamente
2. **Implementar SavedWodsScreen** para guardar WODs favoritos
3. **Añadir sistema de sonidos** (MediaPlayer/SoundPool)
4. **Implementar captura de fotos** (CameraX o Image Picker)
5. **Crear diálogo para guardar workout** al finalizar
6. **Implementar DataStore** para persistir configuraciones
7. **Añadir vibración** en los momentos clave
8. **Keep screen on** durante el cronómetro
9. **Tests** unitarios y de UI

## 📄 Licencia

Este proyecto es de uso personal.

---

**Generado con Claude Code** 🤖
