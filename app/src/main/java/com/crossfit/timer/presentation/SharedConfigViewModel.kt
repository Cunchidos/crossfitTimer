// ruta: app/src/main/java/com/crossfit/timer/presentation/SharedConfigViewModel.kt

package com.crossfit.timer.presentation

import androidx.lifecycle.ViewModel
import com.crossfit.timer.data.model.TimerConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SharedConfigViewModel @Inject constructor() : ViewModel() {

    // Este StateFlow contendrá la configuración final que se compartirá
    private val _sharedConfig = MutableStateFlow<TimerConfig?>(null)
    val sharedConfig = _sharedConfig.asStateFlow()

    // Función para que ConfigScreen guarde la configuración final
    fun setConfig(config: TimerConfig) {
        _sharedConfig.value = config
    }
}
